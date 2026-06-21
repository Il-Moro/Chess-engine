package org.chess.graphics;

import org.chess.pieces.Piece;

public interface ChessView {

    void setController(ChessController controller);

    void setupScreen();

    void gameScreen();

    void displayBoard(Piece[][] board);

    void highlightSquares(int row, int col);

    void clearHighlights(int row, int col);

    void showPromotionDialog(String colour, int row, int col);

    void gameover(String result);

    void setPlayerTurn(String playerTurn);

    void show();

}