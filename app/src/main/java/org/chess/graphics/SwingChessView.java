package org.chess.graphics;

import org.chess.dataTypes.Colour;
import org.chess.pieces.*;

import javax.swing.*;
import java.awt.*;

import java.net.URL;


public class SwingChessView  implements ChessView {
        
    private ChessController controller;
    private JFrame frame;
    private JPanel setupPanel;
    private String selectedMode="";
    private String selectedColor="";
    private String selectedDifficulty ="";
    private String playerTurn = selectedColor;
    private boolean humanVsAi = false;
    private CardLayout cardLayout = new CardLayout();
    private JButton[][] squares = new JButton[8][8];
    private boolean selectedPiece = false;
    private int selectedRow = -1;
    private int selectedColumn = -1;

    public SwingChessView() {
        initFrame();
        setupScreen();
        show();
    }



    private void initFrame() {
        frame = new JFrame("Chess Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 800);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());
    }

    
    @Override
    public void setController(ChessController controller) {
        this.controller = controller;
    }


    @Override
    public void setupScreen() {
        
        setupPanel = new JPanel(cardLayout);
        setupPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // MODE
        JLabel titleMode = new JLabel("Select Game Mode", SwingConstants.CENTER);
        titleMode.setFont(new Font("Arial", Font.BOLD, 50));
        JPanel modePanelContainer = new JPanel();
        modePanelContainer.setLayout(new BorderLayout());
        JPanel modePanel = new JPanel();
        modePanel.setLayout(new GridLayout(1, 2, 10, 10));
        modePanelContainer.add(titleMode,BorderLayout.NORTH);
        modePanelContainer.add(modePanel, BorderLayout.CENTER);

        JButton humanVsAi = new JButton("Human vs AI");
        JButton humanVsHuman = new JButton("Human vs Human");

        // Styling
        buttonStyling(humanVsAi);
        buttonStyling(humanVsHuman);



        modePanel.add(humanVsAi);
        modePanel.add(humanVsHuman);

        humanVsAi.addActionListener(e-> {
            selectedMode = humanVsAi.getText();
            this.humanVsAi = true;
            cardLayout.show(setupPanel, "Color selection");
            
        });
        humanVsHuman.addActionListener(e -> {
            selectedMode = humanVsHuman.getText();
            cardLayout.show(setupPanel, "Color selection");
            
        });

        // ONLY COLOR

        JLabel titleColor = new JLabel("Select Color", SwingConstants.CENTER);
        titleColor.setFont(new Font("Arial", Font.BOLD, 50));
        JPanel colorPanelContainer = new JPanel();
        colorPanelContainer.setLayout(new BorderLayout());
        JPanel colorPanel = new JPanel();
        colorPanel.setLayout(new GridLayout(1, 2, 10, 10));
        colorPanelContainer.add(titleColor,BorderLayout.NORTH);
        colorPanelContainer.add(colorPanel,BorderLayout.CENTER);

        JButton white = new JButton("WHITE");
        JButton black = new JButton("BLACK");

        // Styling

        buttonStyling(white);
        buttonStyling(black);

        colorPanel.add(white);
        colorPanel.add(black);

        white.addActionListener(e-> {
            selectedColor = white.getText();
            if (this.humanVsAi) {
                cardLayout.show(setupPanel, "Difficulty selection");
            }
            else if (controller != null) {
                controller.startGame(selectedMode, selectedColor,selectedDifficulty);
            }
            
        });
        black.addActionListener(e -> {
            selectedColor = black.getText();
            if (this.humanVsAi) {
                cardLayout.show(setupPanel, "Difficulty selection");
            }
            else if (controller != null) {
                controller.startGame(selectedMode, selectedColor,selectedDifficulty);
            }
        });

        // DIFFICULTY

        JLabel titleDifficulty = new JLabel("Select Difficulty", SwingConstants.CENTER);
        titleDifficulty.setFont(new Font("Arial", Font.BOLD, 50));
        JPanel difficultyContainer = new JPanel();
        difficultyContainer.setLayout(new BorderLayout());
        JPanel difficuPanel = new JPanel();
        difficuPanel.setLayout(new GridLayout(1, 3, 10, 10));
        difficultyContainer.add(titleDifficulty,BorderLayout.NORTH);
        difficultyContainer.add(difficuPanel,BorderLayout.CENTER);

        JButton easy = new JButton("EASY");
        JButton normal = new JButton("NORMAL");
        JButton hard = new JButton("HARD");

        difficuPanel.add(easy);
        difficuPanel.add(normal);
        difficuPanel.add(hard);

        easy.addActionListener(e-> {
            selectedDifficulty = easy.getText();
            if (controller != null) {
                controller.startGame(selectedMode, selectedColor,selectedDifficulty);
            }
        });

        normal.addActionListener(e -> {
            selectedDifficulty = normal.getText();
            if (controller != null) {
                controller.startGame(selectedMode, selectedColor,selectedDifficulty);
            }
        });

        hard.addActionListener(e -> {
            selectedDifficulty = hard.getText();
            if (controller != null) {
                controller.startGame(selectedMode, selectedColor,selectedDifficulty);
            }      
        });


        // Styling

        buttonStyling(easy);
        buttonStyling(normal);
        buttonStyling(hard);

        setupPanel.add(modePanelContainer, "ModeSelection");
        setupPanel.add(colorPanelContainer, "Color selection");
        setupPanel.add(difficultyContainer, "Difficulty selection");

        frame.add(setupPanel, BorderLayout.CENTER);

        frame.revalidate();
        frame.repaint();
    }

    private void buttonStyling(JButton button){
        button.setFont(new Font("Arial", Font.BOLD, 20));
        button.setBackground(new Color(52, 73, 94));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(15, 30, 15, 30));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    @Override
    public void gameScreen() {
        JPanel boardPanel = new JPanel(new GridLayout(8, 8));

        frame.add(boardPanel);
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {

                squares[row][col] = new JButton();
                squares[row][col].setPreferredSize(new Dimension(60, 60));
                
                if ((row + col) % 2 == 0) {
                    squares[row][col].setBackground(Color.WHITE);
                    squares[row][col].setName("WHITE");
                } else {
                    squares[row][col].setBackground(new Color(100, 150, 80));
                    squares[row][col].setName("BLACK");
                }

                squares[row][col].setOpaque(true);
                squares[row][col].setBorderPainted(false);
                boardPanel.add(squares[row][col]);
                
                final int r = row;
                final int c = col;
                squares[row][col].addActionListener(e -> handleSquareClick(r, c));
            }
        }
        setupPanel.add(boardPanel,"Chessboard");
        cardLayout.show(setupPanel, "Chessboard");
    }

