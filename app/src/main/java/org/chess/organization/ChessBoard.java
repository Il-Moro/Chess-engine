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
    }

    // getter
    public Piece[][] getBoard(){
        return this.chessboard;
    }

    public int[][] getSquareControlledBy(String colour){
        if(colour == "white"){
            return this.squaresControlledByWhite;
        } else{
            return this.squaresControlledByBlack;
        }
    }    

    public boolean isNull(Position position) {
        return chessboard[position.row()][position.column()] == null;
    }

    // movement of pieces
    public boolean isMoveLegal(Position from, Position to){
        Piece piece = this.getPiece(from);
        boolean legal=true;
        // Se non c'è un pezzo nella posizione di partenza
        if (piece == null) {
            legal=false;
        }
        // mossa su pezzo proprio sasa
        else if (!piece.getPotentialMoves(this).contains(to)) {
            legal = false;
        }
        else if (piece.getColour().equals(this.getPiece(to).getColour())) {
            legal = false;
        }
        // casistiche re: moro
        //      1. non può muoversi su case controllate da avversario
        //      2. mossa obbligata del re: se il re è sotto scacco bisogna coprirlo o muoverlo
        //      3. controllo sull'arrocco
        // pedone: sasa 
        else if(piece instanceof Pawn){
            //      1. non può spostarsi in avanti se è presente un'altro pezzo
            if(from.row()!=to.row() && from.column()==to.column() && !this.isNull(to))
                legal=false;
            //      2. enpassant: considerare traversa iniziale e se si affianca a un pedone opposto
            
        }

        // checkmate: insieme

        return legal;
    }

    public void physicalMovement(Position from, Position to){

        // arrocco
        // promozione

        Piece piece = this.getPiece(from);
        this.chessboard[from.row()][from.column()] = null;
        this.chessboard[to.row()][to.column()] = piece;
        piece.setPosition(to);        
        this.updateControl();
    }

    // Updating control maps
    public void updateControl(){
        // azzero tabelle
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                this.squaresControlledByBlack[i][j] = 0;
                this.squaresControlledByWhite[i][j] = 0;
            }
        }

        for(Piece[] positions : this.chessboard){
            for(Piece p : positions){
                if(p != null){
                    this.fillControlMap(p);
                }
            }
        }        
    }

    private void fillControlMap(Piece piece){
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