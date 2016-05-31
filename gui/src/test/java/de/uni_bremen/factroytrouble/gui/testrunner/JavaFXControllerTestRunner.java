package de.uni_bremen.factroytrouble.gui.testrunner;

import javafx.event.ActionEvent;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static de.uni_bremen.factroytrouble.gui.TestUtil.injectPrivateField;

/**
 * Stellt zusätzlich Methoden zum Testen von JavaFX Controllern zur Verfüngung.
 * Zum ausführen von JavaFX Test muss die Test Klasse zusätlich mit @link{JfxRunner.class} annotiert werden
 *
 * @author Andre
 */
public abstract class JavaFXControllerTestRunner {

    /**
     * Ruft eine Private Methode auf, die als EventHandler benutzt wird. Diese Methoden bekommen dabei nur einen Parameter
     * von Typ @link{ActionEvent.class}
     *
     * @param methodName  Name der Handler Methode (ohne Klammern oder Signatur)
     * @param controller  Die Instanz des JavaFX Controllers
     * @param actionEvent Das Action Event, welches den Aufruf simuliert hat
     * @throws NoSuchMethodException     Wenn die Methode nicht gefunden wird
     * @throws InvocationTargetException Wenn Fehler innerhalb der Methode auftreten
     * @throws IllegalAccessException    Wenn man keinen Zugriff auf die Methode hat
     */
    public void callPrivateHandleMethod(String methodName, Object controller, ActionEvent actionEvent) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method buttonHandlerMethod = controller.getClass().getDeclaredMethod(methodName, ActionEvent.class);
        buttonHandlerMethod.setAccessible(true);
        buttonHandlerMethod.invoke(controller, actionEvent);
    }

    /**
     * Ruft die Methode zum registrieren eines ChangeListeners auf. Diese darf keine Parameter enthalten
     *
     * @param methodName Name der Registrierungsmethode (ohne Klammern oder Signatur)
     * @param controller Die Instanz des JavaFX Controllers
     * @throws NoSuchMethodException     Wenn die Methode nicht gefunden wird
     * @throws InvocationTargetException Wenn Fehler innerhalb der Methode auftreten
     * @throws IllegalAccessException    Wenn man keinen Zugriff auf die Methode hat
     */
    public void callPrivateChangeListenerAddMethod(String methodName, Object controller) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method buttonHandlerMethod = controller.getClass().getDeclaredMethod(methodName);
        buttonHandlerMethod.setAccessible(true);
        buttonHandlerMethod.invoke(controller);
    }

    /**
     * Überschreibt ein Attribut in einen JavaFX Controller. Wird eingesetzt, um JavaFX Objekte des Controllers zu erzeugen
     *
     * @param fieldName    Name des Feldes, welche beschrieben werden soll
     * @param controller   Die Instanz des JavaFX Controllers
     * @param javaFxObject Das JavaFX Objekt, welches hinzugefügt werden soll
     * @throws NoSuchFieldException   Wenn das Attribut nicht exitiert
     * @throws IllegalAccessException Wenn man keinen Zugriff auf das Attribut hat
     */
    public void injectJavaFXObjectToController(String fieldName, Object controller, Object javaFxObject) throws NoSuchFieldException, IllegalAccessException {
        injectPrivateField(fieldName, controller, javaFxObject);
    }

}
