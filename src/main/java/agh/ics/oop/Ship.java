package agh.ics.oop;

import java.util.LinkedList;

public class Ship {
    protected int health;
    protected int points;
    protected Vector2d[] position;
    public Ship(int health, Vector2d[] position){
        this.health = health;
        this.position = position;
        setPoints();
    }
    public int hit(){
        if (this.health > 0){
            this.health--;
            return this.points;
        }
        System.out.println("test");
        return 0;
    }
    private void setPoints(){
        this.points = switch (this.health){
            case 5 -> 10;
            case 4 -> 5;
            default -> 2;
        };
    }
    public boolean isSunk(){
        return this.health == 0;
    }
}
