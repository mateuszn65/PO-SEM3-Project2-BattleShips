package agh.ics.oop.gui;

import agh.ics.oop.Battle;
import agh.ics.oop.CellStatus;
import agh.ics.oop.Ship;
import agh.ics.oop.Vector2d;
import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import java.util.LinkedList;


public class App extends Application {
    //INPUT
    private int mapWidth = 10;
    private int mapHeight = 10;
    private int moves = 30;
    private boolean hardMode = false;
    private int noCuirassiers = 1;
    private int noCruisers = 2;
    private int noDestroyers = 3;

    //PLACEMENT OF SHIPS
    private Ship primaryShip = null;
    private Ship tmpShip = null;
    private Vector2d primaryPos;
    private Vector2d[] primaryPosition;
    private boolean placementFaze = true;
    private boolean rotateShip = false;

    //DISPLAY
    private Stage window;
    private final GridPane playerGrid = new GridPane();
    private final GridPane computerGrid = new GridPane();

    //OTHER
    private final int cellSize = 30;
    private Battle battle;




    //SCENES
    private void mapParametersScene(){
        Label widthLabel = new Label("Map Width");
        TextField mapWidthInput = new TextField("" + this.mapWidth);
        mapWidthInput.setAlignment(Pos.CENTER);
        HBox form1 = new HBox(widthLabel, mapWidthInput);
        form1.setSpacing(60);
        form1.setAlignment(Pos.CENTER);

        Label heightLabel = new Label("Map Height");
        TextField mapHeightInput = new TextField("" + this.mapHeight);
        mapHeightInput.setAlignment(Pos.CENTER);
        HBox form2 = new HBox(heightLabel, mapHeightInput);
        form2.setSpacing(60);
        form2.setAlignment(Pos.CENTER);

        Label movesLabel = new Label("Number of moves");
        TextField movesInput = new TextField("" + this.moves);
        movesInput.setAlignment(Pos.CENTER);
        HBox form3 = new HBox(movesLabel, movesInput);
        form3.setSpacing(28);
        form3.setAlignment(Pos.CENTER);

        CheckBox checkBox = new CheckBox("Hard Mode");

        Button button = new Button("Confirm");
        button.setOnAction(e->{
            try{
                this.mapWidth = Integer.parseInt(mapWidthInput.getText());
                this.mapHeight = Integer.parseInt(mapHeightInput.getText());
                this.moves = Integer.parseInt(movesInput.getText());
                this.hardMode = checkBox.isSelected();
                if (
                    this.mapWidth <= 0 || this.mapWidth > 40 ||
                    this.mapHeight <= 0 || this.mapHeight > 40 ||
                    this.moves <= 0 || this.moves > this.mapHeight * this.mapWidth
                )
                    throw new IllegalArgumentException("Incorrect input, try more appropriate range of input");

                chooseTypesOfShipsScene();

            }catch (NumberFormatException ex){
                AlertBox.display("Wrong Input", "Can't convert input into Integer");
            }catch (IllegalArgumentException ex){
                AlertBox.display("Incorrect Input", ex.getMessage());
            }
        });
        VBox vBox = new VBox(20);
        vBox.getChildren().addAll(form1, form2, form3, checkBox, button);
        vBox.setStyle("-fx-padding: 20;");
        vBox.setAlignment(Pos.TOP_CENTER);
        Scene scene = new Scene(vBox, 500, 300);
        this.window.setScene(scene);

    }
    private void chooseTypesOfShipsScene(){
        Label cuirassierLabel = new Label("Number of cuirassiers(5)");
        TextField cuirassierInput = new TextField("" + 1);
        cuirassierInput.setAlignment(Pos.CENTER);
        HBox form1 = new HBox(cuirassierLabel, cuirassierInput);
        form1.setSpacing(20);
        form1.setAlignment(Pos.CENTER);

        Label cruiserLabel = new Label("Number of Cruisers(4)");
        TextField cruiserInput = new TextField("" + 2);
        cruiserInput.setAlignment(Pos.CENTER);
        HBox form2 = new HBox(cruiserLabel, cruiserInput);
        form2.setSpacing(30);
        form2.setAlignment(Pos.CENTER);

        Label destroyerLabel = new Label("Number of Destroyer(2)");
        TextField destroyerInput = new TextField("" + 3);
        destroyerInput.setAlignment(Pos.CENTER);
        HBox form3 = new HBox(destroyerLabel, destroyerInput);
        form3.setSpacing(20);
        form3.setAlignment(Pos.CENTER);

        Button button = new Button("Confirm");
        button.setOnAction(e->{
            try{
                this.noCuirassiers = Integer.parseInt(cuirassierInput.getText());
                this.noCruisers = Integer.parseInt(cruiserInput.getText());
                this.noDestroyers = Integer.parseInt(destroyerInput.getText());
                if (
                        this.noCuirassiers < 0 ||
                        this.noCruisers < 0 ||
                        this.noDestroyers < 0
                )
                    throw new IllegalArgumentException("Incorrect input, try more appropriate range of input");
                if ((this.noCuirassiers * 5 + this.noCruisers * 4 + this.noDestroyers * 2) * 3 > this.mapWidth * this.mapHeight)
                    throw new IllegalArgumentException("Incorrect input, to many ships");
                this.battle = new Battle(this.mapWidth, this.mapHeight, this.moves, this.hardMode);

                if (!this.battle.setUpMaps(this.noCuirassiers, this.noCruisers, this.noDestroyers))
                    throw new RuntimeException("Couldn't place the ships");



                choosePlacementOfShips();

            }catch (NumberFormatException ex){
                AlertBox.display("Wrong Input", "Can't convert input into Integer");
            }catch (IllegalArgumentException ex){
                AlertBox.display("Incorrect Input", ex.getMessage());
            }catch (RuntimeException ex){
                AlertBox.display("Runtime Exception", ex.getMessage());
            }
        });
        Button back = new Button("Back");
        back.setOnAction(e-> {mapParametersScene();});
        HBox buttonBox = new HBox(back, button);
        buttonBox.setSpacing(20);
        buttonBox.setAlignment(Pos.CENTER);

        VBox vBox = new VBox(20);
        vBox.getChildren().addAll(form1, form2, form3, buttonBox);
        vBox.setStyle("-fx-padding: 20;");
        vBox.setAlignment(Pos.TOP_CENTER);
        Scene scene = new Scene(vBox, 500, 300);
        this.window.setScene(scene);

    }
    private void choosePlacementOfShips(){
        _updatePlayerGrid();
        Button startButton = new Button("Start");
        startButton.setOnAction(e->{battleScene();});
        Button backButton = new Button("Back");
        backButton.setOnAction(e->{
            clearPlayerGrid();
            chooseTypesOfShipsScene();});
        HBox buttonBox = new HBox(backButton, startButton);
        buttonBox.setSpacing(20);
        buttonBox.setAlignment(Pos.CENTER);

        VBox vBox = new VBox(20);
        vBox.getChildren().addAll(this.playerGrid, buttonBox);
        vBox.setStyle("-fx-padding: 20;");
        vBox.setAlignment(Pos.TOP_CENTER);
        Scene scene = new Scene(vBox, this.mapWidth  * this.cellSize + 100, this.mapHeight * this.cellSize + 150);
        this.window.setScene(scene);
    }
    private void battleScene(){
        this.placementFaze = false;
        HBox hbox = new HBox(50);
        updateGrid(true);
        _updateComputerGrid();
        hbox.getChildren().addAll(this.playerGrid, this.computerGrid);
        hbox.setStyle("-fx-padding: 20;");
        hbox.setAlignment(Pos.TOP_CENTER);
        Scene scene = new Scene(hbox, 2*(this.mapWidth  * this.cellSize + 100), (this.mapHeight * this.cellSize + 100));
        this.window.setScene(scene);
        //this.window.setMaximized(true);
    }
    private void scoreScene(){
        int playerScore = this.battle.getPlayerScore();
        int computerScore = this.battle.getComputerScore();
        Label winner = new Label();
        if (playerScore > computerScore){
            winner.setText("Player won!");
        }else if (playerScore == computerScore){
            winner.setText("Draw!");
        }else {
            winner.setText("Computer won!");
        }
        winner.setStyle("-fx-font-size: 20");
        Label playerScoreLabel = new Label("Player score: " + playerScore);
        Label computerScoreLabel = new Label("Computer score: " + computerScore);

        VBox vBox = new VBox(20);
        vBox.getChildren().addAll(winner, playerScoreLabel, computerScoreLabel);
        vBox.setStyle("-fx-padding: 20;");
        vBox.setAlignment(Pos.TOP_CENTER);
        Scene scene = new Scene(vBox, 500, 300);
        this.window.setScene(scene);

    }



