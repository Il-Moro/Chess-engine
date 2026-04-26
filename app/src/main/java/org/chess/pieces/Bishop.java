package org.chess.pieces;

import java.util.Set;

import org.chess.dataTypes.*;
import org.chess.movementsRules.*;
import org.chess.organization.*;

public class Bishop extends Piece {
    
    @Override
    public Set<Position> getLegalMoves(ChessBoard chessBoard, Position position) {
    
        return MoveCalculator.diagonals(chessBoard, position);
    }
}
