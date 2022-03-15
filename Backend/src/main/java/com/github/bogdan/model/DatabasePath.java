package com.github.bogdan.model;

public class DatabasePath {
    private static String path;

    public DatabasePath(String path) {
        DatabasePath.path = path;
    }

    public static String getPath() {
        return path;
    }

    public static void setPath(String path) {
        DatabasePath.path = path;
    }
}