    //HANDLES UPDATING GRIDS
    private void updateGrid(boolean player){
        if (player){
            clearPlayerGrid();
            _updatePlayerGrid();
            return;
        }
        this.computerGrid.getChildren().clear();
        this.computerGrid.getColumnConstraints().clear();
        this.computerGrid.getRowConstraints().clear();
        _updateComputerGrid();
    }

    //HELPER FUNCTION FOR CLEARING PLAYER GRID
    private void clearPlayerGrid(){
        this.playerGrid.getChildren().clear();
        this.playerGrid.getColumnConstraints().clear();
        this.playerGrid.getRowConstraints().clear();
    }

    //HELPER FUNCTION FOR UPDATING COMPUTER GRID
    private void _updateComputerGrid(){
        Label label = new Label("y\\x");
        this.computerGrid.add(label, 0, 0);
        this.computerGrid.getColumnConstraints().add(new ColumnConstraints(this.cellSize));
        this.computerGrid.getRowConstraints().add(new RowConstraints(this.cellSize));
        GridPane.setHalignment(label, HPos.CENTER);

        Vector2d ur = new Vector2d(this.mapWidth, this.mapHeight);
        //col
        for (int i = 0; i < ur.x; i++){
            label = new Label("" + (i));
            this.computerGrid.add(label, i+1, 0);
            this.computerGrid.getColumnConstraints().add(new ColumnConstraints(this.cellSize));
            GridPane.setHalignment(label, HPos.CENTER);
        }
        //row
        for (int i = 0; i < ur.y; i++){
            label = new Label("" + (i));
            this.computerGrid.add(label, 0, i+1);
            this.computerGrid.getRowConstraints().add(new RowConstraints(this.cellSize));
            GridPane.setHalignment(label, HPos.CENTER);
        }
        //el
        for(int i = 0; i < ur.x; i++){
            for(int j = 0; j < ur.y; j++){
                int k = i, l = j;
                Color color = CellStatus.getComputerCellColor(this.battle.getCellStatus(false, new Vector2d(i, j)));

                setCell(false, color, new Vector2d(k, l));
            }
        }
    }

