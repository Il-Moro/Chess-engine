package org.chess.pieces;

import java.util.HashSet;
import java.util.Set;

import org.chess.dataTypes.*;
import org.chess.organization.*;

public class King extends Piece{

    private boolean hasMoved = true;

    public King(Position position, Colour colour){
        super(position, colour);
        this.value = Integer.MAX_VALUE;
    }

    public King(Position position, Colour colour, boolean hasMoved){
        super(position, colour);
        this.hasMoved = hasMoved;
    }

    @Override
    public Set<Position> getPotentialMoves(ChessBoard board) {
        
        Set<Position> moves = new HashSet<>();

        int[][] directions = {{1,-1},{1,0},{1,1},{0,1},{-1,1},{-1,0,},{-1,-1},{0,-1}};

        for(int[] d : directions){
            int targetRow = this.position.row() + d[0];
            int targetColumn = this.position.column() + d[1];

            Position targetPosition = new Position(targetRow, targetColumn);

            if(Position.isInsideBounds(targetRow, targetColumn)){   
                moves.add(targetPosition);
            }
        }
        // mosse per l'arrocco
        // Controlliamo se il Re è nella sua colonna di partenza (4) e in una riga di partenza valida (0 o 7)
        if (!hasMoved && this.position.column() == 4 && (this.position.row() == 0 || this.position.row() == 7)) {
            int row = this.position.row();
            
            // Arrocco Corto (colonna 6) e Arrocco Lungo (colonna 2)
            moves.add(new Position(row, 6));
            moves.add(new Position(row, 2));
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
