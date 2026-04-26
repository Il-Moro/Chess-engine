package org.chess.pieces;

import java.util.Set;

import org.chess.dataTypes.*;
import org.chess.organization.*;

public class Knight extends Piece{

    public Knight(String colour){
        super(colour);
    }

    @Override
    public Set<Position> getLegalMoves(ChessBoard chessBoard) {
    
        return null;
    }
}