    private void handleSquareClick(int row, int col) {
        
        int actualRow = selectedColor.equals("BLACK") ? row : (7 - row);

        if(selectedMode.equals("Human vs AI")) {
            if(!playerTurn.equals(selectedColor)) {
                controller.agentTurn();
                return;
            }
        }

        if (!selectedPiece) {
            
            if (squares[row][col].getIcon() == null) return;
            
            
            if (!squares[row][col].getName().equals(playerTurn)) return;
            
            selectedPiece = true;
            selectedRow = row;
            selectedColumn = col;
            
            controller.onSquareSelected(actualRow, col);

        } else {
            // Se riclicchi la stessa casella, deseleziona
            if (row == selectedRow && col == selectedColumn) {
                deselect();
                return;
            }
            
            // Se clicchi un altro tuo pezzo dello stesso colore, cambia selezione
            if (squares[row][col].getIcon() != null && squares[row][col].getName().equals(playerTurn)) {
                deselect();
                selectedPiece = true;
                selectedRow = row;
                selectedColumn = col;
                controller.onSquareSelected(actualRow, col);
                return;
            }

            // Calcola la riga di partenza reale nel modello
            int actualFromRow = selectedColor.equals("BLACK") ? selectedRow : (7 - selectedRow);

            // Invia il tentativo di mossa con le coordinate reali del modello
            controller.onMoveAttempt(actualFromRow, selectedColumn, actualRow, col);
            deselect();
        }
    }

