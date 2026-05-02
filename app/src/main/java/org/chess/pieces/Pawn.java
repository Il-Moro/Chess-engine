package org.chess.pieces;

import java.util.HashSet;
import java.util.Set;

import org.chess.dataTypes.*;
import org.chess.movementsRules.*;
import org.chess.organization.*; 

public class Pawn extends Piece{

<<<<<<< HEAD
    @Override
    public Set<Position> getLegalMoves(ChessBoard chessBoard, Position position) {
    
        return MoveCalculator.movesOfPawn(chessBoard, position);
=======
    // private boolean enPassant;

    public Pawn(Position position, String colour){
        super(position, colour);
        this.value = 3;
    }

    @Override
    public Set<Position> getPotentialMoves(ChessBoard board) {
        
        Set<Position> moves = new HashSet<>();

        int pieceRow = this.position.row();
        int pieceColumn = this.position.column();

        int[][] directions = {{1,-1},{1, 0}, {1, 1}};

        for (int d[] : directions){
            
            int targetRow, targetColumn;
            
            if(this.colour == "white"){
                targetRow = pieceRow + d[0];
                targetColumn = pieceColumn + d[1];
            } else {
                targetRow = pieceRow - d[0];
                targetColumn = pieceColumn -d[1];
            }

            Position targetPosition = new Position(targetRow, targetColumn); 
            
            if(0 <= targetRow && targetRow < 8 && 0 <= targetColumn && targetColumn < 8){
                
                moves.add(targetPosition);
                
                if(d[0]*d[1] == 0){    
                    if(pieceRow == 1 || pieceRow == 6){
                        if(this.colour == "white"){
                            moves.add(new Position(targetRow + 1, targetColumn));
                        } else {
                            moves.add(new Position(targetRow - 1, targetColumn));
                        }   
                    }
                }
            }
        }
        return moves;
>>>>>>> origin/feature/PiecesAndMovements
    }
}