    //HELPER FUNCTION FOR UPDATING PLAYER GRID
    private void _updatePlayerGrid(){
        Label label = new Label("y\\x");
        this.playerGrid.add(label, 0, 0);
        this.playerGrid.getColumnConstraints().add(new ColumnConstraints(this.cellSize));
        this.playerGrid.getRowConstraints().add(new RowConstraints(this.cellSize));
        GridPane.setHalignment(label, HPos.CENTER);

        Vector2d ur = new Vector2d(this.mapWidth, this.mapHeight);
        //col
        for (int i = 0; i < ur.x; i++){
            label = new Label("" + (i));
            this.playerGrid.add(label, i+1, 0);
            this.playerGrid.getColumnConstraints().add(new ColumnConstraints(this.cellSize));
            GridPane.setHalignment(label, HPos.CENTER);
        }
        //row
        for (int i = 0; i < ur.y; i++){
            label = new Label("" + (i));
            this.playerGrid.add(label, 0, i+1);
            this.playerGrid.getRowConstraints().add(new RowConstraints(this.cellSize));
            GridPane.setHalignment(label, HPos.CENTER);
        }
        boolean canPlace = false;
        if (this.placementFaze && this.tmpShip != null){
             if (!this.battle.getPlayerMap().canPlaceShip(this.tmpShip)){
                 canPlace = false;
                 this.battle.getPlayerMap().addToMap(this.primaryShip);
             }else
                 canPlace = true;
        }
        //el
        for(int i = 0; i < ur.x; i++){
            for(int j = 0; j < ur.y; j++){
                Color color = CellStatus.getPlayerCellColor(this.battle.getCellStatus(true, new Vector2d(i, j)));
                if (this.placementFaze && this.tmpShip != null){
                    for (Vector2d pos: this.tmpShip.getPosition()){
                        if (pos.equals(new Vector2d(i, j))){
                            if (canPlace)
                                color = Color.GREEN;
                            else
                                color = Color.ORANGERED;
                        }
                    }
                }
                setCell(true, color, new Vector2d(i, j));
            }
        }
    }

