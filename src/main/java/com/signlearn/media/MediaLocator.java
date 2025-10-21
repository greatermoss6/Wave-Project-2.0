package com.signlearn.media;

import java.net.URI;
import com.signlearn.domain.model.Video;

public class MediaLocator {
    public URI resolve(Video video) {
        return URI.create(video.toString());
    }
}