package de.uni_bremen.factroytrouble.gui.generator.board;

import de.uni_bremen.factroytrouble.gui.generator.image.LoadImageService;
import de.uni_bremen.factroytrouble.gui.generator.image.SaveImageService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.awt.Image;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class NewGameScreenBoardServiceTest {

    @Mock private LoadImageService loadImageService;
    @Mock private SaveImageService saveImageService;
    @Mock private BoardGenerator boardGenerator;
    @InjectMocks private NewGameScreenBoardService newGameScreenBoardService = new NewGameScreenBoardService();
    private static final Path WANTEDPATH = Paths.get("boards", "preview","test.png");

    @Test
    public void shouldLoadTheImage() {
        when(loadImageService.getImageByFileName(anyString())).thenReturn(mock(Image.class));
        newGameScreenBoardService.getPreviewImage("test");
        verify(loadImageService).getImageByFileName(eq(File.separator + WANTEDPATH));
    }

    @Test
    public void shouldGenerateANewImageWhenImageNotFound() {
        when(loadImageService.getImageByFileName(anyString())).thenReturn(null);
        newGameScreenBoardService.getPreviewImage("test");
        verify(boardGenerator).generateBoard(eq("test"));
    }

    @Test
    public void shouldSaveAResizedBoardImageWhenImageNotFound() {
        when(loadImageService.getImageByFileName(anyString())).thenReturn(null);
        newGameScreenBoardService.getPreviewImage("test");
        String dir = File.separator + WANTEDPATH.getParent().toString();
        String file = WANTEDPATH.getFileName().toString();
        verify(saveImageService).saveResized(any(Image.class), eq(dir), eq(file), anyInt());
    }

    @Test
    public void shouldReloadTheImageWhenImageNotFoundTheFirstTime() {
        when(loadImageService.getImageByFileName(anyString())).thenReturn(null);
        newGameScreenBoardService.getPreviewImage("test");
        verify(loadImageService, times(2)).getImageByFileName(eq(File.separator + WANTEDPATH));
    }

}