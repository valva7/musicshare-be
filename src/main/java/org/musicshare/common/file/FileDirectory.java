package org.musicshare.common.file;

public enum FileDirectory {

    MUSIC("music/"),
    IMAGE("image/"),
    VIDEO("video/"),
    PROFILE("profile/");

    private final String directory;

    FileDirectory(String directory) {
        this.directory = directory;
    }

    public String getDirectory() {
        return directory;
    }
}
