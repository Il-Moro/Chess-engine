package org.chess.organization;
import org.chess.pieces.Piece;

import java.util.List;

import org.chess.dataTypes.Move;

public class Node {
    private ChessBoard board;
    public Piece [][] state;
    public boolean isWhiteTurn;
    public Node parent;
    public List<Node> children;
    public Move moveApplied;

    public Node(Piece [][] state, boolean isWhiteTurn, Node parent, Move moveApplied){
        this.state=state;
        this.isWhiteTurn=isWhiteTurn;
        this.parent=parent;
        this.moveApplied=moveApplied;
    }


    // TODO: public List<Node> generateChildren() { }
    // TODO: public List<Move> getLegalMoves() {  }


}
