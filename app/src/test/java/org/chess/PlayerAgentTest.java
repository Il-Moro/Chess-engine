package org.chess;


import org.chess.organization.PlayerAgent;
import org.chess.organization.ChessBoard;
import org.chess.dataTypes.Colour;
import org.chess.dataTypes.Move;
import org.chess.dataTypes.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class PlayerAgentTest {
    
    private ChessBoard board;
    private PlayerAgent whiteAgent;
    private PlayerAgent blackAgent;

    @BeforeEach
    void setUp() {
        board = new ChessBoard(true);
        whiteAgent = new PlayerAgent(Colour.WHITE, board,5);
        blackAgent = new PlayerAgent(Colour.BLACK, board,5);
    }

    @Test
    void computeAllLegalMoves_ShouldReturnAllMovesForWhiteAtStart() {
        List<Move> moves = whiteAgent.computeAllLegalMoves();
        
        assertNotNull(moves);
        assertEquals(20, moves.size());

        for (Move move : moves) {
            assertEquals(Colour.WHITE, move.selectedPiece().getColour());
        }
    }

    @Test
    void computeAllLegalMoves_ShouldReturnAllMovesForBlackAtStart() {
        List<Move> moves = blackAgent.computeAllLegalMoves();
        
        assertNotNull(moves);
        assertEquals(20, moves.size());
        
        for (Move move : moves) {
            assertEquals(Colour.BLACK, move.selectedPiece().getColour());
        }
    }

    @Test
    void computeAllLegalMoves_ShouldReturnEmptyListWhenNoPieces() {

        ChessBoard emptyBoard = new ChessBoard();
        
        PlayerAgent agent = new PlayerAgent(Colour.WHITE, emptyBoard,5);
        List<Move> moves = agent.computeAllLegalMoves();
        
        assertNotNull(moves);
        assertTrue(moves.isEmpty());
    }

    @Test
    void computeAllLegalMoves_ShouldNotContainMove_AfterMoving() {

        Position pawnStart = new Position(1, 0);
        Position pawnEnd = new Position(3, 0);
        board.physicalMovement(pawnStart, pawnEnd);
        
        List<Move> moves = whiteAgent.computeAllLegalMoves();
        
        boolean found = moves.stream()
            .anyMatch(m -> m.to().equals(pawnEnd));
        
        assertFalse(found);
    }

    @Test
    void computeAllLegalMoves_ShouldNotContainIllegalMoves() {
        
        List<Move> moves = whiteAgent.computeAllLegalMoves();

        for (Move move : moves) {
            assertTrue(board.isMoveLegal(move.selectedPiece().getPosition(), move.to()));
        }
    }

    @Test
    void computeAllLegalMoves_ShouldOnlyReturnMovesForCurrentPlayer() {
        List<Move> whiteMoves = whiteAgent.computeAllLegalMoves();
        List<Move> blackMoves = blackAgent.computeAllLegalMoves();

        for (Move move : whiteMoves) {
            assertEquals(Colour.WHITE, move.selectedPiece().getColour());
        }

        for (Move move : blackMoves) {
            assertEquals(Colour.BLACK, move.selectedPiece().getColour());
        }
    }

    @Test
    void computeAllLegalMoves_ShouldUpdateAfterMove() {
        int initialMoves = whiteAgent.computeAllLegalMoves().size();

        Position from = new Position(1, 4);
        Position to = new Position(2, 4);
        board.physicalMovement(from, to);
        
        int movesAfter = whiteAgent.computeAllLegalMoves().size();
        
        assertNotEquals(initialMoves, movesAfter);
    }
}
