package de.uni_bremen.factroytrouble.editor.component;

import de.saxsys.javafx.test.JfxRunner;
import de.uni_bremen.factroytrouble.editor.ApplicationSettings;
import de.uni_bremen.factroytrouble.editor.controller.AbstractEditorController;
import de.uni_bremen.factroytrouble.editor.data.GroundFill;
import de.uni_bremen.factroytrouble.editor.service.util.ActiveEditorService;
import de.uni_bremen.factroytrouble.editor.spring.SpringConfigHolder;
import javafx.geometry.Insets;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static de.uni_bremen.factroytrouble.editor.TestUtil.fireMouseEvent;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(JfxRunner.class)
public class EditorTileFillToolIconTest {

    @Mock private AnnotationConfigApplicationContext context;
    @Mock private ActiveEditorService activeEditorService;
    @Mock private AbstractEditorController abstractEditorController;
    private EditorTileFillToolIcon editorTileFillToolIcon;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        SpringConfigHolder.getInstance().setContext(context);
        editorTileFillToolIcon = new EditorTileFillToolIcon(null, "test", GroundFill.EMPTY);
        when(context.getBean(ActiveEditorService.class)).thenReturn(activeEditorService);
        when(activeEditorService.getActiveEditor()).thenReturn(abstractEditorController);
    }

    @Test
    public void shouldSetTheActiveToolOnMouseClick() {
        fireMouseEvent(editorTileFillToolIcon, 0.0, 0.0, 0.0, 0.0);
        verify(abstractEditorController).setActiveTool(editorTileFillToolIcon);
    }

    @Test
    public void shouldSetABorderWhenToolIconCliekced() {
        fireMouseEvent(editorTileFillToolIcon, 0.0, 0.0, 0.0, 0.0);
        assertEquals("-fx-border-width: 5px;  -fx-border-style: solid; -fx-border-color: " + ApplicationSettings.ORANGE+ ";", editorTileFillToolIcon.getStyle());
    }

    @Test
    public void shouldSetPaddingNullWhenToolIconCliekced() {
        fireMouseEvent(editorTileFillToolIcon, 0.0, 0.0, 0.0, 0.0);
        assertEquals(new Insets(0), editorTileFillToolIcon.getPadding());
    }

    @Test
    public void shouldResetTheBorder() {
        editorTileFillToolIcon.resetClickedStatus();
        assertEquals("-fx-border-width: 0px", editorTileFillToolIcon.getStyle());
    }


    @Test
    public void shouldResetThePadding() {
        editorTileFillToolIcon.resetClickedStatus();
        assertEquals(new Insets(5), editorTileFillToolIcon.getPadding());
    }

}