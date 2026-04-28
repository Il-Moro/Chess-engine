package org.chess.pieces;

import java.util.HashSet;
import java.util.Set;

import org.chess.dataTypes.*;
import org.chess.organization.*; 

public class Pawn extends Piece{

    private boolean enPassant;

    public Pawn(Position position, String colour){
        super(position, colour);
        if((this.colour == "white" && position.row() == 1) || (this.colour == "black" && position.row() == 6)){
            enPassant = true;
        }
    }

    @Override
    public Set<Position> getLegalMoves(ChessBoard board) {
        
        Set<Position> moves = new HashSet<>();

        int pieceRow = this.position.row();
        int pieceColumn = this.position.column();

        int[][] directions = {{1,-1},{1, 0}, {1, 1}};

        for (int d[] : directions){
            int row, col;
            if(this.colour == "white"){
                row = pieceRow + d[0];
                col = pieceColumn + d[1];
            } else {
                row = pieceRow - d[0];
                col = pieceColumn -d[1];
            }

            Position targetPosition = new Position(row, col); 
            
            if(0 <= row && row < 8 && 0 <= col && col < 8){
                if(d[0]*d[1] == 0 && board.isNull(targetPosition)){
                    moves.add(targetPosition);
                    if(pieceRow == 1 || pieceRow == 6){
                        if(this.colour == "white"){
                            moves.add(new Position(row + 1, col));
                        } else {
                            moves.add(new Position(row - 1,col));
                        }   
                    }
                } else if(d[0]*d[1] != 0 && !board.isNull(targetPosition)){
                    if(board.getPiece(targetPosition).getColour() != this.colour)
                    moves.add(targetPosition);
                }
            }
        }
        return moves;
    }
}