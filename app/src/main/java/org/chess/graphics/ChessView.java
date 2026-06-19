package org.chess.graphics;

import org.chess.pieces.Piece;

public interface ChessView {
    void displayBoard(Piece[][] board);
    //void showMessage(String message);
    void highlightSquare(int row, int col, boolean highlight);
    void clearHighlights();
    void showGameOver(String result);
    void show();
}