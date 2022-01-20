package agh.ics.oop;

import java.util.LinkedList;
import java.util.Random;

public class Battle {
    protected BattleMap computerMap;
    protected BattleMap playerMap;
    protected int computerScore;
    protected int playerScore;
    protected int width;
    protected int height;
    protected int moves;
    protected boolean hardMode;
    protected LinkedList<Vector2d> foundShipCoordinates = new LinkedList<>();
    protected int direction = -1;

    //CONSTRUCTOR
    public Battle(int width, int height, int moves, boolean hardMode){
        this.computerMap = new BattleMap(width, height);
        this.playerMap = new BattleMap(width, height);
        this.width = width;
        this.height = height;
        this.moves = moves;
        this.hardMode = hardMode;
    }

    //GETTERS
    public CellStatus getCellStatus(boolean playerMap, Vector2d pos){
        if (playerMap){
            if (!this.playerMap.hashMap.containsKey(pos))
                return CellStatus.EMPTY;
            return this.playerMap.hashMap.get(pos).cellStatus;
        }
        if (!this.computerMap.hashMap.containsKey(pos))
            return CellStatus.EMPTY;
        return this.computerMap.hashMap.get(pos).cellStatus;
    }
    public Ship getShip(Vector2d pos){
        return this.playerMap.hashMap.get(pos).ship;
    }
    public BattleMap getPlayerMap(){
        return this.playerMap;
    }
    public int getComputerScore(){
        return this.computerScore;
    }
    public int getPlayerScore(){
        return this.playerScore;
    }

    //RANDOMLY PLACE SHIPS ON BOTH MAPS
    public boolean setUpMaps(int noCuirassiers, int noCruisers, int noDestroyers){
        if (!this.computerMap.randomlyPlaceShips(noCuirassiers, noCruisers, noDestroyers)) return false;
        return this.playerMap.randomlyPlaceShips(noCuirassiers, noCruisers, noDestroyers);
    }

    //HANDLES PLAYER TURN
    public boolean playerTurn(Vector2d position){
        //missed
        if (!this.computerMap.hashMap.containsKey(position)){
            this.computerMap.hashMap.put(position, new Cell(position, CellStatus.EMPTYHIT));
            return false;
        }

        //ship hit
        if (this.computerMap.hashMap.get(position).cellStatus == CellStatus.AFLOAT){
            Cell cell = this.computerMap.hashMap.get(position);
            playerScore += cell.ship.hit();
            if (cell.ship.isSunk()){
                for (Vector2d pos: cell.ship.position){
                    this.computerMap.hashMap.get(pos).cellStatus = CellStatus.SUNK;
                }
                this.computerMap.shipsLeft--;
                if (this.computerMap.shipsLeft == 0)
                    return true;
            }else {
                cell.cellStatus = CellStatus.SHIPHIT;
            }
        }
        return false;
    }



