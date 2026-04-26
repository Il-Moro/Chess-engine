package org.chess.pieces;

import java.util.Set;

import org.chess.dataTypes.*;
import org.chess.organization.*;

public class Bishop extends Piece {
    
    public Bishop(Position position, String colour) {
        super(position, colour);
    }

    @Override
    public Set<Position> getLegalMoves(ChessBoard chessBoard) {
    
        return null;
    }
}
