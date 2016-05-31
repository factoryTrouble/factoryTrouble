/*
FactoryTrouble
Copyright (C) 2016  Projectmembers of FacoriyTrouble

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA
 */
package de.uni_bremen.factroytrouble.ai.ki3;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.util.Map;
import java.awt.*;

import de.uni_bremen.factroytrouble.gameobjects.Robot;

/**
 * @author immanuel
 * Diese Klasse, soll das abstrakte Modell eines Weges bereitstellen, den man
 * beim Blick auf das Spielfeld als Mensch sofort sieht.
 */

public class PathFinder {
    
    private ScoreManager scoreManager;
    
    private String[][] boardState;
    private boolean[][] visited;
    private Point[][] parent;
    private int[][] distance;
    private String[][] orientations;
    
    private Map<Robot, String> robotsState;
    private Map<Integer,Point> flagState;
    
    private Point start; //Startpunkt für Wegfindung
    private Point dest; //Zielpunkt für Wegfindung
    private List<Point> path;
    

    public PathFinder(ScoreManager pScoreManager) {
        scoreManager = pScoreManager;
        boardState = scoreManager.getBoardStateAsArray();
        robotsState = scoreManager.getRobotsStateAsMap();
        flagState = scoreManager.getFlagState();
        
    }
    
    /**
     * Führt den Algorithmus aus.
     */
    public void execute(){
        init();
        Point next = new Point();
        while(!(visited[dest.x][dest.y])){
            next=findMinimalDistance();
            visited[next.x][next.y]=true;
            handleNeighbours(next);
        }
        resultingPath(dest);
    }
    
    public List<Point> getPath(){
        return path;
    }
    
   /**
    * Erstellt eine Liste mit dem Weg vom Startpunkt zu einem Zielpunkt und gibt diese zurück.
    * @param destination der Zielpunkt.
    * @return die Liste.
    */
    private List<Point> resultingPath(Point destination){
        if(destination==null){
            Collections.reverse(path);
            return path;
        }
        path.add(destination);
        return resultingPath(parent[destination.x][destination.y]);
    }
    
    private void handleNeighbours(Point father){
        for(int i=father.x-1;i<=father.x+1;i=i+2){
            Point child = new Point(i,father.y);
            if (isInBoard(child)){
                updateDistance(father,child);
            }
        }
        for(int j=father.y-1;j<=father.y+1;j=j+2){
            Point child = new Point(father.x,j);
            if (isInBoard(child)){
                updateDistance(father,child);
            }
        }
    }
    
    /**
     * Sucht aus der Map mit Robotern den eigenen heraus,
     * und setzt den Startpunkt auf die Position dieses Roboters.
     */
    private void findOwnPosition() {
        int x;
        int y;
        for (Robot robot : robotsState.keySet()) {
            String[] robots = robotsState.get(robot).split(",");
            if ("t".equals(robots[1])) {
                x = Integer.parseInt(robots[8].split(":")[0]);
                y = Integer.parseInt(robots[8].split(":")[1]);
                start = new Point(x,y);
                orientations[x][y]=robots[4];
            }
        }
    }
    
    /**
     * Sucht aus der Map mit Robotern den eigenen heraus,
     * und setzt das Ziel auf die Position der als nächstes zu besuchenden Flagge.
     */
    private void findFlagPosition() {
        Integer nextFlagNo;
        for (Robot robot : robotsState.keySet()) {
            String[] robots = robotsState.get(robot).split(",");
            if ("t".equals(robots[1])) {
                nextFlagNo = Integer.parseInt(robots[7])+1;
                dest = flagState.get(nextFlagNo);
            }
        }
    }
    
