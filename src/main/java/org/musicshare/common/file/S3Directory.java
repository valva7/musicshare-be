package org.musicshare.common.file;

public enum S3Directory {

    MUSIC("music/"),
    IMAGE("image/"),
    VIDEO("video/"),
    PROFILE("profile/");

    private final String directory;

    S3Directory(String directory) {
        this.directory = directory;
    }

    public String getDirectory() {
        return directory;
    }
}