    //HANDLES CELL FUNCTIONS
    private void setCell(boolean player ,Color color, Vector2d pos){
        Pane cellPane = new Pane();
        cellPane.setBackground(new Background(new BackgroundFill(color, CornerRadii.EMPTY, Insets.EMPTY)));
        cellPane.setBorder(new Border(new BorderStroke(Color.BLACK,
                BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        if (player){
            this.playerGrid.add(cellPane, pos.x+1, pos.y+1);

            if (this.placementFaze){
                cellPane.setOnDragDetected(e->{
                    if (this.battle.getCellStatus(true, pos) == CellStatus.AFLOAT){
                        cellPane.startFullDrag();
                        this.primaryShip = this.battle.getShip(pos);
                        this.primaryPos = pos;
                        this.primaryPosition = this.primaryShip.getPosition();
                        this.rotateShip = e.isSecondaryButtonDown();
                    }
                });
                cellPane.setOnMouseDragEntered(e->{
                    this.battle.getPlayerMap().removeFromMap(this.primaryShip.getPosition());
                    if (this.tmpShip == null)
                        this.tmpShip = new Ship(this.primaryShip.getHealth(), this.primaryShip.getPosition());
                    LinkedList<Vector2d> newPosition = new LinkedList<>();
                    for (Vector2d oldPos: this.primaryPosition){
                        if (this.rotateShip){
                            int r;
                            if (oldPos.x == this.primaryPos.x)
                                r = oldPos.y - this.primaryPos.y;
                            else
                                r = oldPos.x - this.primaryPos.x;
                            newPosition.add(oldPos.add(pos.subtract(this.primaryPos)).add(new Vector2d(-r, -r)));
                        }else
                            newPosition.add(oldPos.add(pos.subtract(this.primaryPos)));
                    }
                    this.tmpShip.setPosition(newPosition.toArray(new Vector2d[0]));
                    updateGrid(true);
                });
                cellPane.setOnMouseMoved(e->{
                    if (!e.isPrimaryButtonDown()){
                        if (this.tmpShip != null && this.battle.getPlayerMap().canPlaceShip(this.tmpShip)){
                            this.battle.getPlayerMap().addToMap(this.tmpShip);
                            this.tmpShip = null;
                        }
                    }
                });
            }
        }else {
            if (
                    this.battle.getCellStatus(false, pos) == CellStatus.EMPTY ||
                            this.battle.getCellStatus(false, pos) == CellStatus.AFLOAT){
                cellPane.setOnMouseClicked(e->{turns(pos);});
            }
            this.computerGrid.add(cellPane, pos.x+1, pos.y+1);
        }
    }

    //SWITCHING BETWEEN TURNS
    private void turns(Vector2d pos){
        if(this.battle.playerTurn(pos))
            scoreScene();
        updateGrid(false);
        if (this.battle.computerTurn())
            scoreScene();
        updateGrid(true);
    }



    @Override
    public void start(Stage primaryStage) throws Exception {
        this.window = primaryStage;
        this.window.setTitle("BattleShips");
        mapParametersScene();

        this.window.show();
    }
}
