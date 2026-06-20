package org.chess.graphics;

import org.chess.pieces.*;
import org.chess.dataTypes.*;

public class ChessController {
    private ChessView view;
    private ChessModel model;
    private String selectedMode;
    private Piece[][] board;
    private End gameState;
    private Colour currentPlayerColor;
    private boolean isAgentTurn;

    public ChessController(ChessView view, ChessModel model) {
        this.view = view;
        this.model = model;
        view.setController(this);
    }

    public void startGame(String mode, String color, String difficulty) {
        

        model.choosePlayerColors(color);
        
        switch (mode) {
            case "Human vs AI" -> model.setHumanVSAgent();
            case "Human vs Human" -> model.setHumanVSHuman();
            default -> model.setHumanVSAgent();
        }

        model.setDifficulty(switch (difficulty) {
            case "EASY" -> 3;
            case "MEDIUM" -> 4;
            case "HARD" -> 5;
            default -> 3;
        });

        model.startMatch();

        selectedMode = mode;
        board = model.getBoardAsArray();
        gameState = model.getGameState();
        currentPlayerColor = model.getCurrentPlayerColour();
        isAgentTurn = model.isAgentTurn();

        view.gameScreen();
        view.displayBoard(board);
    }
}
