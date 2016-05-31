package de.uni_bremen.factroytrouble.spring;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class PostConstructTaskSchedulerTest {

    @Mock
    private PostConstructTask postConstructTask;
    private PostConstructTaskScheduler postConstructTaskScheduler;

    @Before
    public void setUp() {
        postConstructTaskScheduler = new PostConstructTaskScheduler();
    }

    @Test
    public void shouldExecuteAllAddedTasks() {
        postConstructTaskScheduler.add(postConstructTask);
        postConstructTaskScheduler.execute();
        verify(postConstructTask).postConstructTask();
    }

}