package org.chess.chessBoard;

import org.chess.Pieces.*;
import org.chess.dataTypes.*;

public class ChessBoard {
    private Piece[][] chessboard;

    public ChessBoard() {
        chessboard = new Piece[8][8];
    }

    public Piece getPiece(Position position) {
        return chessboard[position.column()][position.row()];
    }

    public void setPiece(Piece piece, Position position) {
        this.chessboard[position.column()][position.row()] = piece;
    }

    public boolean isNull(Position position) {
        return chessboard[position.column()][position.row()] == null;
    }


} 