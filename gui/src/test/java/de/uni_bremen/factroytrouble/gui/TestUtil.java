package de.uni_bremen.factroytrouble.gui;

import javafx.scene.Parent;

import java.awt.image.BufferedImage;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Hilfmittel für Unit Tests
 *
 * @author Andre
 *
 */
public class TestUtil {

    /**
     * Vergleicht zwei Bilder Pixel für Pixel auf Gleichheit.
     *
     * @param expected
     *      Das zu erwartende Bild
     * @param actual
     *      Das zu testende Bild
     */
    public static void assertImageEquals(BufferedImage expected, BufferedImage actual) {
        assert compareImage(expected, actual) == true;
    }

    /**
     * Schaut, ob zwei View gleich sind
     *
     * @param expected
     *      Die erwartete View
     * @param actual
     *      Die zu Testende View
     */
    public static void assertParentEquals(Parent expected, Parent actual) {
        assert expected.getClass().equals(actual.getClass());
        assert expected.getTypeSelector().equals(actual.getTypeSelector());
        assert expected.getLayoutX() == actual.getLayoutX();
        assert expected.getLayoutY() == actual.getLayoutY();
    }

    /**
     * Fügt einen beliebeigen Wert einen privaten Feld hinzu
     *
     * @param fieldName
     *      Name des Feldes
     * @param targetObject
     *      Objekt, indem sich das Feld befindet
     * @param fieldObject
     *      Objekt, welches injected werden soll
     */
    public static void injectPrivateField(String fieldName, Object targetObject, Object fieldObject) throws NoSuchFieldException, IllegalAccessException {
        Field field = targetObject.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(targetObject, fieldObject);
    }

    /**
     * Fügt einen beliebeigen Wert einen privaten Feld hinzu der Superklasse hinzu
     *
     * @param fieldName
     *      Name des Feldes
     * @param targetObject
     *      Objekt, indem sich das Feld befindet
     * @param fieldObject
     *      Objekt, welches injected werden soll
     */
    public static void injectPrivateFieldIntoParent(String fieldName, Object targetObject, Object fieldObject) throws NoSuchFieldException, IllegalAccessException {
        Field field = targetObject.getClass().getSuperclass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(targetObject, fieldObject);
    }

    /**
     * Gibt den Wert eines privaten Feldes zurück
     *
     * @param fieldName
     *      Name des Feldes
     * @param targetObject
     *      Objekt, indem sich das Feld befindet
     * @return
     *      Wert des Feldes
     */
    public static Object getPrivateField(String fieldName, Object targetObject) throws NoSuchFieldException, IllegalAccessException {
        Field field = targetObject.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.get(targetObject);
    }

    /**
     * Gibt den Wert eines privaten Feldes der Superklasse zurück
     *
     * @param fieldName
     *      Name des Feldes
     * @param targetObject
     *      Objekt, indem sich das Feld befindet
     * @return
     *      Wert des Feldes
     */
    public Object getPrivateFieldFromSuperClass(String fieldName, Object targetObject) throws NoSuchFieldException, IllegalAccessException {
        Field field = targetObject.getClass().getSuperclass().getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.get(targetObject);
    }

    /**
     * Führt eine private Methode auf einen Objekt aus (Die Methode darf keine Parameter haben)
     *
     * @param methodName
     *      Name der Methode
     *
     * @param targetObject
     *      Objeckt, auf dem die Methode ausgeführt werden soll
     */
    public static void callPrivateMethodeWithoutParameter(String methodName, Object targetObject) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method buttonHandlerMethod = targetObject.getClass().getDeclaredMethod(methodName);
        buttonHandlerMethod.setAccessible(true);
        buttonHandlerMethod.invoke(targetObject);
    }

    /**
     * Führt eine private Methode auf einen Objekt aus (Die Methode kann Parameter haben)
     *
     * @param methodName
     *      Name der Methode
     *
     * @param targetObject
     *      Objeckt, auf dem die Methode ausgeführt werden soll
     *
     * @param types
     *      Typen der Parameter
     *
     * @param args
     *      Argumete der Methode
     */
    public static void callPrivateMethodeWithParameter(String methodName, Object targetObject, Class[] types,  Object... args) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method buttonHandlerMethod = targetObject.getClass().getDeclaredMethod(methodName, types);
        buttonHandlerMethod.setAccessible(true);
        buttonHandlerMethod.invoke(targetObject, args);
    }

    /**
     * Quelle: http://stackoverflow.com/questions/11006394/is-there-a-simple-way-to-compare-bufferedimage-instances (Mr. Polywhirl)
     *
     * @param imgA
     *      Der erwartete Bild
     * @param imgB
     *      Das zu testende Bild
     * @return
     *      Ist das Bild gleich?
     */
    private static boolean compareImage(BufferedImage imgA, BufferedImage imgB) {
        // The images must be the same size.
        if (imgA.getWidth() == imgB.getWidth() && imgA.getHeight() == imgB.getHeight()) {
            int width = imgA.getWidth();
            int height = imgA.getHeight();

            // Loop over every pixel.
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    // Compare the pixels for equality.
                    if (imgA.getRGB(x, y) != imgB.getRGB(x, y)) {
                        return false;
                    }
                }
            }
        } else {
            return false;
        }

        return true;
    }

}
