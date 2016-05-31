package de.uni_bremen.factroytrouble.gui.services.util;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ApplicationArgumentsServiceTest {

    private ApplicationArgumentsService applicationArgumentsService;

    @Before
    public void setUp() {
        applicationArgumentsService = new ApplicationArgumentsService();
        applicationArgumentsService.init();
    }

    @Test
    public void shouldParseAnCorrectFormattedArgumentToTrue() {
        applicationArgumentsService.parseArguments(new String[]{"-dFTTest=true"});
        assertTrue(applicationArgumentsService.getArgumentValue("Test"));
    }

    @Test
    public void shouldParseAnCorrectFormattedArgumentToFalse() {
        applicationArgumentsService.parseArguments(new String[]{"-dFTTest=false"});
        assertFalse(applicationArgumentsService.getArgumentValue("Test"));
    }

    @Test
    public void shouldReturnFalseWhenKeyDoNotExist() {
        applicationArgumentsService.parseArguments(new String[]{"-dFTTest=false"});
        assertFalse(applicationArgumentsService.getArgumentValue("Test2"));
    }

    @Test
    public void shcouldReturnNullWhenNoArgumentParsed() {
        assertFalse(applicationArgumentsService.getArgumentValue("Test"));
    }

    @Test
    public void shouldNotParseAnNotCorrectFormattedArgument() {
        applicationArgumentsService.parseArguments(new String[]{"-dNoFTTest=false"});
        assertFalse(applicationArgumentsService.getArgumentValue("Test"));
    }

}