package com.batcheador.utils;

import java.lang.reflect.Field;
import java.util.Arrays;

public class Validator {
    public static boolean IsValid (Object currentApp){
        Field[] fields = currentApp.getClass().getDeclaredFields();

        return Arrays.stream(fields).allMatch(field -> validate(field,currentApp));
    }

    public static boolean validate (Field field, Object currentApp){
        field.setAccessible(true);
        try {
            Object value = field.get(currentApp);
            return (value != null && !value.equals(""));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return false;
        }
    }
}
