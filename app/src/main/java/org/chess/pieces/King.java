package org.chess.Pieces;

import java.util.Set;

import org.chess.dataTypes.*;
import org.chess.chessBoard.*;
import org.chess.movementsRules.*;

public class King extends Piece{

    // private boolean firstMove;

    @Override
    public Set<Position> getLegalMoves(ChessBoard chessBoard, Position position) {
    
        return MoveCalculator.movesOfKing(chessBoard, position);
    }
}
