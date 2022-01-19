package agh.ics.oop.gui;

import agh.ics.oop.Battle;
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
import java.util.concurrent.TimeoutException;

public class App extends Application {
    private Stage window;
    private int mapWidth = 10;
    private int mapHeight = 10;
    private int moves = 30;
    private int noCuirassiers = 1;
    private int noCruisers = 2;
    private int noDestroyers = 3;
    private int cellSize = 30;
    private boolean hardMode = false;
    private Battle battle;
    private GridPane playerGrid = new GridPane();
    private GridPane computerGrid = new GridPane();





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
                if ((this.noCuirassiers * 7 + this.noCruisers * 6 + this.noDestroyers * 4) * 3 > this.mapWidth * this.mapHeight)
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
        //player board to replace ships


        Button startButton = new Button("Start");
        startButton.setOnAction(e->{battleScene();});
        Button backButton = new Button("Back");
        backButton.setOnAction(e->{chooseTypesOfShipsScene();});
        HBox buttonBox = new HBox(backButton, startButton);
        buttonBox.setSpacing(20);
        buttonBox.setAlignment(Pos.CENTER);

        VBox vBox = new VBox(20);
        vBox.getChildren().addAll(buttonBox);
        vBox.setStyle("-fx-padding: 20;");
        vBox.setAlignment(Pos.TOP_CENTER);
        Scene scene = new Scene(vBox, 500, 300);
        this.window.setScene(scene);
    }

    private void battleScene(){
        HBox hbox = new HBox(50);
        _updatePlayerGrid();
        _updateComputerGrid();
        hbox.getChildren().addAll(this.playerGrid, this.computerGrid);
        hbox.setStyle("-fx-padding: 20;");
        hbox.setAlignment(Pos.TOP_CENTER);
        Scene scene = new Scene(hbox, 800, 600);
        this.window.setScene(scene);
        //this.window.setMaximized(true);

    }


    //HANDLES UPDATING GRID
    private void updateGrid(boolean playerGrid){
        if (playerGrid){
            this.playerGrid.getChildren().clear();
            this.playerGrid.getColumnConstraints().clear();
            this.playerGrid.getRowConstraints().clear();
            _updatePlayerGrid();
            return;
        }
        this.computerGrid.getChildren().clear();
        this.computerGrid.getColumnConstraints().clear();
        this.computerGrid.getRowConstraints().clear();
        _updateComputerGrid();

    }

    //HELPER FUNCTION FOR UPDATING GRID
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

                Pane cellPane = new Pane();
                cellPane.setOnMouseClicked(e->{turns(k, l);});
                Color color = null;
                switch (this.battle.getCell(false, new Vector2d(i, j))){
                    case EMPTY, AFLOAT -> color = Color.ALICEBLUE;
                    case SUNK -> color = Color.RED;
                    case EMPTYHIT -> color = Color.GRAY;
                    case SHIPHIT -> color = Color.ORANGE;
                }

                cellPane.setBackground(new Background(new BackgroundFill(color, CornerRadii.EMPTY, Insets.EMPTY)));
                cellPane.setBorder(new Border(new BorderStroke(Color.BLACK,
                        BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
                this.computerGrid.add(cellPane, i+1, j+1);

            }
        }
    }


    //HELPER FUNCTION FOR UPDATING GRID
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
        //el
        for(int i = 0; i < ur.x; i++){
            for(int j = 0; j < ur.y; j++){

                Pane cellPane = new Pane();
                Color color = null;
                switch (this.battle.getCell(true, new Vector2d(i, j))){
                    case EMPTY -> color = Color.ALICEBLUE;
                    case AFLOAT -> color = Color.GREEN;
                    case SUNK -> color = Color.RED;
                    case EMPTYHIT -> color = Color.GRAY;
                    case SHIPHIT -> color = Color.ORANGE;
                }
                cellPane.setBackground(new Background(new BackgroundFill(color, CornerRadii.EMPTY, Insets.EMPTY)));
                cellPane.setBorder(new Border(new BorderStroke(Color.BLACK,
                        BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
                this.playerGrid.add(cellPane, i+1, j+1);

            }
        }
    }
    private void turns(int i, int j){
        try {
            if(this.battle.playerTurn(new Vector2d(i, j)))
                scoreScene();
            updateGrid(false);
            Thread.sleep(500);
            if (this.battle.computerTurn())
                scoreScene();
            updateGrid(true);

        }catch (InterruptedException ex){
            System.out.println(ex.getMessage());
        }

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
    @Override
    public void start(Stage primaryStage) throws Exception {
        this.window = primaryStage;
        this.window.setTitle("BattleShips");
        mapParametersScene();

        this.window.show();
    }
}
