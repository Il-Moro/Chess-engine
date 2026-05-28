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

    

    public Player(Colour colour){
        this.colour=colour;
    }

    public Move submitHumanMove(Piece selectedPiece, Position to){
        return new Move(selectedPiece,to);
    }

    public Move agentMove(){
        
        // TODO: Implement agent ai algorithm
        return new Move(agentPiece,toAgentPiece);
    }

    public void setChessBoard (ChessBoard board){
        this.board=board;
    }


}
