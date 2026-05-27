package org.chess.dataTypes;

public enum Pin {
    PINNED,  // Inchiodato su Riga o Colonna (Torre/Regina)
    CHECK_PATH,         // Casella vuota nella linea di scacco
    KING_ATTACKER,      // Il pezzo che sta dando scacco
    UNDER_CHECK_LINE,   // Re sotto scacco da pezzo lineare (Rook/Bishop/Queen)
    UNDER_CHECK_KNIGHT, // Re sotto scacco da Cavallo
    DOUBLE_CHECK        // Re sotto scacco multiplo
}