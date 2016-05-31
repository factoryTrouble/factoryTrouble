/*
FactoryTrouble
Copyright (C) 2016  Projectmembers of FacoriyTrouble

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA
 */
package de.uni_bremen.factroytrouble.gui.controller.components;

import de.uni_bremen.factroytrouble.gui.controller.RobotController;
import de.uni_bremen.factroytrouble.gui.generator.board.ImageBoardGenerator;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

/**
 * Enthält das Spielfeld sammt Logik
 *
 * @author Johannes und
 */

@Controller
public abstract class FieldController {

    @Autowired
    protected ImageBoardGenerator imageBoardGenerator;

    protected BoardImageView boardImageView;
    protected AnchorPane anchorPane;
    protected ScrollPane scrlPane;
    protected String tempBoard = "";
    protected int boardHeight = 0;
    protected int boardWidth = 0;
    protected DoubleProperty zoom;
    protected Map<String, RobotController> robots;
    protected Map<String, RespawnImageView> respawns;
    protected BufferedImage bufferedImage;

    /**
     * Fügt ein Spielfeld hinzu.
     * 
     * @param scrlPane
     */
    public void addGameField(ScrollPane scrlPane) {
        if (anchorPane != null)
            scrlPane.setContent(null);
        anchorPane = new AnchorPane();
        zoom = new SimpleDoubleProperty(0.2);
        addZoomListener();
        robots = new HashMap<>();
        respawns = new HashMap<>();
        this.scrlPane = scrlPane;
        scrlPane.setContent(anchorPane);
        initZoom();
    }

    /**
     * Initialisiert die Zoom-Funktionalität.
     */
    public void initZoom() {
        anchorPane.setOnScroll(event -> {
            zoom(event.getDeltaY());
            event.consume();
        });
        scrlPane.setOnScroll(event -> {
            zoom(event.getDeltaY());
            event.consume();
        });
    }

    /**
     * Initialisiert ein Spielbrett.
     * 
     * @param name
     */
    public void initBoard(String name) {
        tempBoard = name;
        anchorPane = new AnchorPane();
        anchorPane.getChildren().removeAll();
        scrlPane.setContent(anchorPane);
        bufferedImage = getImage(tempBoard);
        boardImageView = new BoardImageView(bufferedImage);
        anchorPane.getChildren().add(boardImageView);
        boardImageView.setZoom(zoom.get());

        boardHeight = bufferedImage.getHeight() / 150;
        boardWidth = bufferedImage.getWidth() / 150;
    }
    
    public abstract BufferedImage getImage(String name);
    

    public void setGameBoard(String name) {
        initBoard(name);

    }

    /**
     * Registriert einen Listener für die Zoom-Funktion.
     */
    public void addZoomListener() {
        zoom.addListener(arg0 -> {
            boardImageView.setZoom(zoom.getValue());
            robots.forEach((name, robot) -> {
                robot.getRobotImageView().setZoom(zoom.get());
                Point point = new Point(robot.getPosition());
                robot.getRobotImageView().setPos(point);

            });
            respawns.forEach((name, respawn) -> {
                respawn.setZoom(zoom.get());
                respawn.setPos(respawn.getPos(), getRespawnsOnPos(respawn, respawn.getPos()));
            });
        });
    }

    /**
     * Zoomed.
     * 
     * @param deltaY
     */
    public void zoom(double deltaY) {
        double tempZoom = zoom.get();
        if (deltaY > 0 && (tempZoom * 1.1) < 3) {
            zoom.set(tempZoom * 1.1);
        } else if (deltaY < 0 && (tempZoom / 1.1) > 0.2) {
            zoom.set(tempZoom / 1.1);
        }
    }

    public Map<String, RobotController> getRobots() {
        return robots;
    }
    
    public Map<String, RespawnImageView> getRespawns() {
        return respawns;
    }
    
    public void setRobots(Map<String, RobotController> robots) {
        this.robots = robots;
    }
    
    public void setRespawns(Map<String, RespawnImageView> respawns) {
        this.respawns = respawns;
    }
    
    public int getRespawnsOnPos(RespawnImageView respawn, Point point){
        int respawnsOnPos = 0;
        for(String name : respawns.keySet()){
            if(respawns.get(name) != respawn & respawns.get(name).getPos() == point){
                respawnsOnPos++;  
            }
        }
        return respawnsOnPos;
    }

    public BufferedImage getImage(){
        return bufferedImage;
    }
    
    public DoubleProperty getZoom(){
        return zoom;
    }
    
    public String getBoardName(){
        return tempBoard;
    }
}
