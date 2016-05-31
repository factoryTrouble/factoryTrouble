package de.uni_bremen.factroytrouble.editor.service.save;

import de.uni_bremen.factroytrouble.editor.data.Orientation;
import de.uni_bremen.factroytrouble.editor.data.TileWithPosition;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class SaveCourseServiceTest {

    @Mock private SaveInUserDirService saveInUserDirService;
    @InjectMocks private SaveCourseService saveCourseService;
    @Captor private ArgumentCaptor<String> courseString;

    private Map<Integer, TileWithPosition> flags;

    @Before
    public void setUp() {
        flags = new HashMap<>();
        flags.put(1, new TileWithPosition(null, new Point(1, 1)));
    }

    @Test
    public void shouldSerializeACourse() {
        saveCourseService.save("test", "FIELD_test", "DOCK_test", Orientation.NORTH, flags);
        verify(saveInUserDirService).saveCourse(courseString.capture(), eq("CUSTOM_COURSE_MANUAL"));
        assertEquals("aaa-custom-test,DOCK_test,FIELD_test_NORTH,1_1", courseString.getValue());
    }

    @Test
    public void shouldSaveTheCourse() {
        saveCourseService.save("test", "DOCK_test", "FIELD_test", Orientation.NORTH, flags);
        verify(saveInUserDirService).saveCourse(anyString(), eq("CUSTOM_COURSE_MANUAL"));
    }

}