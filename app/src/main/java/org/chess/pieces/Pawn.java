package org.chess.pieces;

import java.util.HashSet;
import java.util.Set;

import org.chess.dataTypes.*;
import org.chess.organization.*; 

public class Pawn extends Piece{

    // private boolean enPassant;

    public Pawn(Position position, Colour colour){
        super(position, colour);
    }

    @Override
    public Set<Position> getPotentialMoves(ChessBoard board) {
        
        Set<Position> moves = new HashSet<>();

        int pieceRow = this.position.row();
        int pieceColumn = this.position.column();

        int[][] directions = {{1,-1},{1, 0}, {1, 1}};

        for (int d[] : directions){
            
            int targetRow, targetColumn;
            
            if(this.colour == (Colour.WHITE)){
                targetRow = pieceRow + d[0];
                targetColumn = pieceColumn + d[1];
            } else {
                targetRow = pieceRow - d[0];
                targetColumn = pieceColumn -d[1];
            }

            Position targetPosition = new Position(targetRow, targetColumn); 
            
            if(Position.isInsideBounds(targetRow, targetColumn)){
                if(d[0] * d[1] == 0){ 
                    if(board.isNull(targetPosition)){
                        moves.add(targetPosition);
                        if(this.colour == (Colour.WHITE) && pieceRow == 1){
                            Position doubleStepPosition = new Position(targetRow + 1, targetColumn);
                            if(board.isNull(doubleStepPosition)){
                                moves.add(doubleStepPosition);
                            }
                        } else if(this.colour == (Colour.BLACK) && pieceRow == 6){
                            Position doubleStepPosition = new Position(targetRow - 1, targetColumn);
                            if(board.isNull(doubleStepPosition)){
                                moves.add(doubleStepPosition);
                            }
                        }
                    }
                } else {
                    moves.add(targetPosition);
                }
            }
        }
        return moves;
    }
}