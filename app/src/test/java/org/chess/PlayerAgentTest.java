package org.chess;
 
import org.chess.dataTypes.Colour;
import org.chess.dataTypes.End;
import org.chess.dataTypes.Move;
import org.chess.dataTypes.Position;
import org.chess.organization.ChessBoard;
import org.chess.organization.PlayerAgent;
import org.chess.organization.PlayerHuman;
import org.chess.pieces.King;
import org.chess.pieces.Knight;
import org.chess.pieces.Piece;
import org.chess.pieces.Queen;
import org.chess.pieces.Rook;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
 
import static org.junit.jupiter.api.Assertions.*;

public class PlayerAgentTest {  
    
    private ChessBoard board;
    private PlayerAgent agentWhite;
    private PlayerHuman opponentBlack;
 
    @BeforeEach
    public void setUp() {
        board        = new ChessBoard(true); // Inizializza la scacchiera con la configurazione standard
        opponentBlack = new PlayerHuman(Colour.BLACK, board);
        agentWhite    = new PlayerAgent(Colour.WHITE, board, 2, opponentBlack);
    }

    private String boardSnapshot(ChessBoard b) {
        StringBuilder sb = new StringBuilder();
        Piece[][] grid = b.getBoard();
        for (int r = 0; r < ChessBoard.BOARD_SIZE; r++) {
            for (int c = 0; c < ChessBoard.BOARD_SIZE; c++) {
                Piece p = grid[r][c];
                if (p == null) {
                    sb.append("null");
                } else {
                    // classe + colore + posizione → identificatore univoco del pezzo
                    sb.append(p.getClass().getSimpleName())
                      .append(p.getColour())
                      .append(p.getPosition().row())
                      .append(p.getPosition().column());
                }
                sb.append('|');
            }
        }
        return sb.toString();
    }

    @Test
    public void testDecideMove_returnsNonNullFromStartingPosition() {
        Move move = agentWhite.decideMove();
        assertTrue(move != null);
    }

    @Test
    public void testDecideMove_moveHasSelectedPiece() {
        Move move = agentWhite.decideMove();
        assertTrue(move.selectedPiece() != null);
    }

    @Test
    public void testDecideMove_moveHasDestination() {
        Move move = agentWhite.decideMove();
        assertTrue(move.to() != null);
    }

    @Test
    public void testDecideMove_isDeterministic() {
        Move first  = agentWhite.decideMove();
        Move second = agentWhite.decideMove();
        assertEquals(first, second);
    }

    @Test
    public void testDecideMove_depth1_returnsMove() {
        PlayerAgent shallowAgent = new PlayerAgent(Colour.WHITE, board, 1, opponentBlack);
        assertNotNull(shallowAgent.decideMove());
    }

    @Test
    public void testDecideMove_depth3_returnsMove() {
        PlayerAgent deepAgent = new PlayerAgent(Colour.WHITE, board, 3, opponentBlack);
        assertNotNull(deepAgent.decideMove());
    }

    @Test
    public void testDecideMove_differentDepthsBothValid() {
        PlayerAgent shallow = new PlayerAgent(Colour.WHITE, board, 1, opponentBlack);
        PlayerAgent deep    = new PlayerAgent(Colour.WHITE, board, 3, opponentBlack);
        assertNotNull(shallow.decideMove());
        assertNotNull(deep.decideMove());
    }

    @Test
    public void testDecideMove_doesNotAlterBoardState() {
        String before = boardSnapshot(board);
        agentWhite.decideMove();
        String after  = boardSnapshot(board);
        assertEquals(before, after);
    }

    @Test
    public void testDecideMove_depth1_doesNotAlterBoard() {
        PlayerAgent shallow = new PlayerAgent(Colour.WHITE, board, 1, opponentBlack);
        String before = boardSnapshot(board);
        shallow.decideMove();
        assertEquals(before, boardSnapshot(board));
    }
 
    @Test
    public void testDecideMove_depth3_doesNotAlterBoard() {
        PlayerAgent deep = new PlayerAgent(Colour.WHITE, board, 3, opponentBlack);
        String before = boardSnapshot(board);
        deep.decideMove();
        assertEquals(before, boardSnapshot(board));
    }

