package org.chess.organization;

import org.chess.dataTypes.*;
import java.util.ArrayList;
import java.util.List;

public class Match {
    private final ChessBoard board;
    private final Player whitePlayer;
    private final Player blackPlayer;
    private List<Move> moveHistory;
    private Player currentPlayerTurn;
    private End state;

    // Costruttore
    public Match(Player white, Player black) {

        this.whitePlayer = white;
        this.blackPlayer = black;
        this.board = new ChessBoard(true);
        this.currentPlayerTurn = white;
        this.state = End.IN_PROGRESS;
        this.moveHistory = new ArrayList<>();
    }

    // returns the chess board
    public ChessBoard getBoard() {
        return board;
    }

    // switches the current player turn
    public void switchTurn() {
        currentPlayerTurn = (currentPlayerTurn == whitePlayer) ? blackPlayer : whitePlayer;
    }

    // sets the current game state
    public void setState(End state) {
        this.state = state;
    }

    // returns the current game state
    public End getState() {
        return state;
    }

    // adds a move to the move history
    public void updateMoveHistory(Move move) {
        moveHistory.add(move);
    }

    // returns the move history
    public List<Move> getMoveHistory() {
        return moveHistory;
    }

    // returns the current player turn
    public Player getCurrentPlayerTurn() {
        return currentPlayerTurn;
    }

    // sets the current player turn based on the colour
    public void setCurrentPlayerTurn(Colour colour) {
        currentPlayerTurn = (colour == Colour.WHITE) ? whitePlayer : blackPlayer;
    }

    // returns the white player
    public Player getWhitePlayer() {
        return whitePlayer;
    }

    // returns the black player
    public Player getBlackPlayer() {
        return blackPlayer;
    }
}
