package com.example.models;

public class FileInfo {
    private final String name;
    private final Long size;
    private final String creationDate;
    private final boolean isDirectory;

    public FileInfo(String name, Long size, String creationDate, boolean isDirectory) {
        this.name = name;
        this.size = size;
        this.creationDate = creationDate;
        this.isDirectory = isDirectory;
    }

    public String getName() {
        return name;
    }

    public Long getSize() {
        return size;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public boolean isDirectory() {
        return isDirectory;
    }
}