    //AI
    public boolean computerTurn() {
        Random generator = new Random();
        //ship has been hit in the past
        if (this.foundShipCoordinates.size() > 0){
            Vector2d[] candidates = {};
            if (this.foundShipCoordinates.size() == 1){
                candidates = this.getNeighbors(this.foundShipCoordinates.getFirst());
                this.direction = -1;
            }
            if (this.direction == -1)
                this.direction = generator.nextInt(2);
            switch (this.direction){
                case 1 -> {
                    candidates = this.getHorizontalNeighbors();
                    if (candidates.length == 0){
                        candidates = this.getVerticalNeighbors();
                        this.direction = 0;
                    }
                }
                case 0 -> {
                    candidates = this.getVerticalNeighbors();
                    if (candidates.length == 0){
                        candidates = this.getHorizontalNeighbors();
                        this.direction = 1;
                    }
                }
            }


            int randIndex = generator.nextInt(candidates.length);
            Vector2d randpos = candidates[randIndex];

            //missed
            if (!this.playerMap.hashMap.containsKey(randpos)) {
                this.playerMap.hashMap.put(randpos, new Cell(randpos, CellStatus.EMPTYHIT));
            }//hit
            else if (this.playerMap.hashMap.get(randpos).cellStatus == CellStatus.AFLOAT){
                Cell cell = this.playerMap.hashMap.get(randpos);
                computerScore += cell.ship.hit();
                this.foundShipCoordinates.add(randpos);
                //entire ship sunk
                if (cell.ship.isSunk()){
                    this.foundShipCoordinates.clear();

                    for (Vector2d pos: cell.ship.position){
                        this.playerMap.hashMap.get(pos).cellStatus = CellStatus.SUNK;
                    }
                    this.playerMap.shipsLeft--;
                    if (this.playerMap.shipsLeft == 0)
                        return true;
                }else {
                    cell.cellStatus = CellStatus.SHIPHIT;
                }
            }
        //still searching
        }else {

            Vector2d[] candidates = getCandidatesToShot();
            int randIndex = generator.nextInt(candidates.length);
            Vector2d randpos = candidates[randIndex];

            //missed
            if (!this.playerMap.hashMap.containsKey(randpos)) {
                this.playerMap.hashMap.put(randpos, new Cell(randpos, CellStatus.EMPTYHIT));
            }
            //next ship has been hit for the first time
            else if (this.playerMap.hashMap.get(randpos).cellStatus == CellStatus.AFLOAT) {
                Cell cell = this.playerMap.hashMap.get(randpos);
                computerScore += cell.ship.hit();
                cell.cellStatus = CellStatus.SHIPHIT;

                this.foundShipCoordinates.add(randpos);
            }
        }
        this.moves--;
        return this.moves == 0;

    }

    private Vector2d[] getCandidatesToShot(){
        LinkedList<Vector2d> result = new LinkedList<>();
        for (int i = 0; i < this.width; i ++){
            for (int j = 0; j < this.height; j++){
                Vector2d pos = new Vector2d(i, j);
                if (this.hardMode && (i+j)%2 == 0)
                    continue;
                if (!this.playerMap.hashMap.containsKey(pos)){
                    result.add(pos);
                }else if (this.playerMap.hashMap.get(pos).cellStatus == CellStatus.AFLOAT){
                    result.add(pos);
                }
            }
        }
        return result.toArray(new Vector2d[0]);
    }
    private boolean isValidCandidate(Vector2d pos){
        return this.playerMap.isInBound(pos) && (!this.playerMap.hashMap.containsKey(pos) || this.playerMap.hashMap.get(pos).cellStatus == CellStatus.AFLOAT);
    }
    private Vector2d[] getNeighbors(Vector2d position) {
        Vector2d[] positions = new Vector2d[] {
                position.add(new Vector2d(-1, 0)),
                position.add(new Vector2d(1, 0)),
                position.add(new Vector2d(0, -1)),
                position.add(new Vector2d(0, 1))
        };
        LinkedList<Vector2d> result = new LinkedList<>();

        for (Vector2d pos: positions){
            if (isValidCandidate(pos))
                result.add(pos);
        }
        return result.toArray(new Vector2d[0]);
    }
    private Vector2d[] getHorizontalNeighbors(){
        LinkedList<Vector2d> result = new LinkedList<>();
        for (Vector2d pos: this.foundShipCoordinates){
            Vector2d possibleCandidate = pos.add(new Vector2d(-1, 0));
            if (isValidCandidate(possibleCandidate))
                result.add(possibleCandidate);
            possibleCandidate = pos.add(new Vector2d(1, 0));
            if (isValidCandidate(possibleCandidate))
                result.add(possibleCandidate);
        }
        return result.toArray(new Vector2d[0]);
    }
    private Vector2d[] getVerticalNeighbors(){
        LinkedList<Vector2d> result = new LinkedList<>();
        for (Vector2d pos: this.foundShipCoordinates){
            Vector2d possibleCandidate = pos.add(new Vector2d(0, -1));
            if (isValidCandidate(possibleCandidate))
                result.add(possibleCandidate);
            possibleCandidate = pos.add(new Vector2d(0, 1));
            if (isValidCandidate(possibleCandidate))
                result.add(possibleCandidate);
        }
        return result.toArray(new Vector2d[0]);
    }
}
