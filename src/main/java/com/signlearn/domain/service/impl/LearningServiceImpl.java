package com.signlearn.domain.service.impl;

import com.signlearn.domain.model.Module;
import com.signlearn.domain.model.Chapter;
import com.signlearn.domain.model.Lesson;
import com.signlearn.persistence.repo.ModuleRepository;
import com.signlearn.persistence.repo.ChapterRepository;
import com.signlearn.persistence.repo.LessonRepository;
import com.signlearn.domain.service.LearningService;

import java.util.List;

public class LearningServiceImpl implements LearningService {
    private final ModuleRepository moduleRepo;
    private final ChapterRepository chapterRepo;
    private final LessonRepository lessonRepo;

    public LearningServiceImpl(ModuleRepository moduleRepo,
                               ChapterRepository chapterRepo,
                               LessonRepository lessonRepo) {
        this.moduleRepo = moduleRepo;
        this.chapterRepo = chapterRepo;
        this.lessonRepo = lessonRepo;
    }

    @Override
    public List<Module> listModules() {
        return moduleRepo.findAll();
    }

    @Override
    public List<Chapter> listChapters(long moduleId) {
        return chapterRepo.findByModuleId(moduleId);
    }

    @Override
    public List<Lesson> listLessons(long chapterId) {
        return lessonRepo.findByChapterId(chapterId);
    }
}