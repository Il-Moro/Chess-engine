package org.chess.pieces;
import java.util.Set;

import org.chess.dataTypes.Colour;
import org.chess.dataTypes.Position;
import org.chess.organization.ChessBoard;

public abstract class Piece {
    
    protected Position position;
    protected Colour colour;

    //costruttori
    public Piece(Position position, Colour colour){
        this.position = position;
        this.colour = colour;
    }
    
    // getter
    public Position getPosition(){
        return this.position;
    }

    public Colour getColour(){
        return this.colour;
    }
    
    // setter
    public void setPosition(Position newPosition){
        this.position = newPosition;
    }    

    // altri metodi 
    public abstract Set<Position> getPotentialMoves(ChessBoard chessBoard);
    
}