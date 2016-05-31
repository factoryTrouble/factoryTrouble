/*
FactoryTrouble
Copyright (C) 2016  Projectmembers of FacoriyTrouble

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA
 */
package de.uni_bremen.factroytrouble.gui.controller.components;

import de.uni_bremen.factroytrouble.gui.generator.board.BoardConverterService;
import de.uni_bremen.factroytrouble.spring.SpringConfigHolder;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.awt.Point;

public class RespawnImageView  extends ImageView {
    public static final int ROBOT_SIZE = 30;
    private double zoom = 0.0;
    private BoardConverterService boardConverterService;
    private Point position;


    public RespawnImageView(int robotNumber) {
        super(new Image("/game/robots/side_" + robotNumber + ".png"));
        boardConverterService = SpringConfigHolder.getInstance().getContext().getBean(BoardConverterService.class);
        setPreserveRatio(true);
        setFitWidth(ROBOT_SIZE);
    }


    public void setZoom(double zoom) {
        this.zoom = zoom;
        setFitWidth(ROBOT_SIZE * zoom);
    }


    public void setPos(Point point, int respawnsOnPos) {
        position = point;
        setX((zoom * RobotImageView.ROBOT_SIZE) * point.x + (ROBOT_SIZE * zoom) * respawnsOnPos);
        setY((zoom * RobotImageView.ROBOT_SIZE) * (boardConverterService.getMaxY()- point.y));
    }
    
    public Point getPos(){
        return position;
    }
}
