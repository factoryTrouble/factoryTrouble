package de.uni_bremen.factroytrouble.misc;

import de.uni_bremen.factroytrouble.player.GameMasterFactory;
import de.uni_bremen.factroytrouble.player.Master;

public class TestFactory extends GameMasterFactory {

    public static void setMaster(Master testMaster) {
        masterMap.put(0,testMaster);
    }
    
}
