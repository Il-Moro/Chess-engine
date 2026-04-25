package org.chess.pieces;

import java.util.Set;

import org.chess.dataTypes.*;
import org.chess.organization.*;

public class King extends Piece{

    // private boolean firstMove;

    public King(String colour){
    
        super(colour);
        
        this.value = Integer.MAX_VALUE;
        // this.firstMove = true;
        
        if(colour == "White"){
            this.position = new Position(4,0);
            this.colour = "White";
        } else if(colour == "Black") {
            this.position = new Position(4,7);
            this.colour = "Black";
        }
    }

    @Override
    public Set<Position> getLegalMoves(ChessBoard chessBoard) {
    
        return null;
    }
}
