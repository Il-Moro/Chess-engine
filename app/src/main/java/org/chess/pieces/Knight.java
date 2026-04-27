package org.chess.pieces;

import java.util.HashSet;
import java.util.Set;

import org.chess.dataTypes.*;
import org.chess.organization.*;

public class Knight extends Piece{

    public Knight(Position position, String colour){
        super(position, colour);
    }

    @Override
    public Set<Position> getLegalMoves(ChessBoard chessBoard) {
        Set<Position> moves = new HashSet<Position>();

        int pieceColumn = this.getPosition().column();
        int pieceRow = this.getPosition().row();

        int[][] rules = {{2, -1}, {2, 1}, {1, 2}, {-1, 2}, {-2, 1}, {-2, -1}, {-1, -2}, {1, -2}};
        
        for(int r[] : rules){
            moves.add(new Position(pieceRow + r[0], pieceColumn + r[1]));    
        }
        return moves;
    }
}
