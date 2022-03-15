package com.github.bogdan.service;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class ClassService {
    public static ArrayList<String> getFieldsName(Class clazz) {
        ArrayList<String> fields = new ArrayList<>();
        for(Field field: clazz.getDeclaredFields()){
           fields.add(field.getName());
        }
        return fields;
    }
}
