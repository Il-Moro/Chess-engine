package org.chess.organization;
import org.chess.pieces.Piece;

import java.util.List;

import org.chess.dataTypes.Move;

public class Node {
    private final ChessBoard board;
    private final boolean isWhite;
    private final Node parent;
    private List<Node> children;
    private final Move moveApplied;

    // Root Node
    public Node(ChessBoard board, boolean isWhite) {
        this(board, isWhite, null, null);
    }

    // Child Node
    public Node(ChessBoard board, boolean isWhite, Node parent, Move moveApplied) {
        this.board = board;
        this.isWhite = isWhite;
        this.parent = parent;
        this.moveApplied = moveApplied;
    }


    // TODO: public List<Node> generateChildren() {}
    // public boolean isLeaf(){}
    // public boolean isTerminal(){}
    // public int getPathToRoot() {}

}
