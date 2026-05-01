package org.chess.pieces;

import java.util.HashSet;
import java.util.Set;

import org.chess.dataTypes.*;
import org.chess.organization.*;

/**
 * classe Alfiere:
 *      attributi: ereditati da Piece
 *      metodi: ereditati da Piece
 */

public class Bishop extends Piece {
    
    public Bishop(Position position, String colour) {
        super(position, colour);
        this.value = 3;
    }

    @Override
    public Set<Position> getLegalMoves(ChessBoard board) {
        Set<Position> moves = new HashSet<Position>();

        int pieceColumn = this.position.column();
        int pieceRow = this.position.row();

        int[][] directions = {{1, 1}, {-1, 1}, {-1, -1}, {1, -1}};

        for (int[] d : directions) {

            int targetRow = pieceRow + d[0];
            int targetColumn = pieceColumn + d[1];

            while (0 <= targetRow && targetRow < 8 && 0 <= targetColumn && targetColumn < 8) {
                
                Position targetPosition = new Position(targetRow, targetColumn);
                
                if (board.isNull(targetPosition)) {
                    moves.add(targetPosition);
                } else{                    
                    moves.add(targetPosition); 
                    break;
                }
                targetRow += d[0];
                targetColumn += d[1];
            }
        }
        return moves;
    }
}




