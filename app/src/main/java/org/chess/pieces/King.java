package org.chess.pieces;

import java.util.Set;

import org.chess.dataTypes.*;
import org.chess.movementsRules.*;
import org.chess.organization.*;

public class King extends Piece{

    // private boolean firstMove;

    public King(String colour){
        
        this.value = Integer.MAX_VALUE;
        // this.firstMove = true;
        
        if(colour == "White"){
            this.position = new Position(5,1);
            this.colour = "White";
        } else if(colour == "Black") {
            this.position = new Position(5,8);
            this.colour = "Black";
        }
    }

    @Override
    public Set<Position> getLegalMoves(ChessBoard chessBoard, Position position) {
    
        return MoveCalculator.movesOfKing(chessBoard, position);
    }
}
