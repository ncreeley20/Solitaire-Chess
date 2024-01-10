package puzzles.chess.gui;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import puzzles.chess.model.ChessConfig;
import puzzles.common.Coordinates;
import puzzles.common.Observer;
import puzzles.chess.model.ChessModel;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;


/**
 * Class representing a GUI for chess puzzle
 *
 * @author Nick Creeley
 */

public class ChessGUI extends Application implements Observer<ChessModel, String> {
    private ChessModel model;

    /**
     * The size of all icons, in square dimension
     */
    private final static int ICON_SIZE = 75;
    /**
     * the font size for labels and buttons
     */
    private final static int FONT_SIZE = 12;

    private Stage stage;

    private GridPane board;

    private Label gameStatus;


    /**
     * The resources directory is located directly underneath the gui package
     */
    private final static String RESOURCES_DIR = "resources/";

    // for demonstration purposes
    private Image bishop = new Image(getClass().getResourceAsStream(RESOURCES_DIR + "bishop.png"));

    private Image rook = new Image(getClass().getResourceAsStream(RESOURCES_DIR + "rook.png"));

    private Image queen = new Image(getClass().getResourceAsStream(RESOURCES_DIR + "queen.png"));

    private Image king = new Image(getClass().getResourceAsStream(RESOURCES_DIR + "king.png"));

    private Image knight = new Image(getClass().getResourceAsStream(RESOURCES_DIR + "knight.png"));

    private Image pawn = new Image(getClass().getResourceAsStream(RESOURCES_DIR + "pawn.png"));


    /**
     * a definition of light and dark and for the button backgrounds
     */
    private static final Background LIGHT =
            new Background(new BackgroundFill(Color.WHITE, null, null));
    private static final Background DARK =
            new Background(new BackgroundFill(Color.MIDNIGHTBLUE, null, null));


    @Override
    public void init() throws IOException {

        // get the file name from the command line
        String filename = getParameters().getRaw().get(0);

        this.model = new ChessModel(filename);

        this.board = new GridPane();

        this.gameStatus = new Label("Loaded: " + filename);

        gameStatus.setFont(Font.font(FONT_SIZE));

        model.addObserver(this);

    }

    /**
     * Updates the board to match the model
     * (Used in update)
     */
    public void updateBoard() {

        board.getChildren().clear();

        for (int row = 0; row < model.getMaxRow(); row++) {
            for (int col = 0; col < model.getMaxCol(); col++) {

                if (model.getPiece(row, col) == ChessConfig.empty) {

                    Button empty = new Button();

                    if ((row % 2 == col % 2)) {
                        empty.setBackground(LIGHT);
                    } else {
                        empty.setBackground(DARK);
                    }

                    empty.setMaxSize(ICON_SIZE, ICON_SIZE);

                    empty.setMinSize(ICON_SIZE, ICON_SIZE);

                    board.add(empty, col, row);

                    int finalRow1 = row;
                    int finalCol1 = col;
                    empty.setOnAction(event -> model.selectOrCapture(new Coordinates(finalRow1, finalCol1)));

                } else {
                    Button button = new Button();

                    switch (model.getPiece(row, col)) {

                        case ChessConfig.king -> button.setGraphic(new ImageView(king));

                        case ChessConfig.knight -> button.setGraphic(new ImageView(knight));

                        case ChessConfig.pawn -> button.setGraphic(new ImageView(pawn));

                        case ChessConfig.queen -> button.setGraphic(new ImageView(queen));

                        case ChessConfig.bishop -> button.setGraphic(new ImageView(bishop));

                        case ChessConfig.rook -> button.setGraphic(new ImageView(rook));
                    }

                    if ((row % 2 == col % 2)) {
                        button.setBackground(LIGHT);
                    } else {
                        button.setBackground(DARK);
                    }

                    button.setMaxSize(ICON_SIZE, ICON_SIZE);

                    button.setMinSize(ICON_SIZE, ICON_SIZE);

                    board.add(button, col, row);

                    int finalRow = row;
                    int finalCol = col;

                    button.setOnAction(event -> model.selectOrCapture(new Coordinates(finalRow, finalCol)));

                }

            }

        }

    }

    @Override
    public void start(Stage stage) throws Exception {

        this.stage = stage;

        //the main components

        BorderPane main = new BorderPane();

        //make the board

        updateBoard();

        //make the top label that displays game status

        HBox topBox = new HBox();

        topBox.getChildren().add(gameStatus);
        topBox.setAlignment(Pos.CENTER);

        HBox bottom = new HBox();

        //the reset button and its logic

        Button reset = new Button("Reset");
        reset.setFont(Font.font(FONT_SIZE));
        reset.setOnAction(event -> {
            try {
                model.reset();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });


        //creates load button and makes it work

        Button load = new Button("Load");
        load.setFont(Font.font(FONT_SIZE));
        load.setOnAction(event -> {

            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Load puzzle");

            String currentPath = Paths.get(".").toAbsolutePath().normalize().toString();
            currentPath += File.separator + "data" + File.separator + "chess";
            fileChooser.setInitialDirectory(new File(currentPath));

            File file = fileChooser.showOpenDialog(stage);
            String filename = file.getPath();
            try {
                model.load(filename);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        });

        Button hint = new Button("Hint");
        hint.setFont(Font.font(FONT_SIZE));
        hint.setOnAction(event -> model.hint());

        bottom.getChildren().add(reset);

        bottom.getChildren().add(load);

        bottom.getChildren().add(hint);

        bottom.setAlignment(Pos.CENTER);

        bottom.setSpacing(30);

        //putting it all together

        main.setBottom(bottom);
        main.setCenter(board);
        main.setTop(topBox);

        Scene scene = new Scene(main);
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void update(ChessModel chessModel, String msg) {

        updateBoard();

        this.stage.sizeToScene();  // when a different sized puzzle is loaded

        this.gameStatus.setText(msg);

    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}
