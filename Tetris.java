import javax.swing.*;
import javax.swing.border.TitledBorder;

import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class Tetris extends JFrame {
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Tetris game = new Tetris();
            game.setVisible(true);
        });
    }
    
    // Removed duplicate constructor
    
    private static final int BOARD_WIDTH = 10;
    private static final int BOARD_HEIGHT = 20;
    private static final int BLOCK_SIZE = 30;
    private static final int BORDER_WIDTH = 5;
    private static final int HOLD_SIZE = 4;
    
    private final GamePanel gamePanel;
    private final HoldPanel holdPanel;
    private final NextPanel nextPanel;
    private final JLabel scoreLabel;
    private final JLabel levelLabel;
    private final JLabel linesLabel;
    
    private Timer timer;
    private int delay = 500; // milliseconds
    private boolean isPaused = false;
    
    private int score = 0;
    private int level = 1;
    private int linesCleared = 0;
    
    public Tetris() {
        setTitle("Tetris");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.setBackground(new Color(40, 40, 40));
        
        gamePanel = new GamePanel();
        holdPanel = new HoldPanel();
        nextPanel = new NextPanel();
        
        JPanel sidePanel = new JPanel(new GridLayout(4, 1, 0, 10));
        sidePanel.setBackground(new Color(40, 40, 40));
        
        scoreLabel = new JLabel("Score: 0");
        levelLabel = new JLabel("Level: 1");
        linesLabel = new JLabel("Lines: 0");
        
        scoreLabel.setForeground(Color.WHITE);
        levelLabel.setForeground(Color.WHITE);
        linesLabel.setForeground(Color.WHITE);
        
        JPanel statsPanel = new JPanel(new GridLayout(3, 1));
        statsPanel.setBackground(new Color(60, 60, 60));
        statsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        statsPanel.add(scoreLabel);
        statsPanel.add(levelLabel);
        statsPanel.add(linesLabel);
        
        sidePanel.add(holdPanel);
        sidePanel.add(statsPanel);
        sidePanel.add(nextPanel);
        sidePanel.add(new JLabel()); // Empty space
        
        mainPanel.add(gamePanel, BorderLayout.CENTER);
        mainPanel.add(sidePanel, BorderLayout.EAST);
        
        setContentPane(mainPanel);
        pack();
        setLocationRelativeTo(null);
        
        timer = new Timer(delay, e -> gamePanel.moveDown());
        timer.start();
        
        // Set up key bindings
        setupKeyBindings();
    }
    
    private void setupKeyBindings() {
        KeyStroke leftKey = KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0);
        KeyStroke rightKey = KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0);
        KeyStroke downKey = KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0);
        KeyStroke rotateKey = KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0);
        KeyStroke dropKey = KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0);
        KeyStroke holdKey = KeyStroke.getKeyStroke(KeyEvent.VK_C, 0);
        KeyStroke pauseKey = KeyStroke.getKeyStroke(KeyEvent.VK_P, 0);
        
        // Set up input map and action map
        gamePanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(leftKey, "left");
        gamePanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(rightKey, "right");
        gamePanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(downKey, "down");
        gamePanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(rotateKey, "rotate");
        gamePanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(dropKey, "drop");
        gamePanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(holdKey, "hold");
        gamePanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(pauseKey, "pause");
        
        gamePanel.getActionMap().put("left", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!isPaused) gamePanel.moveLeft();
            }
        });
        
        gamePanel.getActionMap().put("right", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!isPaused) gamePanel.moveRight();
            }
        });
        
        gamePanel.getActionMap().put("down", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!isPaused) gamePanel.moveDown();
            }
        });
        
        gamePanel.getActionMap().put("rotate", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!isPaused) gamePanel.rotate();
            }
        });
        
        gamePanel.getActionMap().put("drop", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!isPaused) gamePanel.hardDrop();
            }
        });
        
        gamePanel.getActionMap().put("hold", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!isPaused) gamePanel.holdPiece();
            }
        });
        
        gamePanel.getActionMap().put("pause", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                togglePause();
            }
        });
    }
    
    private void togglePause() {
        isPaused = !isPaused;
        if (isPaused) {
            timer.stop();
        } else {
            timer.start();
        }
        gamePanel.repaint();
    }
    
    private void updateScore(int linesCleared) {
        int points;
        switch (linesCleared) {
            case 1:
                points = 100 * level;
                break;
            case 2:
                points = 300 * level;
                break;
            case 3:
                points = 500 * level;
                break;
            case 4:
                points = 800 * level;
                break;
            default:
                points = 0;
        }
        
        score += points;
        this.linesCleared += linesCleared;
        
        // Update level every 10 lines
        level = (this.linesCleared / 10) + 1;
        
        // Update delay based on level
        delay = Math.max(100, 500 - ((level - 1) * 50));
        timer.setDelay(delay);
        
        // Update labels
        scoreLabel.setText("Score: " + score);
        levelLabel.setText("Level: " + level);
        linesLabel.setText("Lines: " + this.linesCleared);
    }
    
    private void gameOver() {
        timer.stop();
        JOptionPane.showMessageDialog(this, 
                "Game Over!\nScore: " + score + "\nLevel: " + level + "\nLines: " + linesCleared, 
                "Game Over", JOptionPane.INFORMATION_MESSAGE);
        resetGame();
    }
    
    private void resetGame() {
        score = 0;
        level = 1;
        linesCleared = 0;
        delay = 500;
        
        scoreLabel.setText("Score: 0");
        levelLabel.setText("Level: 1");
        linesLabel.setText("Lines: 0");
        
        gamePanel.resetGame();
        holdPanel.reset();
        nextPanel.setNextPiece(gamePanel.getNextPiece());
        
        timer.setDelay(delay);
        timer.start();
    }
    
    // Inner class for the main game board
    private class GamePanel extends JPanel {
        private Color[][] board;
        private Tetromino currentPiece;
        private Tetromino nextPiece;
        private Tetromino heldPiece;
        private boolean canHold;
        
        public GamePanel() {
            setPreferredSize(new Dimension(
                    BOARD_WIDTH * BLOCK_SIZE + 2 * BORDER_WIDTH, 
                    BOARD_HEIGHT * BLOCK_SIZE + 2 * BORDER_WIDTH));
            setBackground(Color.BLACK);
            setBorder(BorderFactory.createLineBorder(new Color(100, 100, 100), BORDER_WIDTH));
            
            board = new Color[BOARD_HEIGHT][BOARD_WIDTH];
            new Random();
            resetGame();
        }
        
        public void resetGame() {
            // Clear the board
            for (int row = 0; row < BOARD_HEIGHT; row++) {
                for (int col = 0; col < BOARD_WIDTH; col++) {
                    board[row][col] = null;
                }
            }
            
            currentPiece = createRandomPiece();
            nextPiece = createRandomPiece();
            heldPiece = null;
            canHold = true;
            
            if (nextPanel != null) {
                nextPanel.setNextPiece(nextPiece);
            }
            
            repaint(); // Ensure this is inside a properly defined method with a return type and body
        }
        
        public Tetromino getNextPiece() {
            return nextPiece;
        }
        
        public void holdPiece() {
            if (!canHold) {
                return;
            }
            
            if (heldPiece == null) {
                heldPiece = currentPiece.getType();
                currentPiece = nextPiece;
                nextPiece = createRandomPiece();
                nextPanel.setNextPiece(nextPiece);
            } else {
                Tetromino temp = currentPiece.getType();
                currentPiece = heldPiece;
                heldPiece = temp;
                
                // Reset position of the new current piece
                currentPiece.x = BOARD_WIDTH / 2 - 2;
                currentPiece.y = 0;
            }
            
            holdPanel.setHeldPiece(heldPiece);
            canHold = false;
            repaint();
        }
        
        public void moveLeft() {
            if (canMove(currentPiece, currentPiece.x - 1, currentPiece.y)) {
                currentPiece.x--;
                repaint();
            }
        }
        
        public void moveRight() {
            if (canMove(currentPiece, currentPiece.x + 1, currentPiece.y)) {
                currentPiece.x++;
                repaint();
            }
        }
        
        public void moveDown() {
            if (canMove(currentPiece, currentPiece.x, currentPiece.y + 1)) {
                currentPiece.y++;
                repaint();
            } else {
                placePiece();
            }
        }
        
        public void hardDrop() {
            // Move down as far as possible
            int dropDistance = 0;
            while (canMove(currentPiece, currentPiece.x, currentPiece.y + 1)) {
                currentPiece.y++;
                dropDistance++;
            }
            
            // Add points for hard drop
            score += dropDistance * 2;
            scoreLabel.setText("Score: " + score);
            
            placePiece();
            repaint();
        }
        
        public void rotate() {
            Tetromino rotated = currentPiece.getRotated();
            
            // Try standard rotation
            if (canMove(rotated, currentPiece.x, currentPiece.y)) {
                currentPiece = rotated;
                repaint();
                return;
            }
            
            // Wall kick tests - try nearby positions
            int[] testX = {-1, 1, -2, 2};
            for (int i = 0; i < testX.length; i++) {
                if (canMove(rotated, currentPiece.x + testX[i], currentPiece.y)) {
                    currentPiece = rotated;
                    currentPiece.x += testX[i];
                    repaint();
                    return;
                }
                
                // Try one row up (for floor kicks)
                if (canMove(rotated, currentPiece.x + testX[i], currentPiece.y - 1)) {
                    currentPiece = rotated;
                    currentPiece.x += testX[i];
                    currentPiece.y -= 1;
                    repaint();
                    return;
                }
            }
        }
        
        private boolean canMove(Tetromino piece, int x, int y) {
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    if (piece.shape[i][j]) {
                        int newX = x + j;
                        int newY = y + i;
                        
                        // Check bounds
                        if (newX < 0 || newX >= BOARD_WIDTH || newY >= BOARD_HEIGHT) {
                            return false;
                        }
                        
                        // Check collision with placed pieces (only if inside the board)
                        if (newY >= 0 && board[newY][newX] != null) {
                            return false;
                        }
                    }
                }
            }
            return true;
        }
        
        private void placePiece() {
            // Place the current piece on the board
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    if (currentPiece.shape[i][j]) {
                        int boardY = currentPiece.y + i;
                        int boardX = currentPiece.x + j;
                        
                        // Check if we've reached the top of the board
                        if (boardY < 0) {
                            gameOver();
                            return;
                        }
                        
                        board[boardY][boardX] = currentPiece.color;
                    }
                }
            }
            
            // Check for completed lines
            int linesCleared = clearLines();
            if (linesCleared > 0) {
                updateScore(linesCleared);
            }
            
            // Reset for next piece
            currentPiece = nextPiece;
            nextPiece = createRandomPiece();
            nextPanel.setNextPiece(nextPiece);
            canHold = true;
            
            // Check if the new piece can be placed at the starting position
            if (!canMove(currentPiece, currentPiece.x, currentPiece.y)) {
                gameOver();
                return;
            }
            
            repaint();
        }
        
        private Tetromino createRandomPiece() {
            Random random = new Random();
            int type = random.nextInt(7);
            
            switch (type) {
                case 0:
                    return new TetrominoI();
                case 1:
                    return new TetrominoJ();
                case 2:
                    return new TetrominoL();
                case 3:
                    return new TetrominoO();
                case 4:
                    return new TetrominoS();
                case 5:
                    return new TetrominoT();
                case 6:
                    return new TetrominoZ();
                default:
                    return new TetrominoI();
            }
        }

        private int clearLines() {
            int linesCleared = 0;
            
            for (int row = BOARD_HEIGHT - 1; row >= 0; row--) {
                boolean isLineFull = true;
                
                // Check if the line is full
                for (int col = 0; col < BOARD_WIDTH; col++) {
                    if (board[row][col] == null) {
                        isLineFull = false;
                        break;
                    }
                }
                
                if (isLineFull) {
                    linesCleared++;
                    
                    // Move all lines above this one down
                    for (int r = row; r > 0; r--) {
                        for (int col = 0; col < BOARD_WIDTH; col++) {
                            board[r][col] = board[r - 1][col];
                        }
                    }
                    
                    // Clear the top line
                    for (int col = 0; col < BOARD_WIDTH; col++) {
                        board[0][col] = null;
                    }
                    
                    // Since we've moved lines down, we need to check this row again
                    row++;
                }
            }
            
            return linesCleared;
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            // Draw the board
            for (int row = 0; row < BOARD_HEIGHT; row++) {
                for (int col = 0; col < BOARD_WIDTH; col++) {
                    if (board[row][col] != null) {
                        drawBlock(g2d, col, row, board[row][col]);
                    }
                }
            }
            
            // Draw the current piece
            if (currentPiece != null) {
                for (int i = 0; i < 4; i++) {
                    for (int j = 0; j < 4; j++) {
                        if (currentPiece.shape[i][j]) {
                            int x = currentPiece.x + j;
                            int y = currentPiece.y + i;
                            if (y >= 0) { // Only draw visible blocks
                                drawBlock(g2d, x, y, currentPiece.color);
                            }
                        }
                    }
                }
                
                // Draw ghost piece (preview of where the piece will land)
                drawGhostPiece(g2d);
            }
            
            // Draw pause overlay
            if (isPaused) {
                g2d.setColor(new Color(0, 0, 0, 150));
                g2d.fillRect(0, 0, getWidth(), getHeight());
                
                g2d.setColor(Color.WHITE);
                g2d.setFont(new Font("Arial", Font.BOLD, 24));
                String pauseText = "PAUSED";
                FontMetrics fm = g2d.getFontMetrics();
                g2d.drawString(pauseText, 
                        (getWidth() - fm.stringWidth(pauseText)) / 2, 
                        getHeight() / 2);
            }
        }
        
        private void drawBlock(Graphics2D g2d, int x, int y, Color color) {
            int px = BORDER_WIDTH + x * BLOCK_SIZE;
            int py = BORDER_WIDTH + y * BLOCK_SIZE;
            
            // Main block color
            g2d.setColor(color);
            g2d.fillRect(px, py, BLOCK_SIZE, BLOCK_SIZE);
            
            // Highlight (top and left edges)
            g2d.setColor(color.brighter());
            g2d.drawLine(px, py, px + BLOCK_SIZE - 1, py); // Top
            g2d.drawLine(px, py, px, py + BLOCK_SIZE - 1); // Left
            
            // Shadow (bottom and right edges)
            g2d.setColor(color.darker());
            g2d.drawLine(px + BLOCK_SIZE - 1, py, px + BLOCK_SIZE - 1, py + BLOCK_SIZE - 1); // Right
            g2d.drawLine(px, py + BLOCK_SIZE - 1, px + BLOCK_SIZE - 1, py + BLOCK_SIZE - 1); // Bottom
            
            // Grid border
            g2d.setColor(Color.BLACK);
            g2d.drawRect(px, py, BLOCK_SIZE - 1, BLOCK_SIZE - 1);
        }
        
        private void drawGhostPiece(Graphics2D g2d) {
            Tetromino ghost = currentPiece.copy();
            
            // Move the ghost piece down as far as it will go
            while (canMove(ghost, ghost.x, ghost.y + 1)) {
                ghost.y++;
            }
            
            // Draw ghost piece (semi-transparent outline)
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    if (ghost.shape[i][j]) {
                        int x = ghost.x + j;
                        int y = ghost.y + i;
                        
                        if (y >= 0) { // Only draw visible blocks
                            int px = BORDER_WIDTH + x * BLOCK_SIZE;
                            int py = BORDER_WIDTH + y * BLOCK_SIZE;
                            
                            g2d.setColor(new Color(ghost.color.getRed(), 
                                                  ghost.color.getGreen(),
                                                  ghost.color.getBlue(), 80));
                            g2d.fillRect(px, py, BLOCK_SIZE, BLOCK_SIZE);
                            g2d.setColor(ghost.color);
                            g2d.drawRect(px, py, BLOCK_SIZE - 1, BLOCK_SIZE - 1);
                        }
                    }
                }
            }
        }
    }
    
    // Inner class for the hold panel
    class HoldPanel extends JPanel {
        private Tetromino heldPiece;
        
        public HoldPanel() {
            setPreferredSize(new Dimension(HOLD_SIZE * BLOCK_SIZE + 2 * BORDER_WIDTH, 
                                         HOLD_SIZE * BLOCK_SIZE + 2 * BORDER_WIDTH));
            setBackground(new Color(60, 60, 60));
            setBorder(BorderFactory.createTitledBorder(
                    BorderFactory.createLineBorder(new Color(100, 100, 100), BORDER_WIDTH),
                    "HOLD", 
                    TitledBorder.CENTER, 
                    TitledBorder.TOP,
                    null,
                    Color.WHITE));
        }
        
        public void setHeldPiece(Tetromino piece) {
            this.heldPiece = piece;
            repaint();
        }
        
        public void reset() {
            heldPiece = null;
            repaint();
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            if (heldPiece != null) {
                // Center the piece in the panel
                int offsetX = (getWidth() - 4 * BLOCK_SIZE) / 2;
                int offsetY = (getHeight() - 4 * BLOCK_SIZE) / 2;
                
                // Draw the held piece
                for (int i = 0; i < 4; i++) {
                    for (int j = 0; j < 4; j++) {
                        if (heldPiece.shape[i][j]) {
                            int px = offsetX + j * BLOCK_SIZE;
                            int py = offsetY + i * BLOCK_SIZE;
                            
                            g2d.setColor(heldPiece.color);
                            g2d.fillRect(px, py, BLOCK_SIZE - 1, BLOCK_SIZE - 1);
                            
                            g2d.setColor(heldPiece.color.brighter());
                            g2d.drawLine(px, py, px + BLOCK_SIZE - 2, py);
                            g2d.drawLine(px, py, px, py + BLOCK_SIZE - 2);
                            
                            g2d.setColor(heldPiece.color.darker());
                            g2d.drawLine(px + BLOCK_SIZE - 2, py, px + BLOCK_SIZE - 2, py + BLOCK_SIZE - 2);
                            g2d.drawLine(px, py + BLOCK_SIZE - 2, px + BLOCK_SIZE - 2, py + BLOCK_SIZE - 2);
                        }
                    }
                }
            }
        }
    }
    
    // Inner class for the next piece panel
    class NextPanel extends JPanel {
        private Tetromino nextPiece;
        
        public NextPanel() {
            setPreferredSize(new Dimension(HOLD_SIZE * BLOCK_SIZE + 2 * BORDER_WIDTH, 
                                         HOLD_SIZE * BLOCK_SIZE + 2 * BORDER_WIDTH));
            setBackground(new Color(60, 60, 60));
            setBorder(BorderFactory.createTitledBorder(
                    BorderFactory.createLineBorder(new Color(100, 100, 100), BORDER_WIDTH),
                    "NEXT", 
                    TitledBorder.CENTER, 
                    TitledBorder.TOP,
                    null,
                    Color.WHITE));
        }
        
        public void setNextPiece(Tetromino piece) {
            this.nextPiece = piece;
            repaint();
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            if (nextPiece != null) {
                // Center the piece in the panel
                int offsetX = (getWidth() - 4 * BLOCK_SIZE) / 2;
                int offsetY = (getHeight() - 4 * BLOCK_SIZE) / 2;
                
                // Draw the next piece
                for (int i = 0; i < 4; i++) {
                    for (int j = 0; j < 4; j++) {
                        if (nextPiece.shape[i][j]) {
                            int px = offsetX + j * BLOCK_SIZE;
                            int py = offsetY + i * BLOCK_SIZE;
                            
                            g2d.setColor(nextPiece.color);
                            g2d.fillRect(px, py, BLOCK_SIZE - 1, BLOCK_SIZE - 1);
                            
                            g2d.setColor(nextPiece.color.brighter());
                            g2d.drawLine(px, py, px + BLOCK_SIZE - 2, py);
                            g2d.drawLine(px, py, px, py + BLOCK_SIZE - 2);
                            
                            g2d.setColor(nextPiece.color.darker());
                            g2d.drawLine(px + BLOCK_SIZE - 2, py, px + BLOCK_SIZE - 2, py + BLOCK_SIZE - 2);
                            g2d.drawLine(px, py + BLOCK_SIZE - 2, px + BLOCK_SIZE - 2, py + BLOCK_SIZE - 2);
                        }
                    }
                }
            }
        }
    }
    
    // Abstract Tetromino class
    abstract class Tetromino {
        protected boolean[][] shape;
        protected Color color;
        protected int x, y;
        
        public Tetromino() {
            shape = new boolean[4][4];
            x = BOARD_WIDTH / 2 - 2;
            y = 0;
        }
        
        // Get a new instance of the same type of piece
        public abstract Tetromino getType();
        
        // Get a rotated copy
        public Tetromino getRotated() {
            Tetromino rotated = getType();
            rotated.x = this.x;
            rotated.y = this.y;
            
            // Rotate the shape (90 degrees clockwise)
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    rotated.shape[j][3 - i] = this.shape[i][j];
                }
            }
            
            return rotated;
        }
        
        // Create a copy of this piece
        public Tetromino copy() {
            Tetromino copy = getType();
            copy.x = this.x;
            copy.y = this.y;
            
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    copy.shape[i][j] = this.shape[i][j];
                }
            }
            
            return copy;
        }
    }
    
    // Tetromino implementations for each shape
    private class TetrominoI extends Tetromino {
        public TetrominoI() {
            super();
            color = new Color(0, 240, 240); // Cyan
            
            // ****
            shape[1][0] = true;
            shape[1][1] = true;
            shape[1][2] = true;
            shape[1][3] = true;
        }
        
        @Override
        public Tetromino getType() {
            return new TetrominoI();
        }
    }
    
    private class TetrominoJ extends Tetromino {
        public TetrominoJ() {
            super();
            color = new Color(0, 0, 240); // Blue
            
            // *
            // ***
            shape[0][0] = true;
            shape[1][0] = true;
            shape[1][1] = true;
            shape[1][2] = true;
        }
        
        @Override
        public Tetromino getType() {
            return new TetrominoJ();
        }
    }
    
    private class TetrominoL extends Tetromino {
        public TetrominoL() {
            super();
            color = new Color(240, 160, 0); // Orange
            
            //   *
            // ***
            shape[0][2] = true;
            shape[1][0] = true;
            shape[1][1] = true;
            shape[1][2] = true;
        }
        
        @Override
        public Tetromino getType() {
            return new TetrominoL();
        }
    }
    
    private class TetrominoO extends Tetromino {
        public TetrominoO() {
            super();
            color = new Color(240, 240, 0); // Yellow
            
            // **
            // **
            shape[0][0] = true;
            shape[0][1] = true;
            shape[1][0] = true;
            shape[1][1] = true;
        }
        
        @Override
        public Tetromino getType() {
            return new TetrominoO();
        }
    }
    
    private class TetrominoS extends Tetromino {
        public TetrominoS() {
            super();
            color = new Color(0, 240, 0); // Green
            
            //  **
            // **
            shape[0][1] = true;
            shape[0][2] = true;
            shape[1][0] = true;
            shape[1][1] = true;
        }
        
        @Override
        public Tetromino getType() {
            return new TetrominoS();
        }
    }
    
    private class TetrominoT extends Tetromino {
        public TetrominoT() {
            super();
            color = new Color(160, 0, 240); // Purple
            
            //  *
            // ***
            shape[0][1] = true;
            shape[1][0] = true;
            shape[1][1] = true;
            shape[1][2] = true;
        }
        
        @Override
        public Tetromino getType() {
            return new TetrominoT();
        }
    }
    
    private class TetrominoZ extends Tetromino {
        public TetrominoZ() {
            super();
            color = new Color(240, 0, 0); // Red
            
            // **
            //  **
            shape[0][0] = true;
            shape[0][1] = true;
            shape[1][1] = true;
            shape[1][2] = true;
        }
        
        @Override
        public Tetromino getType() {
            return new TetrominoZ();
        }
    }
}