package org.chess.organization;

import org.chess.dataTypes.Colour;
import org.chess.dataTypes.End;
import java.util.ArrayList;
import java.util.List;
import org.chess.dataTypes.Move;
import org.chess.dataTypes.Position;
import org.chess.pieces.Piece;

public class Match {
    private final ChessBoard board;
    private final Player whitePlayer;
    private final Player blackPlayer;
    private List<Move> moveHistory;
    private Player currentPlayerTurn;
    private End state;

    // Costruttore
    public Match(Player white, Player black) {
        
        this.whitePlayer = white;
        this.blackPlayer = black;
        this.board = new ChessBoard();
        this.currentPlayerTurn = white;          
        this.state = End.IN_PROGRESS;
        this.moveHistory = new ArrayList<>();
    }

    // TODO: public update move list
    // TODO: public Player getCurrentPlayerTurn(){}
    // TODO : public void setCurrentPlayerTurn(){}


}
