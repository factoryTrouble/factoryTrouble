package de.uni_bremen.factroytrouble.ai.ki1.configreader;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runners.Parameterized.Parameter;

import de.uni_bremen.factroytrouble.exceptions.KeyNotFoundException;

public class ConfigReaderTest {
    @Parameter
    ConfigReader config;
    ConfigReader config2;
    ConfigReader configTest;

    @Before
    public void create() {
        try {
            config = AgentConfigReader.getInstance(0);
            config2 = AgentConfigReader.getInstance(1);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test(expected = IOException.class)
    public void throwsIOExceptionTestGetInstance() throws IOException {
        configTest = AgentConfigReader.getInstance(-1);
    }
    
    @Test
    public void testGetInstance(){
        try {
            assertEquals(config2, AgentConfigReader.getInstance(1));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testStringProperty() throws KeyNotFoundException {
        assertEquals("string", config.getStringProperty("Test.String"));
    }

    @Test
    public void testDoubleProperty() throws KeyNotFoundException {
        assertEquals(1.0, config.getDoubleProperty("Test.Double"), 0.0001);
    }

    @Test
    public void testIntProperty() throws KeyNotFoundException {
        assertEquals(50, config.getIntProperty("Test.Int"));
    }

    @Test
    public void testBoolProperty() throws KeyNotFoundException {
        assertTrue(config.getBoolProperty("Test.Boolean"));
    }

    @Test(expected = KeyNotFoundException.class)
    public void throwsKeyNotFoundExceptionTestBoolProperty() throws KeyNotFoundException {
        config.getBoolProperty("Test.NotFound");
    }

    @Test(expected = KeyNotFoundException.class)
    public void throwsKeyNotFoundExceptionTestStringProperty() throws KeyNotFoundException {
        config.getStringProperty("Test.NotFound");
    }

    @Test(expected = KeyNotFoundException.class)
    public void throwsKeyNotFoundExceptionTestDoubleProperty() throws KeyNotFoundException {
        config.getDoubleProperty("Test.NotFound");
    }

    @Test(expected = KeyNotFoundException.class)
    public void throwsKeyNotFoundExceptionTestIntProperty() throws KeyNotFoundException {
        config.getIntProperty("Test.NotFound");
    }
}
