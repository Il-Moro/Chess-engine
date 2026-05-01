package org.chess.pieces;

import java.util.HashSet;
import java.util.Set;

import org.chess.dataTypes.*;
import org.chess.organization.*; 

public class Rock extends Piece {

    public Rock(Position position, String colour){
        super(position, colour);
        this.value = 5;
    }

    @Override
    public Set<Position> getPotentialMoves(ChessBoard board) {
        
        Set<Position> moves = new HashSet<Position>();

        int pieceColumn = this.getPosition().column();
        int pieceRow = this.getPosition().row();

        int[][] directions = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};

        for (int[] d : directions) {

            int targetRow = pieceRow + d[0];
            int targetColumn = pieceColumn + d[1];

            while (0 <= targetRow && targetRow < 8 && 0 <= targetColumn && targetColumn < 8) {
                
                Position targePosition = new Position(targetRow, targetColumn);
                
                if (board.isNull(targePosition)) {
                    moves.add(targePosition);
                } else {
                    moves.add(targePosition);
                    break;
                }
                targetRow += d[0];
                targetColumn += d[1];
            }
        }
        return moves;
    }
}
