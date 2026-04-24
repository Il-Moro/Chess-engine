package org.chess.Pieces;

import java.util.Set;

import org.chess.dataTypes.*;
import org.chess.chessBoard.*;
import org.chess.movementsRules.*;

public class Knight extends Piece{

    @Override
    public Set<Position> getLegalMoves(ChessBoard chessBoard, Position position) {
    
        return MoveCalculator.knightJump(chessBoard, position);
    }
}
