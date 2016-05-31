package de.uni_bremen.factroytrouble;

import java.lang.reflect.Field;

/**
 * Hilfmittel für Unit Tests
 *
 * @author Andre
 *
 */
public class TestUtil {

    public static void injectPrivateField(String fieldName, Object targetObject, Object fieldObject) throws NoSuchFieldException, IllegalAccessException {
        Field javaFxElementField = targetObject.getClass().getDeclaredField(fieldName);
        javaFxElementField.setAccessible(true);
        javaFxElementField.set(targetObject, fieldObject);
    }

}
