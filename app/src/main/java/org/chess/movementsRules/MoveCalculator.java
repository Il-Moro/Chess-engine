package org.chess.movementsRules;

import java.util.HashSet;
import java.util.Set;

import org.chess.dataTypes.*;
import org.chess.organization.*;


public class MoveCalculator{
    
    public static Set<Position> horizontalAndVertical(ChessBoard chessBoard, Position currentPosition){
        
        Set<Position> legalMovements = new HashSet<Position>();

        int pieceColumn = currentPosition.column();
        int pieceRow = currentPosition.row();

        for(int i = 1; i < 9; i++){
            
            if( i != pieceRow){
                legalMovements.add(new Position(i, pieceColumn));
            }
            
            if( i != pieceColumn){
                legalMovements.add(new Position(pieceRow, i));
            }
        }
        return legalMovements;
    }
    
    public static Set<Position> diagonals(ChessBoard chessBoard, Position position){
        return null;
    }
    
    public static Set<Position> knightJump(ChessBoard chessBoard, Position position){
        return null;
    }

    public static Set<Position> movesOfPawn(ChessBoard chessBoard, Position position){
        return null;
    }

    public static Set<Position> movesOfKing(ChessBoard chessBoard, Position position){
        return null;
    }
}