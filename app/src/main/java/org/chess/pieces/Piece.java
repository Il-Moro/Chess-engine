package org.chess.pieces;
import java.util.Set;

import org.chess.dataTypes.Position;
import org.chess.organization.ChessBoard;

public abstract class Piece {
    protected Position position;
    protected String colour;
    protected int value;
    
    public Position getPosition(){
        return this.position;
    }
    
    public void setPosition(ChessBoard chessBoard, Position newPosition){
        // controls
        this.position = newPosition;
    }

    public int getValue(){
        return this.value;
    }

    public String getColour(){
        return this.colour;
    }
    

    public abstract Set<Position> getLegalMoves(ChessBoard chessBoard, Position position);
}