package org.chess.Pieces;

import java.util.Set;

import org.chess.chessBoardAndPlayer.*;
import org.chess.dataTypes.*;
import org.chess.movementsRules.*;


public class Queen extends Piece{
    
    @Override
    public Set<Position> getLegalMoves(ChessBoard chessBoard, Position position) {
        
        return MoveCalculator.horizontalAndVertical(chessBoard, position);
    }
}

