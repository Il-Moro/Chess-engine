package org.chess.organization;

import java.util.*;

import org.chess.dataTypes.*;
import org.chess.pieces.*;

public class ChessBoard {

    private Piece[][] chessboard;
    private int[][] squareControlledByWhite;
    private int[][] squareControlledByBlack;

    // costruttore
    public ChessBoard() {
        this.chessboard = new Piece[8][8];
        this.squareControlledByBlack = new int[8][8];
        this.squareControlledByWhite = new int[8][8];
        this.updateControl();
    }

    // setter
    public Piece getPiece(Position position) {
        return chessboard[position.row()][position.column()];
    }

    public void setPiece(Piece piece) {
        this.chessboard[piece.getPosition().row()][piece.getPosition().column()] = piece;
        this.updateControl();
    }

    // getter
    public int[][] getSquareControlledBy(String colour){
        if(colour == "white"){
            return this.squareControlledByWhite;
        } else{
            return this.squareControlledByBlack;
        }
    }

    // altri
    public boolean isNull(Position position) {
        return chessboard[position.row()][position.column()] == null;
    }

    public void movePiece(Piece piece, Position to){
        try {

        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    void updateControl(){
        // azzero tabelle
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                squareControlledByBlack[i][j] = 0;
                squareControlledByWhite[i][j] = 0;
            }
        }

        for(Piece[] positions : this.chessboard){
            for(Piece p : positions){
                if(p != null){
                    fillControlMap(p);
                }
            }
        }        
    }

    void fillControlMap(Piece piece){
        Set<Position> set = piece.getPotentialMoves(this);

        if(piece.getColour() == "white"){
            for(Position s : set){
                this.squareControlledByWhite[s.row()][s.column()] += 1;
            }
        } else{
            for(Position s : set){
                this.squareControlledByBlack[s.row()][s.column()] += 1;
            }
        }
    }

}


