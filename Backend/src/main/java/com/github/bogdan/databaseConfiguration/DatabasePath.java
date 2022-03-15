package com.github.bogdan.databaseConfiguration;

public class DatabasePath {
    public static String getBagaPath() {
        String bagaPath=System.getenv("DB_PATH");
        return bagaPath;
    }

    public String getSalaPath() {
        String salaPath="jdbc:sqlite:С:\\Users\\MI\\Desktop\\suppeople.sqlite";
        return salaPath;
    }
    
    
}
