package org.chess.pieces;
import java.util.Set;

import org.chess.dataTypes.Position;
import org.chess.organization.ChessBoard;

public abstract class Piece {
    
    protected Position position;
    protected String colour;
    protected int value;

    //costruttori
    public Piece(Position position, String colour){
        this.position = position;
        this.colour = colour;
    }
    
    // getter
    public Position getPosition(){
        return this.position;
    }

    public String getColour(){
        return this.colour;
    }

    public int getValue(){
        return this.value;
    }
    
    // setter
    public void setPosition(Position newPosition){
        this.position = newPosition;
    }    

    // altri metodi 
    public abstract Set<Position> getLegalMoves(ChessBoard chessBoard);
    
}