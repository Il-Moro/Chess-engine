package org.chess.pieces;

import java.util.HashSet;
import java.util.Set;

import org.chess.dataTypes.*;
import org.chess.organization.*;


public class Queen extends Piece{

    public Queen(Position position, String colour){
        super(position, colour);
        this.value = 9;
    }
    
    @Override
    public Set<Position> getLegalMoves(ChessBoard board) {

        Set<Position> moves = new HashSet<Position>();

        int pieceColumn = this.position.column();
        int pieceRow = this.position.row();

        int[][] directionsBishop = {{1, 1}, {-1, 1}, {-1, -1}, {1, -1}, {1, 0}, {-1, 0}, {0, 1}, {0, -1}};

        for (int[] d : directionsBishop) {

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

