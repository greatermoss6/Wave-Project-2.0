package com.signlearn.domain.service;

import com.signlearn.domain.model.Module;
import com.signlearn.domain.model.Chapter;
import com.signlearn.domain.model.Lesson;
import java.util.List;

public interface LearningService {
    List<Module> listModules();
    List<Chapter> listChapters(long moduleId);
    List<Lesson> listLessons(long chapterId);
}