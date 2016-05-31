package de.uni_bremen.factroytrouble.ai.ki1.mock;

import java.awt.Point;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import de.uni_bremen.factroytrouble.board.Tile;
import de.uni_bremen.factroytrouble.gameobjects.FieldObject;
import de.uni_bremen.factroytrouble.gameobjects.Orientation;
import de.uni_bremen.factroytrouble.gameobjects.Robot;
import de.uni_bremen.factroytrouble.gameobjects.Wall;

public class MockTile implements Tile {

    private List<Wall> walls=new LinkedList<Wall>();
    private FieldObject fieldObject;
    private Point absCoordinates;
    private Robot robot;

    public MockTile(Point absCoordinates) {
        this.absCoordinates = absCoordinates;
    }

    @Override
    public void action() {
    }

    @Override
    public void setFieldObject(FieldObject object) {
        fieldObject = object;

    }

    @Override
    public void removeFieldObject() {
        fieldObject = null;

    }

    @Override
    public FieldObject getFieldObject() {
       return fieldObject;
    }

    @Override
    public void setRobot(Robot robot) {
        this.robot = robot;
    }

    @Override
    public Robot getRobot() {
        return robot;
    }

    @Override
    public List<Wall> getWalls() {
        return walls;
    }
    public void setWall(Wall wall){
        this.walls.add(wall);
    }

    @Override
    public Point getCoordinates() {
        return null;
    }
    
    @Override
    public String toString() {
        return "MockTile [coordinates=" + absCoordinates + ", robot=" + robot + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((absCoordinates == null) ? 0 : absCoordinates.hashCode());
        result = prime * result + ((robot == null) ? 0 : robot.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        MockTile other = (MockTile) obj;
        if (absCoordinates == null) {
            if (other.absCoordinates != null)
                return false;
        } else if (!absCoordinates.equals(other.absCoordinates))
            return false;
        if (robot == null) {
            if (other.robot != null)
                return false;
        } else if (!robot.equals(other.robot))
            return false;
        return true;
    }

    @Override
    public boolean hasWall(Orientation direction) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public Tile clone() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Point getAbsoluteCoordinates() {
        // TODO Auto-generated method stub
        return absCoordinates;
    }

    @Override
    public Map<Orientation, Tile> getNeighbors() {
        return null;
    }

    @Override
    public Wall getWall(Orientation direction) {
        // TODO Auto-generated method stub
        return null;
    }
}