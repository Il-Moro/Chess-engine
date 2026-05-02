package org.chess.pieces;

import java.util.Set;

import org.chess.dataTypes.*;
import org.chess.movementsRules.*;
import org.chess.organization.*;


public class Queen extends Piece{
    
    @Override
    public Set<Position> getLegalMoves(ChessBoard chessBoard, Position position) {
        
        return MoveCalculator.horizontalAndVertical(chessBoard, position);
    }
}

