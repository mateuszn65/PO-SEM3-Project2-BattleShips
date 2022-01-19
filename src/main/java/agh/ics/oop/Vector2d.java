package agh.ics.oop;


import java.util.Objects;
public class Vector2d {
    public final int x;
    public final int y;

    //CONSTRUCTOR
    public Vector2d(int x, int y){
        this.x = x;
        this.y = y;
    }



    @Override
    public String toString() {
        return "(" + this.x + ", " + this.y + ")";
    }

    public boolean precedes(Vector2d other){
        return this.x <= other.x && this.y <= other.y;
    }
    public boolean follows(Vector2d other){
        return this.x >= other.x && this.y >= other.y;
    }

    public Vector2d upperRight(Vector2d other){
        int rx, ry;
        rx = Math.max(this.x, other.x);
        ry = Math.max(this.y, other.y);
        return new Vector2d(rx, ry);
    }
    public Vector2d lowerLeft(Vector2d other){
        int rx, ry;
        rx = Math.min(this.x, other.x);
        ry = Math.min(this.y, other.y);
        return new Vector2d(rx, ry);
    }

    public Vector2d add(Vector2d other){
        return new Vector2d(this.x + other.x, this.y + other.y);
    }
    public Vector2d subtract(Vector2d other){
        return new Vector2d(this.x - other.x, this.y - other.y);
    }

    public Vector2d opposite(){
        return new Vector2d(-this.x, -this.y);
    }

    //FOR HASHMAP
    public int hashCode(){
        return Objects.hash(this.x, this.y);
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vector2d vector2d = (Vector2d) o;
        return x == vector2d.x && y == vector2d.y;
    }
}
