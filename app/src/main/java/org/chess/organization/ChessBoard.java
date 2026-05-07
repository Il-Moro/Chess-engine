package org.chess.organization;

import java.util.*;

import org.chess.dataTypes.*;
import org.chess.pieces.*;

public class ChessBoard {

    private Piece[][] chessboard;
    private int[][] squaresControlledByWhite;
    private int[][] squaresControlledByBlack;

    // costruttore
    public ChessBoard() {
        this.chessboard = new Piece[8][8];
        this.squaresControlledByBlack = new int[8][8];
        this.squaresControlledByWhite = new int[8][8];
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
            return this.squaresControlledByWhite;
        } else{
            return this.squaresControlledByBlack;
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
                squaresControlledByBlack[i][j] = 0;
                squaresControlledByWhite[i][j] = 0;
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
                this.squaresControlledByWhite[s.row()][s.column()] += 1;
            }
        } else{
            for(Position s : set){
                this.squaresControlledByBlack[s.row()][s.column()] += 1;
            }
        }
    }

}