    /**
     * Findet das Tile mit dem kleinsten Abstand zum Startknoten, das noch nicht abgearbeitet wurde
     * und gibt desen Koordinaten als Punkt zurück. Bevorzugt bei gleichen Distanzen Tiles, die auf einer
     * geraden Strecke liegen.
     * @return die Koordinaten als Point.
     */
    private Point findMinimalDistance(){
        int minimum = Integer.MAX_VALUE;
        int x = 0;
        int y = 0;
        for (int i=0; i<distance.length; i++){
            for (int j=0; j<distance[0].length; j++){
                if(!(visited[i][j]) && distance[i][j]<=minimum && examineMinimum(i,j,minimum)){
                    minimum = distance[i][j];
                    x=i;
                    y=j;
                }
            }
        }
        return new Point(x,y);
    }
    
    /**
     * Untersucht, ob ein Punkt mit gegebenen Koordinaten ein besseres Minimum ist.
     * @param x die x-Koordinate
     * @param y die y-Koordinate
     * @param minimum die bisherige Minimaldistanz
     * @return true, falls das Minimum aktualisiert werden soll, false sonst.
     */
    private boolean examineMinimum(int x, int y, int minimum){
        if(distance[x][y]<minimum){
            return true;
        }
        if (parent[x][y]==null){
            return false;
        }
        else{
            Point father = parent[x][y];
            Point child = new Point(x,y);
            String direction = orientations[father.x][father.y];
            if(isNorthernNeighbour(father,child)&& "n".equals(direction)){
                return true;
            }
            if(isSouthernNeighbour(father,child)&& "s".equals(direction)){
                return true;
            }
            if(isWesternNeighbour(father,child)&& "w".equals(direction)){
                return true;
            }
            if(isEasternNeighbour(father,child)&& "e".equals(direction)){
                return true;
            }
        }
        return false;
    }
    
    /**
     * @return true, falls @param point1 und @param point2 den gleichen x-Wert haben, false sonst.
     */
    private boolean sameX(Point point1, Point point2){
        if (point1.x==point2.x){
            return true;
        }
        return false;
    }
    
    /**
     * @return true, falls @param point1 und @param point2 den gleichen y-Wert haben, false sonst.
     */
    private boolean sameY(Point point1, Point point2){
        if (point1.y==point2.y){
            return true;
        }
        return false;
    }
    
    /**
     * Gibt an, ob der zweite Punkt der nördliche Nachbar vom ersten ist.
     * @return true, wenn @param point1 der nördliche Nachbar von  @param point2 ist, false sonst.
     */
    private boolean isNorthernNeighbour(Point point1, Point point2){
        if (point1.y==point2.y-1 && sameX(point1,point2)){
            return true;
        }
        return false;
    }
    
    /**
     * Gibt an, ob der zweite Punkt der südliche Nachbar vom ersten ist.
     * @return true, wenn @param point1 der südliche Nachbar von  @param point2 ist, false sonst.
     */
    private boolean isSouthernNeighbour(Point point1, Point point2){
        if (point1.y==point2.y+1 && sameX(point1,point2)){
            return true;
        }
        return false;
    }
    
    /**
     * Gibt an, ob der zweite Punkt der westliche Nachbar vom ersten ist.
     * @return true, wenn @param point1 der westliche Nachbar von  @param point2 ist, false sonst.
     */
    private boolean isWesternNeighbour(Point point1, Point point2){
        if (point1.x==point2.x+1 && sameY(point1,point2)){
            return true;
        }
        return false;
    }
    
