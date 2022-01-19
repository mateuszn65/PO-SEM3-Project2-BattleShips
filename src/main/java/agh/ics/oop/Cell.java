package agh.ics.oop;

public class Cell {
    protected Vector2d position;
    protected CellStatus cellStatus;
    protected Ship ship;
    public Cell(Vector2d position, CellStatus cellStatus, Ship ship){
        this.position = position;
        this.cellStatus = cellStatus;
        this.ship = ship;
    }
    public Cell(Vector2d position, CellStatus cellStatus){
        this.position = position;
        this.cellStatus = cellStatus;
        this.ship = null;
    }
}
