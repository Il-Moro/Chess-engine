package org.chess.pieces;

import java.util.HashSet;
import java.util.Set;

import org.chess.dataTypes.*;
import org.chess.organization.*; 

public class Rook extends Piece {

    private boolean hasMoved;

    public Rook(Position position, Colour colour){
        super(position, colour);
    }

    public Rook(Position position, Colour colour, boolean hasMoved){
        super(position, colour);
        this.hasMoved = hasMoved;
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

            while (Position.isInsideBounds(targetRow, targetColumn)) {
                
                Position targePosition = new Position(targetRow, targetColumn);
                
                if (board.isNull(targePosition)) {
                    moves.add(targePosition);
                } else {
                    moves.add(targePosition);
                    Piece hitPiece = board.getPiece(targePosition);
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

    public void setHasMovedTrue(){
        this.hasMoved = true;
    }
    public void setHasMovedFalse(){
        this.hasMoved = false;
    }
    public boolean getHasMoved(){
        return this.hasMoved;
    }
}
