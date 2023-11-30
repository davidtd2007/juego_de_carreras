package Shared;

import java.awt.*;
import java.io.Serializable;

public class Kart implements Serializable
{

    private Point position;

    private int direction;

    private int speed;

    private  int lap;

    private boolean lap_bool;

    public Kart(int direction,Point position,int speed,int lap,boolean lap_bool)
    {

        this.speed= speed;
        this.direction=direction;
        this.position=position;
        this.lap = lap;
        this.lap_bool=lap_bool;
    }



    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public Point getPosition() {
        return position;
    }

    public void setPosition(Point position) {
        this.position = position;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getLap() {
        return lap;
    }

    public void setLap(int lap) {
        this.lap = lap;
    }

    public boolean getLap_bool() {
        return lap_bool;
    }

    public void setLap_bool(boolean lap_bool) {
        this.lap_bool = lap_bool;
    }
}