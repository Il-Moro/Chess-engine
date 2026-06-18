package org.chess.organization;

import org.chess.dataTypes.Colour;
import org.chess.dataTypes.End;
import org.chess.dataTypes.Position;
import org.chess.pieces.*;

import java.util.Set;


public class StateEvaluation {

    private static final double MATERIAL_WEIGHT = 1.0;
    private static final double MOBILITY_WEIGHT = 0.1;
    private static final double KING_SAFETY_WEIGHT = 0.5;
    private static final double CENTER_CONTROL_WEIGHT = 0.3;
    private static final double PAWN_STRUCTURE_WEIGHT = 0.4;
    private static final double KING_TROPISM_WEIGHT = 0.2;
    
    private static final int PAWN_VALUE = 100;
    private static final int KNIGHT_VALUE = 320;
    private static final int BISHOP_VALUE = 330;
    private static final int ROOK_VALUE = 500;
    private static final int QUEEN_VALUE = 900;
    private static final int KING_VALUE = 10000;


    private static final Position[] CENTER_SQUARES = {
        new Position(3, 3), new Position(3, 4),
        new Position(4, 3), new Position(4, 4)
    };
    
    private static final Position[] EXTENDED_CENTER = {
        new Position(2, 2), new Position(2, 3), new Position(2, 4), new Position(2, 5),
        new Position(3, 2), new Position(3, 3), new Position(3, 4), new Position(3, 5),
        new Position(4, 2), new Position(4, 3), new Position(4, 4), new Position(4, 5),
        new Position(5, 2), new Position(5, 3), new Position(5, 4), new Position(5, 5)
    };


    private ChessBoard board;
    private PlayerAgent player;
    private Player opponent;

    
    public StateEvaluation(ChessBoard board, PlayerAgent player,Player opponent) {
        this.board = board;
        this.player=player;
        this.opponent=opponent;
    }


    public int evaluate() {
        int materialScore = evaluateMaterial();
        int mobilityScore = evaluateMobility();
        int kingSafetyScore = evaluateKingSafety();
        int centerControlScore = evaluateCenterControl();
        int pawnStructureScore = evaluatePawnStructure();
        int kingTropismScore = evaluateKingTropism();
        
        int totalScore = (int)(
            MATERIAL_WEIGHT * materialScore +
            MOBILITY_WEIGHT * mobilityScore +
            KING_SAFETY_WEIGHT * kingSafetyScore +
            CENTER_CONTROL_WEIGHT * centerControlScore +
            PAWN_STRUCTURE_WEIGHT * pawnStructureScore +
            KING_TROPISM_WEIGHT * kingTropismScore
        );
        
        return totalScore;
    }


