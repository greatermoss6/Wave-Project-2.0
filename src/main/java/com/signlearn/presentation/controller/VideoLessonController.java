package com.signlearn.presentation.controller;

import com.signlearn.app.context.LearningContext;
import com.signlearn.app.router.SceneRouter;
import com.signlearn.app.router.View;
import com.signlearn.app.dependency_injection.DependencyAware;
import com.signlearn.app.dependency_injection.ServiceRegistry;
import com.signlearn.domain.model.Video;
import com.signlearn.domain.service.VideoService;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

import java.io.File;

/**
 * Controller for displaying and playing video lessons.
 */
public class VideoLessonController extends BaseController implements DependencyAware {

    @FXML private MediaView mediaView;
    @FXML private Button nextBtn;
    @FXML private Button backBtn;

    private MediaPlayer mediaPlayer;
    private SceneRouter router;
    private VideoService videoService;

    @Override
    public void setRouter(SceneRouter router) {
        this.router = router;
    }

    @Override
    public void injectDependencies(ServiceRegistry registry) {
        this.videoService = registry.getVideoService();
    }

    @Override
    public void postInit() {
        long lessonId = LearningContext.getCurrentLessonId();
        videoService.getVideoForLesson(lessonId).ifPresent(this::playVideo);

        nextBtn.setOnAction(e -> {
            if (mediaPlayer != null) mediaPlayer.stop();
            router.goTo(View.QUESTION);
        });

        backBtn.setOnAction(e -> {
            if (mediaPlayer != null) mediaPlayer.stop();
            router.goTo(View.LEARNING);
        });
    }

    private void playVideo(Video video) {
        File file = new File(video.getFilePath());
        if (file.exists()) {
            Media media = new Media(file.toURI().toString());
            mediaPlayer = new MediaPlayer(media);
            mediaView.setMediaPlayer(mediaPlayer);
            mediaPlayer.play();
        }
    }
}