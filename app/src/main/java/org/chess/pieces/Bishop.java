package org.chess.pieces;

import java.util.HashSet;
import java.util.Set;

import org.chess.dataTypes.*;
import org.chess.organization.*;

public class Bishop extends Piece {
    
    public Bishop(Position position, Colour colour) {
        super(position, colour);
    }

    
    @Override
    public Set<Position> getPotentialMoves(ChessBoard board) {
        Set<Position> moves = new HashSet<Position>();

        int pieceColumn = this.position.column();
        int pieceRow = this.position.row();

        int[][] directions = {{1, 1}, {-1, 1}, {-1, -1}, {1, -1}};

        for (int[] d : directions) {

            int targetRow = pieceRow + d[0];
            int targetColumn = pieceColumn + d[1];

            while (Position.isInsideBounds(targetRow, targetColumn)) {
                
                Position targetPosition = new Position(targetRow, targetColumn);
                
                if (board.isNull(targetPosition)) {
                    moves.add(targetPosition);
                } else {                    
                    moves.add(targetPosition); 
                    Piece hitPiece = board.getPiece(targetPosition);
                    if (hitPiece instanceof King && hitPiece.getColour() != this.colour) {
                        // The ray continues through the opponent's King
                    } else {
                        break;
                    }
                }
                targetRow += d[0];
                targetColumn += d[1];
            }
        }
        return moves;
    }
}




