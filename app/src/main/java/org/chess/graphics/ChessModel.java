package org.chess.graphics;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.chess.dataTypes.*;
import org.chess.pieces.*;
import org.chess.organization.*;

public class ChessModel {

    private ChessBoard board = new ChessBoard(true);;
    private Match match = null;
    private Player player1 = null;
    private Player player2 = null;
    private Colour playerColor;
    private Colour opponentColor;
    private List <Move> legalMoves;
    private int DEPTH = 3;

    public ChessModel() {}

    public void choosePlayerColors(String color){
        switch (color) {
            case "BLACK" -> {
                playerColor = Colour.BLACK;
                opponentColor = Colour.WHITE;
            }
            default -> {
                playerColor = Colour.WHITE;
                opponentColor = Colour.BLACK;
            }
        }
    }
    
    public void setHumanVSAgent(){
        this.player1 = new PlayerHuman(playerColor, board);
        this.player2 = new PlayerAgent(opponentColor, board, DEPTH, player1);
    }

    public void setHumanVSHuman(){
        this.player1 = new PlayerHuman(playerColor, board);
        this.player2 = new PlayerHuman(opponentColor, board);
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

    public boolean updateGameStateAfterMove(Move move){
        if(move != Move.INVALID){
            match.updateMoveHistory(move);
            match.switchTurn();
            return true;
        }
        return false;
    }

    public boolean updateGameStateAfterMove(Move move,String promotionPiece){
        if(move != Move.INVALID){
            board.physicalMovement(move.selectedPiece().getPosition(), move.to(),promotionPiece);
            match.updateMoveHistory(move);
            match.switchTurn();
            return true;
        }
        return false;
    }

    public boolean isPromotionMove(Move move){
        return move.selectedPiece() instanceof Pawn && (move.to().row() == 0 || move.to().row() == 7);
    }

    public Piece[][] getBoardAsArray() {
        return board.getBoard();
    }

    public Colour getCurrentPlayerColour() {
        return match.getCurrentPlayerTurn().getColour();
    }

    public End getGameState() {
        return match.getState();
    }

    public boolean isAgentTurn() {
        return match.getCurrentPlayerTurn() instanceof PlayerAgent;
    }

    public List<Move> getLegalMovesForPiece(Piece selectedPiece){
        legalMoves=new ArrayList<>();

        Set<Position> potentialMoves=selectedPiece.getPotentialMoves(board);
        
        for(Position m:potentialMoves){
            if (board.isMoveLegal(selectedPiece.getPosition(), m)) {
                legalMoves.add(new Move(selectedPiece,m));
            }
        }

        return legalMoves;
    }
}
