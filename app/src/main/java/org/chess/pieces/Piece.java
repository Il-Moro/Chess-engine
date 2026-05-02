package org.chess.pieces;
import java.util.Set;

import org.chess.dataTypes.Position;
import org.chess.organization.ChessBoard;

public abstract class Piece {
    protected Position position;
    protected String colour;
    protected int value;
<<<<<<< HEAD
    
=======

    //costruttori
    public Piece(Position position, String colour){
        this.position = position;
        this.colour = colour;
    }
    
    // getter
>>>>>>> origin/feature/PiecesAndMovements
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
<<<<<<< HEAD
    

    public abstract Set<Position> getLegalMoves(ChessBoard chessBoard, Position position);
=======

    public int getValue(){
        return this.value;
    }
    
    // setter
    public void setPosition(Position newPosition){
        this.position = newPosition;
    }    

    // altri metodi 
    public abstract Set<Position> getPotentialMoves(ChessBoard chessBoard);
    
>>>>>>> origin/feature/PiecesAndMovements
}