    /**
     * Gibt an, ob der zweite Punkt der östliche Nachbar vom ersten ist.
     * @return true, wenn @param point1 der östliche Nachbar von  @param point2 ist, false sonst.
     */
    private boolean isEasternNeighbour(Point point1, Point point2){
        if (point1.x==point2.x-1 && sameY(point1,point2)){
            return true;
        }
        return false;
    }
    
    
    /**
     * Methode, um zu entscheiden, ob von einem Ausgangsfeld der direkte Übergang zu einem Zielfeld
     * nöglich ist.
     * @param from das Ausfgangsfeld.
     * @param to das Zielfeld.
     * @return true, falls der Übergang möglich ist, false sonst.
     */
    private boolean isPassable(Point from, Point to){
        String fromState = boardState[from.x][from.y];
        String toState = boardState[to.x][to.y];
        if(fromState.contains("de") || toState.contains("de")){
            return false;
        }
        if(isSouthernNeighbour(from,to) && !(fromState.contains("ws") || toState.contains("wn"))){
            return true;
        }
        if(isNorthernNeighbour(from,to) && !(fromState.contains("wn") || toState.contains("ws"))){
            return true;
        }
        if(isWesternNeighbour(from,to) && !(fromState.contains("ww") || toState.contains("we"))){
            return true;
        }
        if(isEasternNeighbour(from,to) && !(fromState.contains("we") || toState.contains("ww"))){
            return true;
        }
        return false;
    }

    /**
     * Erstellt alle Arrays. Setzt Start- und Zielpunkt. Setzt die Distanz von Startpunkt auf 0 und
     * alle anderen auf Maximum.
     */
    private void init() {
        distance = new int[boardState.length][boardState[0].length];
        parent = new Point[boardState.length][boardState[0].length];
        visited = new boolean[boardState.length][boardState[0].length];
        orientations = new String[boardState.length][boardState[0].length];
        path = new ArrayList<Point>();
        
        for (int i=0; i<boardState.length; i++){
            for (int j=0; j<boardState[0].length; j++){
                distance[i][j]=Integer.MAX_VALUE;
            }
        }
        findOwnPosition();
        findFlagPosition();
        distance[start.x][start.y]=0;
    }
    
    /**
     * Aktualisiert die Distanz zu einem Zieltile, falls diese über das Ausgangstile erreichbar und
     * kliner, als die bisherige Distanz ist. Setzt in diesem Fall außerdem den Vorgänger
     * auf das Ausgangstile.
     * @param from das Ausgangstile
     * @param to das Zieltile
     */
    private void updateDistance(Point from, Point to){
        if (isPassable(from, to) && distance[from.x][from.y]+1<=distance[to.x][to.y] && selectBestChoice(from,to)){
            distance[to.x][to.y]=distance[from.x][from.y]+1;
            parent[to.x][to.y]=from;
            setOrientation(from,to);
        }
    }
    
    private boolean selectBestChoice(Point from, Point to){
        if(distance[from.x][from.y]+1<distance[to.x][to.y]){
            return true;
        }
        else{
            if(isNorthernNeighbour(from,to) && "n".equals(orientations[from.x][from.y])){
                return true;
            }
            if(isSouthernNeighbour(from,to) && "s".equals(orientations[from.x][from.y])){
                return true;
            }
            if(isWesternNeighbour(from,to) && "w".equals(orientations[from.x][from.y])){
                return true;
            }
            if(isEasternNeighbour(from,to) && "e".equals(orientations[from.x][from.y])){
                return true;
            }
        }
        return false;
    }
    
    /**
     * Überprüft, ob sich ein bestimmter Punkt innerhalb des Feldes befindet.
     * @param point der zu überprüfende Punkt.
     * @return true, falls der Punkt im Feld liegt, false sonst.
     */
    private boolean isInBoard(Point point){
        if(point.x>=0 && point.x<boardState.length && point.y>=0 && point.y<boardState[0].length){
            return true;
        }
        return false;
    }
    
    /**
     * Setzt die Orientation eines Punktes abhängig davon, wo sich sein Elternpunkt befindet.
     * @param father der Elternpunkt
     * @param child der Kindpunkt
     */
    private void setOrientation(Point father,Point child){
        if(isNorthernNeighbour(father,child)){
            orientations[child.x][child.y]="n";
        }
        if(isWesternNeighbour(father,child)){
            orientations[child.x][child.y]="w";
        }
        if(isSouthernNeighbour(father,child)){
            orientations[child.x][child.y]="s";
        }
        if(isEasternNeighbour(father,child)){
            orientations[child.x][child.y]="w";
        }
    }

}
