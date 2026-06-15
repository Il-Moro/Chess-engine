package org.chess;

import org.chess.organization.*;
import org.chess.dataTypes.Colour;
import org.chess.dataTypes.Position;
import org.chess.pieces.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class StateEvaluationTest {

    private ChessBoard board;
    private PlayerAgent whitePlayer;
    private PlayerAgent blackPlayer;
    private StateEvaluation evaluation;

    @BeforeEach
    void setUp() {
        board = new ChessBoard();
        whitePlayer = new PlayerAgent(Colour.WHITE, board,5);
        blackPlayer = new PlayerAgent(Colour.BLACK, board,5);
        evaluation = new StateEvaluation(board, whitePlayer, blackPlayer);
    }

    @Test
    void queenAdvantageShouldIncreaseEvaluation(){
        King whiteKing = new King(new Position(0,0),Colour.WHITE);
        King blackKing = new King(new Position(7,0),Colour.BLACK);
        board.setPiece(whiteKing);
        board.setPiece(blackKing);
        int equalPosition = evaluation.evaluate();
        Queen whiteQueen = new Queen(new Position(0,7),Colour.WHITE);
        board.setPiece(whiteQueen);
        int whiteAdvantage = evaluation.evaluate();
        assertTrue(whiteAdvantage > equalPosition);
    }

    @Test
    void passedPawnShouldBeRewarded() {

        board.setPiece(new King(new Position(0, 0), Colour.WHITE));
        board.setPiece(new King(new Position(7, 7), Colour.BLACK));
        board.setPiece(new Pawn(new Position(4, 4), Colour.WHITE));

        StateEvaluation eval1 =new StateEvaluation(board, whitePlayer, blackPlayer);

        int passedPawnScore = eval1.evaluate();

        board.setPiece(new Pawn(new Position(5, 4), Colour.BLACK));

        StateEvaluation eval2 = new StateEvaluation(board, whitePlayer, blackPlayer);

        int blockedPawnScore = eval2.evaluate();

        assertTrue(passedPawnScore > blockedPawnScore);

    }


    @Test
    void doubledPawnsShouldBePenalized() {

        board.setPiece(new King(new Position(0, 0), Colour.WHITE));
        board.setPiece(new King(new Position(7, 7), Colour.BLACK));
        board.setPiece(new Pawn(new Position(1, 4), Colour.WHITE));
        board.setPiece(new Pawn(new Position(1, 5), Colour.WHITE));

        StateEvaluation eval1 = new StateEvaluation(board, whitePlayer, blackPlayer);

        int normalStructure = eval1.evaluate();


        board.setNull(new Position(1,5));
        board.setPiece(new Pawn(new Position(2, 4), Colour.WHITE));

        StateEvaluation eval2 = new StateEvaluation(board, whitePlayer, blackPlayer);

        int doubledStructure = eval2.evaluate();

        assertTrue(normalStructure > doubledStructure);
    }


    @Test
    void kingWithPawnShieldShouldBeSafer() {

        board.setPiece(new King(new Position(3, 3), Colour.WHITE));
        board.setPiece(new King(new Position(7, 7), Colour.BLACK));

        board.setPiece(new Pawn(new Position(4, 2), Colour.WHITE));
        board.setPiece(new Pawn(new Position(4, 3), Colour.WHITE));
        board.setPiece(new Pawn(new Position(4, 4), Colour.WHITE));

        int protectedKing = new StateEvaluation(board, whitePlayer, blackPlayer).evaluate();

        board.setNull(new Position(4, 2));
        board.setNull(new Position(4, 3));
        board.setNull(new Position(4, 4));

        int exposedKing = new StateEvaluation(board, whitePlayer, blackPlayer).evaluate();

        assertTrue(protectedKing > exposedKing);
    }


    @Test
    void centerControlShouldBeRewarded() {

        board.setPiece(new King(new Position(0, 0), Colour.WHITE));
        board.setPiece(new King(new Position(4,4), Colour.BLACK));
        board.setPiece(new Knight(new Position(3, 3), Colour.WHITE));

        int centerKnight = new StateEvaluation(board, whitePlayer, blackPlayer).evaluate();

        board.setNull(new Position(3, 3));
        board.setPiece(new Knight(new Position(0, 7), Colour.WHITE));

        int cornerKnight = new StateEvaluation(board, whitePlayer, blackPlayer).evaluate();

        assertTrue(centerKnight > cornerKnight);
    }

    @Test
    void piecesNearEnemyKingShouldIncreaseTropism(){

        King whiteKing = new King(new Position(0,4),Colour.WHITE);
        King blackKing = new King(new Position(7,7),Colour.BLACK);
        Queen whiteQueen = new Queen(new Position(0,0),Colour.WHITE);
        
        board.setPiece(whiteKing);
        board.setPiece(blackKing);
        board.setPiece(whiteQueen);
        
        int evalA = evaluation.evaluate();
        assertTrue(board.isMoveLegal(whiteQueen.getPosition(),new Position(6,6)));
        board.physicalMovement(whiteQueen.getPosition(), new Position(6,6));
        int evalB = evaluation.evaluate();
        assertTrue(evalB>evalA);
    }


    @Test
    void evaluationShouldBeSymmetric() {

        ChessBoard boardInit = new ChessBoard(true);

        int whiteScore = new StateEvaluation(boardInit, whitePlayer, blackPlayer).evaluate();

        int blackScore = new StateEvaluation(boardInit, blackPlayer, whitePlayer).evaluate();

        assertEquals(whiteScore-blackScore, 0);
    }
}