    private void deselect() {
        selectedPiece = false;
        selectedRow = -1;
        selectedColumn = -1;
        controller.clearAllHighlights();
    }


    @Override
    public void displayBoard(Piece[][] board) {
        if (selectedColor.equals("BLACK")) {
            this.displayBlack(board);
        }
        else{
            this.displayWhite(board);
        }
        
    }

    private void displayBlack(Piece[][] board){
        ImageIcon icon = null;
        for(int row = 0; row< 8; row++){
            for(int column = 0; column< 8; column++){

                if(board[row][column] instanceof Pawn){
                    icon = board[row][column].getColour() == Colour.WHITE
                        ? loadIcon("whitePawn.png")
                        : loadIcon("blackPawn.png");
                }
                else if(board[row][column] instanceof Bishop){
                    icon = board[row][column].getColour() == Colour.WHITE
                            ? loadIcon("whiteBishop.png")
                            : loadIcon("blackBishop.png");
                }
                else if(board[row][column] instanceof King){
                    icon = board[row][column].getColour() == Colour.WHITE
                            ? loadIcon("whiteKing.png")
                            : loadIcon("blackKing.png");
                }
                else if(board[row][column] instanceof Knight){
                    icon = board[row][column].getColour() == Colour.WHITE
                            ? loadIcon("whiteKnight.png")
                            : loadIcon("blackKnight.png");
                }
                else if(board[row][column] instanceof Queen){
                    icon = board[row][column].getColour() == Colour.WHITE
                            ? loadIcon("whiteQueen.png")
                            : loadIcon("blackQueen.png");
                }
                else if(board[row][column] instanceof Rook){
                    icon = board[row][column].getColour() == Colour.WHITE
                            ? loadIcon("whiteRook.png")
                            : loadIcon("blackRook.png");
                }
                else{
                    icon = null;
                }

                if (icon != null) {
                    squares[row][column].setIcon(icon);
                    if (board[row][column].getColour() == Colour.WHITE)
                        squares[row][column].setName("WHITE");
                    else
                        squares[row][column].setName("BLACK");
                } else {
                    // Svuota il bottone grafico se nel modello non c'è più il pezzo
                    squares[row][column].setIcon(null);
                    squares[row][column].setName("EMPTY");
                }
            }   
        }
    }

    private void displayWhite(Piece[][] board){
        ImageIcon icon = null;
        for(int row = 0; row< 8; row++){
            for(int column = 0; column< 8; column++){
                int modelRow = 7 - row;
                if(board[modelRow][column] instanceof Pawn){
                    icon = board[modelRow][column].getColour() == Colour.WHITE
                        ? loadIcon("whitePawn.png")
                        : loadIcon("blackPawn.png");
                }
                else if(board[modelRow][column] instanceof Bishop){
                    icon = board[modelRow][column].getColour() == Colour.WHITE
                            ? loadIcon("whiteBishop.png")
                            : loadIcon("blackBishop.png");
                }
                else if(board[modelRow][column] instanceof King){
                    icon = board[modelRow][column].getColour() == Colour.WHITE
                            ? loadIcon("whiteKing.png")
                            : loadIcon("blackKing.png");
                }
                else if(board[modelRow][column] instanceof Knight){
                    icon = board[modelRow][column].getColour() == Colour.WHITE
                            ? loadIcon("whiteKnight.png")
                            : loadIcon("blackKnight.png");
                }
                else if(board[modelRow][column] instanceof Queen){
                    icon = board[modelRow][column].getColour() == Colour.WHITE
                            ? loadIcon("whiteQueen.png")
                            : loadIcon("blackQueen.png");
                }
                else if(board[modelRow][column] instanceof Rook){
                    icon = board[modelRow][column].getColour() == Colour.WHITE
                            ? loadIcon("whiteRook.png")
                            : loadIcon("blackRook.png");
                }
                else{
                    icon = null;
                }

                if (icon != null) {
                    squares[row][column].setIcon(icon);
                    if (board[modelRow][column].getColour() == Colour.WHITE)
                        squares[row][column].setName("WHITE");
                    else
                        squares[row][column].setName("BLACK");
                } else {
                    // Se la casella nel modello è vuota, svuota il bottone e resetta il Name
                    squares[row][column].setIcon(null);
                    squares[row][column].setName("EMPTY"); 
                }

            }
        }
    }