    private int evaluateMaterial() {
        int whiteMaterial = 0;
        int blackMaterial = 0;
        
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Piece piece = board.getPiece(new Position(row, col));
                if (piece != null) {
                    int value = getPieceValue(piece);
                    if (piece.getColour() == Colour.WHITE) {
                        whiteMaterial += value;
                    } else {
                        blackMaterial += value;
                    }
                }
            }
        }

        if (player.colour == Colour.WHITE) {
            return whiteMaterial - blackMaterial;    
        }
        
        return blackMaterial - whiteMaterial;
    }


    private int getPieceValue(Piece piece) {
        if (piece instanceof Pawn) return PAWN_VALUE;
        if (piece instanceof Knight) return KNIGHT_VALUE;
        if (piece instanceof Bishop) return BISHOP_VALUE;
        if (piece instanceof Rook) return ROOK_VALUE;
        if (piece instanceof Queen) return QUEEN_VALUE;
        if (piece instanceof King) return KING_VALUE;
        return 0;
    }

    private int evaluateMobility() {
        int whiteMobility;
        int blackMobility;

        switch (player.colour) {
            case Colour.WHITE:
                whiteMobility=player.computeAllLegalMoves().size();
                blackMobility=opponent.computeAllLegalMoves().size();
                return whiteMobility - blackMobility;
        
            default:
                blackMobility=player.computeAllLegalMoves().size();
                whiteMobility=opponent.computeAllLegalMoves().size();
                return blackMobility - whiteMobility;
        }
        
    }


    private int evaluateKingSafety() {
        King whiteKing = findKing(Colour.WHITE);
        King blackKing = findKing(Colour.BLACK);
        
        int whiteSafety = calculateKingSafety(whiteKing, Colour.WHITE);
        int blackSafety = calculateKingSafety(blackKing, Colour.BLACK);
        
        if(player.colour == Colour.WHITE)
            return whiteSafety - blackSafety;
        else
            return blackSafety - whiteSafety;
    }

    
    private King findKing(Colour colour) {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Piece piece = board.getPiece(new Position(row, col));
                if (piece instanceof King && piece.getColour() == colour) {
                    return (King) board.getPiece(new Position(row, col));
                }
            }
        }
        return null;
    }

    private int calculateKingSafety(King king, Colour colour) {
        
        if (king == null) {
            return -1000000;
        }

        if(board.isCheckmateOrStalemate(colour) == End.CHECKMATE)
            return -1000000;
        else if(board.isCheckmateOrStalemate(colour) == End.STALEMATE)
            return 0;

        int safety = 0;

        if(!king.getHasMoved())
            safety += 30;

        int exposedPenality = evaluateKingExposure(king, colour);
        safety-=exposedPenality*10;

        int pawnShield = countPawnShield(king, colour);
        safety += pawnShield * 15;
        
        return safety;
    }

    private int evaluateKingExposure(King king, Colour colour){
        int [][] squaresControlledOpponent;
        int attackCount = 0;
        Set<Position> kingMoves;
        
        if (king.getColour()==player.colour) {
            squaresControlledOpponent = board.getSquareControlledBy(opponent.colour);
            kingMoves = king.getPotentialMoves(board);
        }
        else{
            squaresControlledOpponent = board.getSquareControlledBy(player.colour);
            kingMoves = king.getPotentialMoves(board);
        }

        for(Position p:kingMoves){
            attackCount+=squaresControlledOpponent[p.row()][p.column()];
        }

        if(squaresControlledOpponent[king.getPosition().row()][king.getPosition().column()] != 0)
            attackCount +=100;

        return attackCount;
    }

    private int countPawnShield(King king, Colour colour){
        int shieldCount = 0;

        Set<Position> kingMoves;
        kingMoves = king.getPotentialMoves(board);


        for(Position p:kingMoves){
            Piece piece = board.getPiece(p);
            if(piece != null && piece instanceof Pawn)
                shieldCount+=1;
        }

        return shieldCount;
    }


    private int evaluateCenterControl() {


        int whiteControl=countSquares(Colour.WHITE, CENTER_SQUARES);
        int blackControl=countSquares(Colour.BLACK, CENTER_SQUARES);

        whiteControl+=countSquares(Colour.WHITE, EXTENDED_CENTER);
        blackControl+=countSquares(Colour.BLACK, EXTENDED_CENTER);

        if(player.colour==Colour.WHITE)
            return whiteControl-blackControl;

        return blackControl - whiteControl;
    }

    private int countSquares(Colour colour,Position[] square){
        int [][] squaresControlled;
        int sum = 0;
        
        squaresControlled = board.getSquareControlledBy(colour);

        for (Position center : square) {
            sum+=squaresControlled[center.row()][center.column()];
        }
        return sum;
    }


    private int evaluatePawnStructure() {

        int whitePawnScore = evaluatePawnsForColour(Colour.WHITE);
        int blackPawnScore = evaluatePawnsForColour(Colour.BLACK);
        
        if(player.colour == Colour.WHITE)
            return whitePawnScore - blackPawnScore;
        else
            return blackPawnScore -whitePawnScore;
    }
    
    private int evaluatePawnsForColour(Colour colour) {
        int score = 0;
        
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Piece piece = board.getPiece(new Position(row, col));
                if (piece instanceof Pawn && piece.getColour() == colour) {
                    
                    if (isPassedPawn(new Position(row, col), colour)) {
                        score += 20;
                    }
                    
                    if (col == 3 || col == 4) {
                        score += 5;
                    }
                }
            }
        }
        
        int doubledPawns = 0;
        for (int col = 0; col < 8; col++) {
            int pawnCount = 0;
            for (int row = 0; row < 8; row++) {
                Piece piece = board.getPiece(new Position(row, col));
                if (piece instanceof Pawn && piece.getColour() == colour) {
                    pawnCount++;
                }
            }
            if (pawnCount > 1) {
                doubledPawns += (pawnCount - 1);
            }
        }
        score -= doubledPawns * 15;
        
        return score;
    }


    private boolean isPassedPawn(Position pawnPos, Colour colour) {
        int direction = (colour == Colour.WHITE) ? 1 : -1;
        int startRow = pawnPos.row() + direction;
        int endRow = (colour == Colour.WHITE) ? 7 : 0;
        
        for (int row = startRow; row != endRow + direction; row += direction) {
            for (int dc = -1; dc <= 1; dc++) {
                int col = pawnPos.column() + dc;
                if (col >= 0 && col < 8) {
                    Piece piece = board.getPiece(new Position(row, col));
                    if (piece instanceof Pawn && piece.getColour() != colour) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private int evaluateKingTropism() {
        King whiteKing = findKing(Colour.WHITE);
        King blackKing = findKing(Colour.BLACK);
        
        if (whiteKing == null || blackKing == null)
            return 0;

        int whiteTropism = calculateTropism(Colour.WHITE, blackKing.getPosition());
        int blackTropism = calculateTropism(Colour.BLACK, whiteKing.getPosition());
        
        if(player.colour==Colour.WHITE)
            return whiteTropism - blackTropism;
        
        return blackTropism - whiteTropism;
    }




    private int calculateTropism(Colour colour, Position enemyKingPos) {

        if (enemyKingPos == null) return 0;
        
        int tropism = 0;
        
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Piece piece = board.getPiece(new Position(row, col));
                if (piece != null && piece.getColour() == colour) {
                    int distance = calculateDistance(new Position(row, col), enemyKingPos);
                    int bonus = 0;
                    if (piece instanceof Queen)
                        bonus = 20;

                    else if (piece instanceof Knight)
                        bonus = 15;

                    else if (piece instanceof Rook)
                        bonus = 10;

                    else if (piece instanceof Bishop)
                        bonus = 10;

                    else if (piece instanceof Pawn)
                        bonus = 3;
                        
                    tropism += bonus * (14 - distance);
                }
            }
        }
        
        return tropism;
    }

    
    private int calculateDistance(Position p1, Position p2) {
        return Math.abs(p1.row() - p2.row()) + Math.abs(p1.column() - p2.column());
    }
    

}