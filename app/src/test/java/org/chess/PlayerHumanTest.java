package org.chess;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;

import org.chess.dataTypes.Colour;
import org.chess.dataTypes.Move;
import org.chess.dataTypes.Position;
import org.chess.organization.ChessBoard;
import org.chess.organization.PlayerHuman;
import org.chess.pieces.King;
import org.chess.pieces.Piece;



public class PlayerHumanTest {

    private ChessBoard board;
    private PlayerHuman player;
    private Piece whiteKing;
    private Piece blackKing;
    @BeforeEach
    void setUp() {
        board = new ChessBoard();  // Assumendo che inizializzi la scacchiera standard
        player = new PlayerHuman(Colour.WHITE, board);
        whiteKing=new King(new Position(0,0), Colour.WHITE);
        blackKing=new King(new Position(7,7), Colour.BLACK);
        board.setPiece(whiteKing);
        board.setPiece(blackKing);
    }
    

    @Test
    void validMove(){
        Position to = new Position(1, 0);
        player.setMove(whiteKing,to);
        Move move = player.decideMove();
        assertNotSame(Move.INVALID, move);
        assertEquals(whiteKing, move.selectedPiece());
        assertEquals(to, move.to());
    }

    @Test
    void invalidMove(){
        Position to = new Position(2, 0);
        player.setMove(whiteKing,to);
        Move move = player.decideMove();
        assertEquals(Move.INVALID, move);
    }


}
