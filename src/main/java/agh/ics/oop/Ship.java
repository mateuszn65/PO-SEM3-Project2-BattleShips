package agh.ics.oop;


public class Ship {
    protected int health;
    protected int points;
    protected Vector2d[] position;

    //CONSTRUCTOR
    public Ship(int health, Vector2d[] position){
        this.health = health;
        this.position = position;
        setPoints();
    }

    //SETTERS
    public void setPosition(Vector2d[] position){
        this.position = position;
    }
    private void setPoints(){
        this.points = switch (this.health){
            case 5 -> 10;
            case 4 -> 5;
            default -> 2;
        };
    }

    //GETTERS
    public int getHealth() {
        return this.health;
    }
    public Vector2d[] getPosition() {
        return this.position;
    }

    //OTHER
    public boolean isSunk(){
        return this.health == 0;
    }
    public int hit(){
        if (this.health > 0){
            this.health--;
            return this.points;
        }
        System.out.println("test");
        return 0;
    }
}
