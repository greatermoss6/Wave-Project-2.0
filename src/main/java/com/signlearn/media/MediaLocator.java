package com.signlearn.media;

import com.signlearn.domain.model.Video;
import java.net.URI;

public class MediaLocator {
    public URI resolve(Video video) {
        return URI.create(video.toString());
    }
}