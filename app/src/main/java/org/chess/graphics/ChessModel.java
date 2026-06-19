package org.chess.graphics;

import org.chess.dataTypes.*;
import org.chess.pieces.*;
import org.chess.organization.*;

public class ChessModel {

    public ChessBoard board;
    public Match match = null;
    public Player player1 = null;
    public Player player2 = null;
    private int DEPTH = 3;

    ChessModel() {}

    public void setAgentVSAgent(){
        this.player1 = new PlayerAgent(Colour.WHITE, board, DEPTH, player2);
        this.player2 = new PlayerAgent(Colour.BLACK, board, DEPTH, player1);
    }


    public void setHumanVSAgent(){
        this.player1 = new PlayerHuman(Colour.WHITE, board);
        this.player2 = new PlayerAgent(Colour.BLACK, board, DEPTH, player1);
    }

    public void setHumanVSHuman(){
        this.player1 = new PlayerHuman(Colour.WHITE, board);
        this.player2 = new PlayerHuman(Colour.BLACK, board);
    }
     
    public void setDifficulty(int depth){
        this.DEPTH = depth;
    }

    public Move humanMove(Piece selectedPiece, Position to){
        if(player1 instanceof PlayerHuman){
            ((PlayerHuman) player1).setMove(selectedPiece, to);
            return player1.decideMove();
        } else if(player2 instanceof PlayerHuman){
            ((PlayerHuman) player2).setMove(selectedPiece, to);
            return player2.decideMove();
        }
        return Move.INVALID;
    }

    public Move agentMove(){
        if(player1 instanceof PlayerAgent){
            return player1.decideMove();
        } else if(player2 instanceof PlayerAgent){
            return player2.decideMove();
        }
        return Move.INVALID;
     }

    public void startMatch(){
        this.match = new Match(player1, player2);
        this.board = match.getBoard();
    }

    public End isCheckmateOrStalemate(){
        Player currentPlayer = match.getCurrentPlayerTurn();
        if(board.isCheckmateOrStalemate(currentPlayer.getColour()) == End.CHECKMATE){
            match.setState(End.CHECKMATE);
        }
        else if(board.isCheckmateOrStalemate(currentPlayer.getColour()) == End.STALEMATE){
            match.setState(End.STALEMATE);
        }
        return match.getState();
    }

    public Move updateGameStateAfterMove(Move move){
        if(move != Move.INVALID){
            board.physicalMovement(move.selectedPiece().getPosition(), move.to());
            match.updateMoveHistory(move);
            match.switchTurn();
        }
        return;
    }


}
