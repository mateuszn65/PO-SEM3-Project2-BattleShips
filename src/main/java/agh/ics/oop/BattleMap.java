package agh.ics.oop;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;

public class BattleMap {
    protected int width;
    protected int height;
    protected int shipsLeft;
//    protected int moves;
//    protected LinkedList<Ship> shipsList = new LinkedList<>();
    protected HashMap<Vector2d, Cell> hashMap = new HashMap<>();

    public BattleMap(int width, int height){
        this.width = width;
        this.height = height;
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
            if (isInBound(pos))
                result.add(pos);
        }
        return result.toArray(new Vector2d[0]);
    }

    public boolean isInBound(Vector2d position){
        return position.x >= 0 && position.x < this.width && position.y >= 0 && position.x < this.height;
    }
    public boolean isValidPoint(Vector2d position){
        if (!isInBound(position))
            return false;
        Vector2d[] neighbors = getNeighbors(position);
        for (Vector2d pos : neighbors) {
            if (this.hashMap.containsKey(pos)){
                if (this.hashMap.get(pos).cellStatus != CellStatus.EMPTYHIT){
                    return false;
                }
            }
        }
        return true;
    }

    public boolean canPlaceShip(){



        return true;
    }

    public void placeShip(Ship ship, Vector2d[] position){
        //if canPlaceShip if calling from outside
        for (Vector2d pos: position){
            Cell cell = new Cell(pos, CellStatus.AFLOAT, ship);
            this.hashMap.put(pos, cell);
        }
    }
    private Vector2d[] getRandomPositionToPlaceShip(int length){
        Random generator = new Random();
        int toManyTimes = this.width*this.height*4;

        do {
            LinkedList<Vector2d> candidates = new LinkedList<>();
            Vector2d randpos = new Vector2d(generator.nextInt(this.width), generator.nextInt(this.height));
            if (isValidPoint(randpos)){
                candidates.add(randpos);
                int randDirection = generator.nextInt(2);
                //horizontal
                if (randDirection == 1){
                    int randCandidateDirection = generator.nextInt(2);
                    //right
                    if (randCandidateDirection == 1){
                        int i = 1;
                        while (candidates.size() < length){
                            Vector2d candidate = randpos.add(new Vector2d(i, 0));
                            if (!isValidPoint(candidate)) break;
                            candidates.add(candidate);
                            i++;
                        }
                        i = 1;
                        while (candidates.size() < length){
                            Vector2d candidate = randpos.subtract(new Vector2d(i, 0));
                            if (!isValidPoint(candidate)) break;
                            candidates.add(candidate);
                            i++;
                        }
                    }
                    //left
                    else {
                        int i = 1;
                        while (candidates.size() < length){
                            Vector2d candidate = randpos.subtract(new Vector2d(i, 0));
                            if (!isValidPoint(candidate)) break;
                            candidates.add(candidate);
                            i++;
                        }
                        i = 1;
                        while (candidates.size() < length){
                            Vector2d candidate = randpos.add(new Vector2d(i, 0));
                            if (!isValidPoint(candidate)) break;
                            candidates.add(candidate);
                            i++;
                        }
                    }
                }
                //vertical
                else {
                    int randCandidateDirection = generator.nextInt(2);
                    //up
                    if (randCandidateDirection == 1){
                        int i = 1;
                        while (candidates.size() < length){
                            Vector2d candidate = randpos.add(new Vector2d(0, i));
                            if (!isValidPoint(candidate)) break;
                            candidates.add(candidate);
                            i++;
                        }
                        i = 1;
                        while (candidates.size() < length){
                            Vector2d candidate = randpos.subtract(new Vector2d(0, i));
                            if (!isValidPoint(candidate)) break;
                            candidates.add(candidate);
                            i++;
                        }
                    }
                    //left
                    else {
                        int i = 1;
                        while (candidates.size() < length){
                            Vector2d candidate = randpos.subtract(new Vector2d(0, i));
                            if (!isValidPoint(candidate)) break;
                            candidates.add(candidate);
                            i++;
                        }
                        i = 1;
                        while (candidates.size() < length){
                            Vector2d candidate = randpos.add(new Vector2d(0, i));
                            if (!isValidPoint(candidate)) break;
                            candidates.add(candidate);
                            i++;
                        }
                    }
                }
            }
            if (candidates.size() == length)
                return candidates.toArray(new Vector2d[0]);
            toManyTimes--;
        }while (toManyTimes > 0);

        return null;
    }


    private boolean randomlyPlaceShip(int length){
        Vector2d[] randomPos = getRandomPositionToPlaceShip(length);
        if (randomPos == null) return false;
        Ship ship = new Ship(length, randomPos);
        placeShip(ship, randomPos);
        return true;
    }
    public boolean randomlyPlaceShips(int noCuirassiers, int noCruisers, int noDestroyers){
        this.shipsLeft = noCuirassiers + noCruisers + noDestroyers;
        for (int i = 0; i < noCuirassiers; i++){
            if (!randomlyPlaceShip(5))
                return false;
        }
        for (int i = 0; i < noCruisers; i++){
            if (!randomlyPlaceShip(4))
                return false;
        }
        for (int i = 0; i < noDestroyers; i++){
            if (!randomlyPlaceShip(2))
                return false;
        }
        return true;
    }
}
