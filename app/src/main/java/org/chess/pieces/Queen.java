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

        int pieceColumn = this.getPosition().column();
        int pieceRow = this.getPosition().row();

        int[][] directionsBishopMovements = {{1, 1}, {-1, 1}, {-1, -1}, {1, -1}};

        for (int[] d : directionsBishopMovements) {

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
       
        int[][] directionsRockMovements = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};

        for (int[] d : directionsRockMovements) {

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

