package org.chess.pieces;

import java.util.HashSet;
import java.util.Set;

import org.chess.dataTypes.*;
import org.chess.organization.*; 

public class Rock extends Piece {

    private boolean hasMoved;

    public Rock(Position position, Colour colour){
        super(position, colour);
        this.value = 5;
    }

    public Rock(Position position, Colour colour, boolean hasMoved){
        super(position, colour);
        this.value = 5;
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
                    break;
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
