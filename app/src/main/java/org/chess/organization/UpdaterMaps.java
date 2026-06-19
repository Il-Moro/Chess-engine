package org.chess.organization;

import java.util.Arrays;

import org.chess.dataTypes.*;
import org.chess.pieces.*;

public class UpdaterMaps {
    
    public static void updateKingPin(ChessBoard board) {
        generateKingPinData(board, board.getKingPin(Colour.BLACK), board.getKing(Colour.BLACK));
        generateKingPinData(board, board.getKingPin(Colour.WHITE), board.getKing(Colour.WHITE));
    }

    public static void updateControlMap(ChessBoard board) {

        Arrays.stream(board.getSquaresControlledBy(Colour.BLACK))
            .forEach(row -> Arrays.fill(row, 0));        

        Arrays.stream(board.getSquaresControlledBy(Colour.WHITE))
            .forEach(row -> Arrays.fill(row, 0)); 

        Arrays.stream(board.getBoard())
            .flatMap(Arrays::stream)
            .filter(p -> p != null)
            .forEach(p -> fillControlMap(board, p));

    }



    private static void generateKingPinData(ChessBoard board, Pin[][] kingPin, King king) {
        if (king == null) {
            return;
        }
        for (int row = 0; row < ChessBoard.BOARD_SIZE; row++) {
            for (int column = 0; column < ChessBoard.BOARD_SIZE; column++) {
                kingPin[row][column] = null;
            }
        }

        int[][] linearDirections = { { 1, 1 }, { -1, 1 }, { -1, -1 }, { 1, -1 }, { 1, 0 }, { -1, 0 }, { 0, 1 }, { 0, -1 } };
        int[][] knightDirections = { { 2, -1 }, { 2, 1 }, { 1, 2 }, { -1, 2 }, { -2, 1 }, { -2, -1 }, { -1, -2 }, { 1, -2 } };

        int kingRow = king.getPosition().row();
        int kingColumn = king.getPosition().column();

        int checks = 0;

        checks = calculateLinearDirectionsForKingPin(board, king.getColour(), linearDirections, kingRow, kingColumn, checks, kingPin);
        checks = calculatePawnDirectionsForKingPin(board, king.getColour(), kingRow, kingColumn, checks, kingPin);
        calculateKnightDirectionsForKingPin(board, king.getColour(), knightDirections, kingRow, kingColumn, checks, kingPin);        
    }

    private static int calculateLinearDirectionsForKingPin(ChessBoard board, Colour colour, int[][] directions, int kingRow, int kingColumn, int checks,
            Pin[][] kingPin) {

        for (int[] d : directions) {
            int targetRow = kingRow + d[0];
            int targetColumn = kingColumn + d[1];
            Piece ownPiece = null;
            while (Position.isInsideBounds(targetRow, targetColumn)) {
                Piece targetPiece = board.getBoard()[targetRow][targetColumn];
                if (targetPiece != null) {
                    if (targetPiece.getColour() == (colour)) {
                        if (ownPiece == null) {
                            ownPiece = targetPiece;
                        } else {
                            break;
                        }
                    } else {
                        boolean isDiagonal = (d[0] * d[1] != 0);
                        boolean validAttacker = (
                            isDiagonal && (targetPiece instanceof Bishop || targetPiece instanceof Queen)) ||
                            (!isDiagonal && (targetPiece instanceof Rook || targetPiece instanceof Queen));

                        if (validAttacker) { 
                            if (ownPiece != null) {
                                kingPin[ownPiece.getPosition().row()][ownPiece.getPosition().column()] = Pin.PINNED;
                            } else {
                                checks += 1;
                                if (checks == 1) {
                                    kingPin[kingRow][kingColumn] = Pin.UNDER_CHECK_LINE;
                                } else if (checks == 2) {
                                    kingPin[kingRow][kingColumn] = Pin.DOUBLE_CHECK;
                                }
                            
                                kingPin[targetRow][targetColumn] = Pin.KING_ATTACKER;
                                markCheckPathForKingPin(kingRow, kingColumn, targetRow, targetColumn, d, kingPin);
                            }
                        }
                        break;
                    }
                }
                targetRow += d[0];
                targetColumn += d[1];
            }
        }
        return checks;
    }

    private static int calculatePawnDirectionsForKingPin(ChessBoard board, Colour colour, int kingRow, int kingColumn, int checks, Pin[][] kingPin) {
        int[][] directions = (colour == Colour.WHITE) ? new int[][] {{1,-1}, {1,1}} : new int[][] {{-1,-1}, {-1,1}};
        
        for (int[] d : directions) {
            int targetRow = kingRow + d[0];
            int targetColumn = kingColumn + d[1];
            
            if (!Position.isInsideBounds(targetRow, targetColumn)) continue;
            
            Piece targetPiece = board.getBoard()[targetRow][targetColumn];
            if (targetPiece instanceof Pawn && targetPiece.getColour() != colour) {
                checks += 1;
                kingPin[kingRow][kingColumn] = (checks == 1) ? Pin.UNDER_CHECK_LINE : Pin.DOUBLE_CHECK;
                kingPin[targetRow][targetColumn] = Pin.KING_ATTACKER;
            }
        }
        return checks;
    }

    private static void calculateKnightDirectionsForKingPin(ChessBoard board, Colour colour, int[][] directions, int kingRow, int kingColumn, int checks, Pin[][] kingPin) {
        for (int[] d : directions) {           
            int targetRow = kingRow + d[0];
            int targetColumn = kingColumn + d[1];

            if (!Position.isInsideBounds(targetRow, targetColumn)) continue;
            
            Piece targetPiece = board.getBoard()[targetRow][targetColumn];
            if (targetPiece instanceof Knight && targetPiece.getColour() != colour) {
                checks += 1;
                kingPin[kingRow][kingColumn] = (checks == 1) ? Pin.UNDER_CHECK_KNIGHT : Pin.DOUBLE_CHECK;
                kingPin[targetRow][targetColumn] = Pin.KING_ATTACKER;
            }
        }
    }

    private static void markCheckPathForKingPin(int startRow, int startCol, int endRow, int endCol, int[] d, Pin[][] kingPin) {
        int row = startRow + d[0];
        int column = startCol + d[1];
        while (row != endRow || column != endCol) {
            kingPin[row][column] = Pin.CHECK_PATH;
            row += d[0];
            column += d[1];
        }
    }

    

    private static void fillControlMap(ChessBoard board, Piece piece) {

        int[][] targetMatrix = piece.getColour().equals(Colour.WHITE) ? board.getSquaresControlledBy(Colour.WHITE) : board.getSquaresControlledBy(Colour.BLACK);
        
        piece.getPotentialMoves(board).stream()
            .forEach(s -> targetMatrix[s.row()][s.column()] += 1);
    }
}
