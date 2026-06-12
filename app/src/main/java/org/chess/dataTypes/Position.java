package org.chess.dataTypes;

import org.chess.organization.ChessBoard;

public record Position(int row, int column){
    public static boolean isInsideBounds(int row, int col) {
        return 0 <= row && row < ChessBoard.BOARD_SIZE && 0 <= col && col < ChessBoard.BOARD_SIZE;
    }
}
