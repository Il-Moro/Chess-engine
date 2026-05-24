package org.chess.dataTypes;

public enum Pin {
    PINNED, // indica un pezzo pin
    CHECK_PATH, // case tra re e attaccante
    KING_ATTACKER, // pezzo che attacca il re
    UNDER_CHECK, // sulla casa del re, indica se è in scacco
    DOUBLE_UNDER_CHECK
}