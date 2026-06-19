package org.chess.graphics;

import org.chess.pieces.Piece;

public interface ChessView {

    void setupScreen();

    void gameScreen();

    void displayBoard(Piece[][] board);

    void highlightSquare(int row, int col, boolean highlight);

    void clearHighlights();

    void setStatus(String message);

    void gameover(String result);

    void show();
}