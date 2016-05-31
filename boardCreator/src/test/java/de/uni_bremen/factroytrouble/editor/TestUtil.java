package de.uni_bremen.factroytrouble.editor;

import javafx.event.Event;
import javafx.event.EventTarget;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Hilfsmethoden zum Testen
 *
 * @author Andre
 */
public class TestUtil {

    /**
     * Löst ein Mouse-Click aus
     *
     * @param eventTarget
     *      Objekt, auf dem das Event ausgelöst wird
     *
     * @param positionX
     *      Mauspostion in X
     *
     * @param positionY
     *      Mauspostion in Y
     *
     * @param screenX
     *      Mauspostion auf dem Screen in X
     *
     * @param screenY
     *      Mauspostion auf dem Screen in Y
     */
    public static void fireMouseEvent(EventTarget eventTarget, Double positionX, Double positionY, Double screenX, Double screenY) {
        Event.fireEvent(eventTarget, new MouseEvent(MouseEvent.MOUSE_CLICKED, positionX, positionY, screenX, screenY, MouseButton.PRIMARY, 1, true, true, true, true, true, true, true, true, true, true, null));
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
    public static void callPrivateMethodInSuperclassWithParameter(String methodName, Object targetObject, Class[] types,  Object... args) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method buttonHandlerMethod = targetObject.getClass().getSuperclass().getDeclaredMethod(methodName, types);
        buttonHandlerMethod.setAccessible(true);
        buttonHandlerMethod.invoke(targetObject, args);
    }

    /**
     * Liest den Inhalt einer Dati und gibt diesen als String zurück
     *
     * @param path
     *      Pfad zur Datei
     *
     * @return
     *      Inhalt der Datei
     * @throws IOException
     */
    public static String readContentFromFile(String path) throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, Charset.defaultCharset());
    }

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
