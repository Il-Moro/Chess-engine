package org.chess.organization;

import org.chess.dataTypes.*;
import org.chess.pieces.*;

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

    public void movePiece(Position from, Position to){
        // Piece piece = this.chessboard[from.row()][from.column()];
        try {
            
        } catch (Exception e) {
            // TODO: handle exception
        }
    }
} 