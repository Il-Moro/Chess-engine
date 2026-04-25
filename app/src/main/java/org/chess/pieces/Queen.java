package org.chess.pieces;

import java.util.Set;

import org.chess.dataTypes.*;
import org.chess.organization.*;


public class Queen extends Piece{

    public Queen(String colour){

        super(colour);
        
    }
    
    @Override
    public Set<Position> getLegalMoves(ChessBoard chessBoard) {
        
        return null;
    }
}

