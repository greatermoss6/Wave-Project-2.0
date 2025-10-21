package com.signlearn.domain.service;

import com.signlearn.domain.model.Chapter;
import com.signlearn.domain.model.Lesson;
import com.signlearn.domain.model.Module;
import com.signlearn.domain.model.Progress;
import com.signlearn.domain.service.impl.ProgressServiceImpl;
import com.signlearn.persistence.repo.ChapterRepository;
import com.signlearn.persistence.repo.LessonRepository;
import com.signlearn.persistence.repo.ModuleRepository;
import com.signlearn.persistence.repo.ProgressRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProgressServiceImplTest {

    @Mock private ProgressRepository progressRepo;
    @Mock private LessonRepository lessonRepo;
    @Mock private ChapterRepository chapterRepo;
    @Mock private ModuleRepository moduleRepo;

    private ProgressServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new ProgressServiceImpl(progressRepo, lessonRepo, chapterRepo, moduleRepo);
    }

    @Test
    void recordAttempt_upsertsProgress_withCompletedFalse_andGivenScore() {
        long userId = 7L;
        long lessonId = 101L;
        int score = 85;

        service.recordAttempt(userId, lessonId, score);

        ArgumentCaptor<Progress> cap = ArgumentCaptor.forClass(Progress.class);
        verify(progressRepo).upsert(cap.capture());
        Progress p = cap.getValue();

        assertEquals(userId, p.getUserId());
        assertEquals(lessonId, p.getLessonId());
        assertFalse(p.isCompleted(), "recordAttempt should not mark as completed");
        assertEquals(Integer.valueOf(score), p.getScorePercent());
        verifyNoMoreInteractions(progressRepo);
    }

    @Test
    void markCompleted_whenExistingProgress_preservesScore_andMarksCompletedTrue() {
        long userId = 3L;
        long lessonId = 42L;
        Progress existing = new Progress(userId, lessonId, false, 70);

        when(progressRepo.find(userId, lessonId)).thenReturn(Optional.of(existing));

        service.markCompleted(userId, lessonId);

        ArgumentCaptor<Progress> cap = ArgumentCaptor.forClass(Progress.class);
        verify(progressRepo).find(userId, lessonId);
        verify(progressRepo).upsert(cap.capture());
        Progress updated = cap.getValue();

        assertEquals(userId, updated.getUserId());
        assertEquals(lessonId, updated.getLessonId());
        assertTrue(updated.isCompleted(), "markCompleted should set completed=true");
        assertEquals(Integer.valueOf(70), updated.getScorePercent(), "Score should be preserved");
        verifyNoMoreInteractions(progressRepo);
    }

    @Test
    void markCompleted_whenNoExistingProgress_createsCompletedWithZeroScore() {
        long userId = 9L;
        long lessonId = 55L;

        when(progressRepo.find(userId, lessonId)).thenReturn(Optional.empty());

        service.markCompleted(userId, lessonId);

        ArgumentCaptor<Progress> cap = ArgumentCaptor.forClass(Progress.class);
        verify(progressRepo).find(userId, lessonId);
        verify(progressRepo).upsert(cap.capture());
        Progress created = cap.getValue();

        assertEquals(userId, created.getUserId());
        assertEquals(lessonId, created.getLessonId());
        assertTrue(created.isCompleted());
        assertEquals(Integer.valueOf(0), created.getScorePercent(), "New completed progress defaults to 0%");
        verifyNoMoreInteractions(progressRepo);
    }

    @Test
    void getProgress_delegatesToRepository() {
        long userId = 1L;
        long lessonId = 2L;
        Progress progress = new Progress(userId, lessonId, false, 60);
        when(progressRepo.find(userId, lessonId)).thenReturn(Optional.of(progress));

        Optional<Progress> result = service.getProgress(userId, lessonId);

        assertTrue(result.isPresent());
        assertSame(progress, result.get());
        verify(progressRepo).find(userId, lessonId);
        verifyNoMoreInteractions(progressRepo);
    }

    @Test
    void summarizeByModule_groupsByModule_andAveragesScores() {
        long userId = 100L;

        // Progress entries: (L1:80 in M10), (L2:60 in M10), (L3:100 in M20)
        Progress p1 = new Progress(userId, 1L, false, 80);
        Progress p2 = new Progress(userId, 2L, true, 60);
        Progress p3 = new Progress(userId, 3L, true, 100);

        when(progressRepo.findByUser(userId)).thenReturn(List.of(p1, p2, p3));

        // Mock the lesson → chapter → module chain
        Lesson l1 = mock(Lesson.class);
        Lesson l2 = mock(Lesson.class);
        Lesson l3 = mock(Lesson.class);
        when(lessonRepo.findById(1L)).thenReturn(Optional.of(l1));
        when(lessonRepo.findById(2L)).thenReturn(Optional.of(l2));
        when(lessonRepo.findById(3L)).thenReturn(Optional.of(l3));

        Chapter cA = mock(Chapter.class); // for M10
        Chapter cB = mock(Chapter.class); // for M20
        when(cA.getModuleId()).thenReturn(10L);
        when(cB.getModuleId()).thenReturn(20L);

        when(l1.getChapterId()).thenReturn(1000L);
        when(l2.getChapterId()).thenReturn(1000L);
        when(l3.getChapterId()).thenReturn(2000L);

        when(chapterRepo.findById(1000L)).thenReturn(Optional.of(cA));
        when(chapterRepo.findById(2000L)).thenReturn(Optional.of(cB));

        Module m10 = mock(Module.class);
        Module m20 = mock(Module.class);
        when(m10.getId()).thenReturn(10L);
        when(m20.getId()).thenReturn(20L);
        when(moduleRepo.findById(10L)).thenReturn(Optional.of(m10));
        when(moduleRepo.findById(20L)).thenReturn(Optional.of(m20));

        Map<Long, Double> result = service.summarizeByModule(userId);

        assertEquals(2, result.size());
        assertEquals(70.0, result.get(10L), 0.0001, "Average of 80 and 60");
        assertEquals(100.0, result.get(20L), 0.0001);
        verify(progressRepo).findByUser(userId);
    }

    @Test
    void summarizeByModule_assignsUnknownsToMinusOneWhenLookupFails() {
        long userId = 200L;

        // Progress for a lesson that cannot be resolved to a module
        Progress p = new Progress(userId, 9999L, false, 50);
        when(progressRepo.findByUser(userId)).thenReturn(List.of(p));

        when(lessonRepo.findById(9999L)).thenReturn(Optional.empty()); // lookup fails → -1L bucket

        Map<Long, Double> result = service.summarizeByModule(userId);

        assertEquals(1, result.size());
        assertTrue(result.containsKey(-1L), "Unresolvable lessons should be grouped under -1 key");
        assertEquals(50.0, result.get(-1L), 0.0001);
    }
}
