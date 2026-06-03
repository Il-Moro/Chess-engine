package org.chess;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.chess.dataTypes.Colour;
import org.chess.dataTypes.Move;
import org.chess.dataTypes.Position;
import org.chess.organization.ChessBoard;
import org.chess.organization.Node;
import org.chess.pieces.King;
import org.chess.pieces.Pawn;

class NodeTest {

    // -------------------------------------------------------------------------
    // Constructor tests
    // -------------------------------------------------------------------------

    @Test
    void testConstructor_rootNode() {
        Node root=new Node(new ChessBoard(), true, null,null);
        assertNotNull(root);
    }

    @Test
    void testConstructor_childNode() {
        Node root=new Node(new ChessBoard(), true, null,null);
        Pawn pawn=new Pawn(new Position(6,5),Colour.WHITE);
        Node childNode=new Node(new ChessBoard(), false, root,new Move(pawn,new Position(5,5)));
        assertNotNull(childNode);
    }

    @Test
    void testConstructor_nullBoard() {
           assertDoesNotThrow(() -> new Node(null, true, null, null));
    }

    // -------------------------------------------------------------------------
    // Placeholder: generateChildren()
    // -------------------------------------------------------------------------

    @Test
    void testGenerateChildren_placeholder() {
        ChessBoard board=new ChessBoard();
        board.setPiece(new King(new Position(1,4),Colour.BLACK));
        board.setPiece(new King(new Position(7,5),Colour.WHITE));
        Node rootNode=new Node(board, true);
        List<Node> children=rootNode.generateChildren();
        assertEquals(5, children.size());
        
    }

    // -------------------------------------------------------------------------
    // Placeholder: isLeaf()
    // -------------------------------------------------------------------------

    @Test
    void testIsLeaf_noChildren() {
        Node root=new Node(new ChessBoard(), true, null,null);
        assertTrue(root.isLeaf());
    }

    @Test
    void testIsLeaf_withChildren() {
    }

    // -------------------------------------------------------------------------
    // Placeholder: isTerminal()
    // -------------------------------------------------------------------------

    @Test
    void testIsTerminal_terminalState() {
        // Terminal for white

    }

    @Test
    void testIsTerminal_nonTerminalState() {
    }

    // -------------------------------------------------------------------------
    // Placeholder: getPath()
    // -------------------------------------------------------------------------

    @Test
    void testGetPath_rootNode() {
    }

    @Test
    void testGetPath_childNode() {
    }

    @Test
    void testGetPath_grandchildNode() {
    }

    // -------------------------------------------------------------------------
    // Tree structure
    // -------------------------------------------------------------------------

    @Test
    void testTree_parentChildRelationship() {
    }

    @Test
    void testTree_rootHasNoParent() {
    }

    @Test
    void testTree_moveAppliedStored() {
    }
}