    private ImageIcon loadIcon(String fileName) {
        URL url = getClass().getResource("/org/chess/graphics/png/" + fileName);

        if (url == null) {
            System.out.println("Immagine non trovata: /graphics/png/" + fileName);
            return null;
        }
        
        ImageIcon original = new ImageIcon(url);

        Image scaled = original.getImage().getScaledInstance(70, 70, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaled);
        
        return scaledIcon;
    }

    @Override
    public void highlightSquares(int row, int col){
        squares[row][col].setBackground(new Color(100, 180, 255));
        squares[row][col].setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        squares[row][col].setBorderPainted(true);
    }

    @Override
    public void clearHighlights(int row, int col) {
        if ((row + col) % 2 == 0) {
            squares[row][col].setBackground(Color.WHITE);
        } else {
            squares[row][col].setBackground(new Color(100, 150, 80));
        }
        squares[row][col].setBorderPainted(false);
    }

    @Override
    public void showPromotionDialog(String colour, int row, int col) {

        JDialog dialog = new JDialog(frame, "Promozione", true);
        dialog.setLayout(new GridLayout(1, 4, 10, 10));
        dialog.setSize(400, 120);
        dialog.setLocationRelativeTo(frame);
        dialog.setUndecorated(true);

        String[] pieces = {"Queen", "Rook", "Bishop", "Knight"};

        for (String piece : pieces) {
            String iconName = colour.equals("WHITE") 
                ? "white" + piece + ".png" 
                : "black" + piece + ".png";

            JButton btn = new JButton(loadIcon(iconName));
            btn.setName(piece);
            buttonStyling(btn);
            btn.addActionListener(e -> {
                controller.onPromotion(btn.getName());
                dialog.dispose();
            });
            dialog.add(btn);
        }
        dialog.setVisible(true);
    }

    @Override
    public void gameover(String result) {
        String message = result.equals("CHECKMATE") ? "Scacco Matto!" : "Stallo!";

        JDialog dialog = new JDialog(frame, "Fine Partita", true);
        dialog.setLayout(new BorderLayout(10, 10));
        dialog.setSize(350, 160);
        dialog.setLocationRelativeTo(frame);
        dialog.setUndecorated(true);

        JLabel label = new JLabel(message, SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 32));
        label.setForeground(Color.WHITE);
        label.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));

        JButton btn = new JButton("Chiudi");
        buttonStyling(btn);
        btn.addActionListener(e -> {
            dialog.dispose();
            System.exit(0);
        });

        JPanel btnPanel = new JPanel();
        btnPanel.setBackground(new Color(52, 73, 94));
        btnPanel.add(btn);

        dialog.getContentPane().setBackground(new Color(52, 73, 94));
        dialog.add(label, BorderLayout.CENTER);
        dialog.add(btnPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    @Override
    public void setPlayerTurn(String playerTurn){
        this.playerTurn=playerTurn;
    }

    @Override
    public void show() {
        frame.setVisible(true);
    }

}