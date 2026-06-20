package org.chess.graphics;

import org.chess.pieces.*;

import java.lang.annotation.ElementType;
import java.util.List;

import javax.swing.SwingWorker;

import org.chess.dataTypes.*;

public class ChessController {
    private ChessView view;
    private ChessModel model;
    private String selectedMode;
    private Piece[][] board;
    private End gameState;
    private Colour currentPlayerColor;
    private Colour playerColor;
    private boolean isAgentTurn = false;
    private String selectedPromotion; 

    public ChessController(ChessView view, ChessModel model) {
        this.view = view;
        this.model = model;
        view.setController(this);
    }

    public void startGame(String mode, String color, String difficulty) {
        
        model.choosePlayerColors(color);
        switch (mode) {
            case "Human vs AI" -> model.setHumanVSAgent();
            case "Human vs Human" -> model.setHumanVSHuman();
            default -> model.setHumanVSAgent();
        }

        model.setDifficulty(switch (difficulty) {
            case "EASY" -> 3;
            case "MEDIUM" -> 4;
            case "HARD" -> 12;
            default -> 3;
        });

        model.startMatch();
        selectedMode = mode;
        board = model.getBoardAsArray();
        gameState = model.getGameState();
        currentPlayerColor = model.getCurrentPlayerColour();
        playerColor = color.equals("BLACK") ? Colour.BLACK : Colour.WHITE;
        isAgentTurn = model.isAgentTurn();
        if (currentPlayerColor == Colour.WHITE) {
            view.setPlayerTurn("WHITE");
        }
        else
            view.setPlayerTurn("BLACK");
            
        view.gameScreen();
        view.displayBoard(board);
        isAgentTurn = model.isAgentTurn();
        if (isAgentTurn) {
            agentTurn();
        }
    }

    public void onSquareSelected(int row, int col) {

        List<Move> moves = model.getLegalMovesForPiece(board[row][col]);

        for (Move m : moves) {
            int targetRow = m.to().row();
            int targetCol = m.to().column();

            if (playerColor == Colour.WHITE) {
                targetRow = 7 - targetRow;
            }

            view.highlightSquares(targetRow, targetCol);
        }
    }

    public void clearAllHighlights() {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                view.clearHighlights(row, col);
            }
        }
    }

    public void onMoveAttempt(int fromRow, int fromCol, int toRow, int toCol) {
        if (gameState != End.IN_PROGRESS) return;

        Move move = model.humanMove(board[fromRow][fromCol], new Position(toRow, toCol));
        if (move != Move.INVALID) {
            if (model.isPromotionMove(move)) {
                if (currentPlayerColor == Colour.WHITE) {
                    view.showPromotionDialog("WHITE", toRow, toCol);
                } else
                    view.showPromotionDialog("BLACK", toRow, toCol);

                model.updateGameStateAfterMove(move, selectedPromotion);
            } else {
                model.updateGameStateAfterMove(move);
            }

            currentPlayerColor = model.getCurrentPlayerColour();
            gameState = model.isCheckmateOrStalemate();

            if (gameState == End.CHECKMATE) {
                view.displayBoard(board);
                view.gameover("CHECKMATE");
                return;
            } else if (gameState == End.STALEMATE) {
                view.displayBoard(board);
                view.gameover("STALEMATE");
                return;
            }

            if (currentPlayerColor == Colour.WHITE) {
                view.setPlayerTurn("WHITE");
            } else {
                view.setPlayerTurn("BLACK");
            }
            view.displayBoard(board);
            isAgentTurn = model.isAgentTurn();
            if(isAgentTurn){
                agentTurn();
            }
        }
    }

    public void onPromotion(String promotion){
        switch (promotion) {
            case "Rook":
                selectedPromotion = "R";
                break;
            
            case "Bishop":
                selectedPromotion = "B";
                break;

            case "Knight":
                selectedPromotion = "K";
                break;
        
            default:
                selectedPromotion = "Q";
                break;
        }        
    }

    public void agentTurn() {
        if (gameState != End.IN_PROGRESS) return;
        new SwingWorker<Move, Void>() {
            @Override
            protected Move doInBackground() {
                return model.agentMove();
            }
            @Override
            protected void done() {
                try {
                    Move move = get();
                    if (move != Move.INVALID) {
                        model.updateGameStateAfterMove(move);
                        currentPlayerColor = model.getCurrentPlayerColour();
                        gameState = model.isCheckmateOrStalemate();
                        if (gameState == End.CHECKMATE) {
                            view.displayBoard(board);
                            view.gameover("CHECKMATE");
                            return;
                        } else if (gameState == End.STALEMATE) {
                            view.displayBoard(board);
                            view.gameover("STALEMATE");
                            return;
                        }
                        if (currentPlayerColor == Colour.WHITE)
                            view.setPlayerTurn("WHITE");
                        else
                            view.setPlayerTurn("BLACK");
                        view.displayBoard(board);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.execute();
    }



}