    @Test
    public void testDecideMove_returnsMoveAmongLegalMoves() {
        ChessBoard customBoard = new ChessBoard();
        customBoard.setPiece(new King(new Position(0, 4), Colour.WHITE, false));
        customBoard.setPiece(new King(new Position(7, 4), Colour.BLACK, false));
        customBoard.setPiece(new Knight(new Position(0, 1), Colour.WHITE));
        customBoard.updateControl();
        customBoard.updateKingPin();
 
        PlayerHuman blackPlayer = new PlayerHuman(Colour.BLACK, customBoard);
        PlayerAgent whiteAgent  = new PlayerAgent(Colour.WHITE, customBoard, 2, blackPlayer);
 
        Move chosen = whiteAgent.decideMove();
        assertNotNull(chosen);
 
        assertTrue(customBoard.isMoveLegal(chosen.selectedPiece().getPosition(), chosen.to()));
    }

    @Test
    public void testDecideMove_blackAgent_returnsValidMove() {
        PlayerAgent agentBlack = new PlayerAgent(Colour.BLACK, board, 2, agentWhite);
        Move move = agentBlack.decideMove();
        assertNotNull(move);
        assertEquals(Colour.BLACK, move.selectedPiece().getColour());
    }
 
    @Test
    public void testDecideMove_blackAgent_doesNotAlterBoard() {
        PlayerAgent agentBlack = new PlayerAgent(Colour.BLACK, board, 2, agentWhite);
        String before = boardSnapshot(board);
        agentBlack.decideMove();
        assertEquals(before, boardSnapshot(board));
    }

    @Test
    public void testDecideMove_repeatedCalls_boardAlwaysRestored() {
        String initial = boardSnapshot(board);
        for (int i = 0; i < 3; i++) {
            agentWhite.decideMove();
            String afterCall = boardSnapshot(board);
            assertEquals(initial,afterCall);
        }
    }

    @Test
    public void testDecideMove_choosesCheckmate_whenMateInOne1() {
        ChessBoard mateBoard = new ChessBoard();
 
        King  whiteKing  = new King (new Position(5, 2), Colour.WHITE, true);
        Queen whiteQueen = new Queen(new Position(5, 0), Colour.WHITE);

        King  blackKing  = new King (new Position(7, 0), Colour.BLACK, true);
 
        mateBoard.setPiece(whiteKing);
        mateBoard.setPiece(whiteQueen);
        mateBoard.setPiece(blackKing);
 
        PlayerHuman blackPlayer = new PlayerHuman(Colour.BLACK, mateBoard);
        PlayerAgent whiteAgent  = new PlayerAgent(Colour.WHITE, mateBoard, 2, blackPlayer);
 
        Move chosen = whiteAgent.decideMove();
        System.out.println(">>> MOSSA SCELTA: " + chosen);
        
        assertNotNull(chosen);
 
        mateBoard.physicalMovement(chosen.selectedPiece().getPosition(), chosen.to());
 
        End result = mateBoard.isCheckmateOrStalemate(Colour.BLACK);
        assertEquals(End.CHECKMATE, result);
    }

    @Test
    public void testDecideMove_choosesCheckmate_whenMateInOne2() {
        ChessBoard mateBoard = new ChessBoard();
 
        King  whiteKing  = new King (new Position(7, 7), Colour.WHITE, true);
        Queen whiteQueen = new Queen(new Position(3, 2), Colour.WHITE);
        Rook  whiteRook  = new Rook (new Position(1, 1), Colour.WHITE);

        King  blackKing  = new King (new Position(7, 0), Colour.BLACK, true);
 
        mateBoard.setPiece(whiteKing);
        mateBoard.setPiece(whiteQueen);
        mateBoard.setPiece(whiteRook);
        mateBoard.setPiece(blackKing);
 
        PlayerHuman blackPlayer = new PlayerHuman(Colour.BLACK, mateBoard);
        PlayerAgent whiteAgent  = new PlayerAgent(Colour.WHITE, mateBoard, 2, blackPlayer);
 
        Move chosen = whiteAgent.decideMove();
        System.out.println(">>> MOSSA SCELTA: " + chosen);
        
        assertNotNull(chosen);
 
        mateBoard.physicalMovement(chosen.selectedPiece().getPosition(), chosen.to());
 
        End result = mateBoard.isCheckmateOrStalemate(Colour.BLACK);
        assertEquals(End.CHECKMATE, result);
    }

}