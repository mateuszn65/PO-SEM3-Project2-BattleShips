package agh.ics.oop;

import javafx.scene.paint.Color;

public enum CellStatus {
    EMPTY,
    EMPTYHIT,
    SHIPHIT,
    SUNK,
    AFLOAT;
    //RETURN COLOR CELL COLOR FOR PLAYER MAP
    public static Color getPlayerCellColor(CellStatus cellStatus){
        return switch (cellStatus){
            case EMPTY -> Color.ALICEBLUE;
            case AFLOAT -> Color.GREEN;
            case SUNK -> Color.RED;
            case EMPTYHIT -> Color.GRAY;
            case SHIPHIT -> Color.ORANGE;
        };
    }
    //RETURN COLOR CELL COLOR FOR COMPUTER MAP
    public static Color getComputerCellColor(CellStatus cellStatus){
        return switch (cellStatus){
            case EMPTY, AFLOAT -> Color.ALICEBLUE;
            case SUNK -> Color.RED;
            case EMPTYHIT -> Color.GRAY;
            case SHIPHIT -> Color.ORANGE;
        };
    }
}
