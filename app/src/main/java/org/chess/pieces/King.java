package org.chess.pieces;

import java.util.HashSet;
import java.util.Set;

import org.chess.dataTypes.*;
import org.chess.movementsRules.*;
import org.chess.organization.*;

public class King extends Piece{

    public King(Position position, String colour){
    
        super(position, colour);

        this.value = Integer.MAX_VALUE;
    }

    @Override
    public Set<Position> getPotentialMoves(ChessBoard board) {
        
        Set<Position> moves = new HashSet<>();

        int[][] directions = {{1,-1},{1,0},{1,1},{0,1},{-1,1},{-1,0,},{-1,-1},{0,-1}};

        for(int[] d : directions){
            int targetRow = this.position.row() + d[0];
            int targetColumn = this.position.column() + d[1];

            Position targetPosition = new Position(targetRow, targetColumn);

            if(0 <= targetRow && targetRow < 8 && 0 <= targetColumn && targetColumn < 8){   
                moves.add(targetPosition);
            }
        }
        return moves;
    }
}
