package org.chess.pieces;

import java.util.HashSet;
import java.util.Set;

import org.chess.dataTypes.*;
import org.chess.organization.*;

public class King extends Piece{

    public King(Position position, String colour){
    
        super(position, colour);

        this.value = Integer.MAX_VALUE;
    }

    @Override
    public Set<Position> getLegalMoves(ChessBoard board) {
        
        Set<Position> moves = new HashSet<>();

        int[][] directions = {{1,-1},{1,0},{1,1},{0,1},{-1,1},{-1,0,},{-1,-1},{0,-1}};

        for(int[] d : directions){
            int row = this.position.row() + d[0];
            int col = this.position.column() + d[1];

            Position targetPosition = new Position(row, col);

            if(0 <= row && row < 8 && 0 <= col && col < 8){
                if(board.isNull(targetPosition)){
                    moves.add(targetPosition);
                } else if(board.getPiece(targetPosition).getColour() != this.colour){
                    moves.add(targetPosition);
                }
            }
        }
        return moves;        
    }
}
