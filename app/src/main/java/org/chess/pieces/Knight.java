package org.chess.pieces;

import java.util.HashSet;
import java.util.Set;

import org.chess.dataTypes.*;
import org.chess.organization.*;

public class Knight extends Piece{

    public Knight(Position position, String colour){
        super(position, colour);
        this.value = 3;
    }

    @Override
    public Set<Position> getLegalMoves(ChessBoard board) {
        Set<Position> moves = new HashSet<Position>();

        int pieceColumn = this.getPosition().column();
        int pieceRow = this.getPosition().row();

        int[][] directions = {{2, -1}, {2, 1}, {1, 2}, {-1, 2}, {-2, 1}, {-2, -1}, {-1, -2}, {1, -2}};
        
        for(int d[] : directions){
            
            int targetRow = pieceRow + d[0];
            int targetColumn = pieceColumn + d[1];

            Position targetPosition = new Position(targetRow, targetColumn);

            if(0 <= targetRow && targetRow < 8 && 0 <= targetColumn && targetColumn < 8){
                moves.add(targetPosition); 
            }
        }
        return moves;
    }
}
