package de.uni_bremen.factroytrouble.ai.ki3;

import static org.mockito.Mockito.when;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.awt.*;
import static org.junit.Assert.assertEquals;
import de.uni_bremen.factroytrouble.gameobjects.Robot;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
/**
 * @author immanuel
 */

@RunWith(MockitoJUnitRunner.class)
public class PathFinderTest {

    @Mock
    ScoreManager manager;
    @Mock
    Robot robot1, robot2;
    
    private PathFinder pathFinder;
    
    private String[][] transpose(String[][] original) {
        String[][] transposed = new String[original[0].length][original.length];
        for (int i = 0; i < original[0].length; i++) {
            for (int j = 0; j < original.length; j++) {
                transposed[i][j]=original[j][i];
            }
        }
        return transposed;
    }
    
    @Before
    public void setUp() {
        String[][] robotStew1 = {{"ti","ti","1e","1e","1e","ti","ti_ww","1w","1w","1w","ti","ti"},
                                {"1e","1e","1s","ti","ti","ti","ti_ww","ti","ti","1s","1w","1w"},
                                {"ti","ti_ww","ti_ww","ti","ti","ti","ti","ti","ti","ti_we","ti_we","ti"},
                                {"ti","ti","ti_wn","ti","ti_ww_wn","ti","ti","ti_wn_we","ti","ti_wn","ti","ti"},
                                {"re","ti","ti_ws","ti","ti_ws","1s","1n","ti_ws","ti","ti_ws","ti","ti"},
                                {"ti","de","f3","ti","ti","1s","1e","1e","1e","1e","ti_wn_ws_ls3","1e"},
                                {"ti_ww","ti_wn","ti_ww_lw1","ti_lw1","ti_lw1","rr_lw1","rl_lw1","ti_we_lw1","ti_wn","re","ti","ti_we"},
                                {"2w","2w_ls1","2w","2w","2w","2w","2w","2w","ti_ls2","1w","1w","1w"},
                                {"ti_ww","ti_ls1","2s","ti","ti","ti","ti","ti","rr_ls2","f2","ti","ti_we"},
                                {"2e","2e_wn_ls1","2s","ti_we_ws","1n","re","ti","ti","rl_ls2","de","ti","ti"},
                                {"1w","1w","1w","1w","rl","1e_ws","1e","1e","ti_wn_ls2","ti","ti","ti"},
                                {"f1_ww","ti","ti","ti","ti","1s","de","ti","ti","ti","ti_ws_ln1","ti_we"},
                                {"1e","1e","1e","ti","ti_ww_lw1","rr_lw1","ti_we_lw1","ti","rl","1e","1e_ln1","1e"},
                                {"ti_ww","1e","1e","de","re","1s","ti","ti","1s","de","ti_wn_ln1","ti_we"},
                                {"ti","1s","ti","ti","ti","1s","ti_we","ti","1s","ti","ti","ti"},
                                {"ti","1s","ti_wn","ti","ti_wn","1s","ti","ti_wn","1s","ti_wn","ti","re"}};
        String[][] robotStew = transpose(robotStew1);
        Map<Robot,String> robotMap = new HashMap<>();
        Map<Integer,Point> flagMap = new HashMap<>();
        robotMap.put(robot1, "enemy,nil,3,10,e,nil-nil-nil-nil-nil,t,0,11:11,4:2");
        robotMap.put(robot2, "hammer,t,2,3,w,nil-nil-a1_200-b1_100-tu_30,nil,2,7:15,1:1");
        Point point1 = new Point(0,11);
        Point point2 = new Point(9,8);
        Point point3 = new Point(2,5);
        flagMap.put(1,point1);
        flagMap.put(2,point2);
        flagMap.put(3,point3);
        
        when(manager.getBoardStateAsArray()).thenReturn(robotStew);
        when(manager.getRobotsStateAsMap()).thenReturn(robotMap);
        when(manager.getFlagState()).thenReturn(flagMap);
        pathFinder = new PathFinder(manager);
    }
    
    @Test
    public void test() {
        pathFinder.execute();
        List<Point> path = pathFinder.getPath();
        List<Point> expected = new ArrayList<Point>();
        expected.add(new Point(7,15));
        expected.add(new Point(6,15));
        expected.add(new Point(5,15));
        expected.add(new Point(4,15));
        expected.add(new Point(4,14));
        expected.add(new Point(4,13));
        expected.add(new Point(4,12));
        expected.add(new Point(4,11));
        expected.add(new Point(4,10));
        expected.add(new Point(4,9));
        expected.add(new Point(4,8));
        expected.add(new Point(4,7));
        expected.add(new Point(4,6));
        expected.add(new Point(4,5));
        expected.add(new Point(3,5));
        expected.add(new Point(2,5));

        assertEquals(path, expected);
    }

}
