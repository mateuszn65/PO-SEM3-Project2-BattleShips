package agh.ics.oop;

import java.util.ArrayList;
import java.util.List;

public class SimulationEngine implements Runnable{
    private final int moveDelay = 30;
    protected Battle battle;
    protected List<IGUIObserver> observers = new ArrayList<>();


    //CONSTRUCTOR
    public SimulationEngine(Battle battle){
        this.battle = battle;
    }

    //OBSERVERS
    public void addObserver(IGUIObserver observer){
        this.observers.add(observer);
    }
    public void removeObserver(IGUIObserver observer) {
        this.observers.remove(observer);
    }
    public void guiChange(){
        for (IGUIObserver observer : this.observers) {
            observer.updateGUI();
        }
    }
    //HEARTH OF THE ENGINE
    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                guiChange();
                Thread.sleep(this.moveDelay);
                this.battle.computerTurn();
                guiChange();
            } catch (InterruptedException ex) {
                break;
            }
        }
    }

}
