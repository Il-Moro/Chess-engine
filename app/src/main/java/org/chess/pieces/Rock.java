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
    public Set<Position> getLegalMoves(ChessBoard board) {
        
        Set<Position> moves = new HashSet<Position>();

        int pieceColumn = this.getPosition().column();
        int pieceRow = this.getPosition().row();

        int[][] directions = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};

        for (int[] d : directions) {

            int row = pieceRow + d[0];
            int column = pieceColumn + d[1];

            while (0 <= row && row < 8 && 0 <= column && column < 8) {
                
                Position squareOtherPiece = new Position(row, column);
                
                if (board.isNull(squareOtherPiece)) {
                    moves.add(squareOtherPiece);
                } else {
                    if (!board.getPiece(squareOtherPiece).getColour().equals(this.colour)) {
                        moves.add(squareOtherPiece); 
                    }
                    break;
                }
                row += d[0];
                column += d[1];
            }
        }
        return moves;
    }
}
