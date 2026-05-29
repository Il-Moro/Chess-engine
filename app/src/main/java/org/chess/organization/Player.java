package org.chess.organization;

import org.chess.pieces.Piece;

import org.chess.dataTypes.Colour;
import org.chess.dataTypes.Move;
import org.chess.dataTypes.Position;

public class Player {

    private ChessBoard board;
    private Colour colour;
    private Piece agentPiece;
    private Position toAgentPiece;
    

    public Player(Colour colour, ChessBoard board){
        this.colour=colour;
        this.board=board;
    }

    public Move submitHumanMove(Piece selectedPiece, Position to){
        Move chosenMove=null;
        if(board.isMoveLegal(selectedPiece.getPosition(), to))
            chosenMove= new Move(selectedPiece,to);
        return chosenMove;
    }

    public Move agentMove(){
        
        // TODO: Implement agent ai algorithm
        return new Move(agentPiece,toAgentPiece);
    }


    public Colour getColour(){
        return this.colour;
    }
}
