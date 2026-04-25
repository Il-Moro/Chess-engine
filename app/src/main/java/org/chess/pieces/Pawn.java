package org.chess.pieces;

import java.util.Set;

import org.chess.dataTypes.*;
import org.chess.organization.*; 

public class Pawn extends Piece{

    public Pawn(String colour){
        super(colour);
    }

    @Override
    public Set<Position> getLegalMoves(ChessBoard chessBoard) {
    
        return null;
    }
}