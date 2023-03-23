// GUI Source https://copyassignment.com/gui-tic-tac-toe-game-in-java/
// Algorithm Reference https://www.geeksforgeeks.org/finding-optimal-move-in-tic-tac-toe-using-minimax-algorithm-in-game-theory/

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class TicTacToe extends JPanel implements ActionListener {

    // core logic variables
    static boolean playerX; // true if player X's turn, false if player O's turn
    static boolean xThinking;
    static boolean gameDone = false; // true if game is over
    static int xWins = 0;
    static int oWins = 0; // number of wins for each player
    static char[][] board = new char[3][3];
    static char computer = 'x', human = 'o', empty = '\u0000', tie = 't', noWinnerYet = 'n';
    static char winner = noWinnerYet;
    static int[] bestMove = {-1,-1};
    static int pointValue = 1;

    // paint variables
    static int a = 0; // used for drawing the X's and O's
    static int b = 5; // used for drawing the X's and O's
    static int selX = 0; // selected square x
    static int selY = 0; // selected square y

    // COLORS
    Color white = new Color(255, 255, 255);
    Color black = new Color(0, 0, 0);

    static JButton resetButton;

    // CONSTRUCTOR
    public TicTacToe() {
        Dimension size = new Dimension(450, 300); // size of the panel
        setPreferredSize(size);
        setMaximumSize(size);
        setMinimumSize(size);
        addMouseListener(new XOListener()); // add mouse listener
        resetButton = new JButton("New Game");
        resetButton.addActionListener(this);
        resetButton.setBounds(300, 250, 100, 30);
        setLayout(null);
        add(resetButton);
        resetButton.setVisible(false);
        resetButton.setLocation(320, 250);
    }

    public static void newGame() {
        board = new char[3][3];
        winner = noWinnerYet;
        gameDone = false;
        playerX = false;
        resetButton.setVisible(false);
    }

    @Override
    public void paintComponent(Graphics page) {
        super.paintComponent(page);
        drawBoard(page);
        drawUI(page);
        if (!xThinking) {
            drawGame(page);
        }
    }

    public void drawBoard(Graphics page) {
        setBackground(white);
        page.setColor(black);
        page.fillRect(15, 100, 270, 5);
        page.fillRect(15, 195, 270, 5);
        page.fillRect(100, 15, 5, 270);
        page.fillRect(195, 15, 5, 270);
    }

    public void drawUI(Graphics page) {
        // SET FONT
        Font font = new Font("Helvetica", Font.BOLD, 20);
        page.setFont(font);

        // SET WIN COUNTER
        page.setColor(black);
        page.drawString("Win Count", 310, 30);
        page.drawString(": " + xWins, 362, 70);
        page.drawString(": " + oWins, 362, 105);

        // DRAW score X
        Graphics2D page2 = (Graphics2D) page;
        page2.setStroke(new BasicStroke(7));
        page2.drawLine(332, 55, 352,75);
        page2.drawLine(352, 55, 332, 75);

        // DRAW score O
        page.setColor(black);
        page.fillOval(43 + 190 + 95, 85, 30, 30);
        page.setColor(white);
        page.fillOval(49 + 190 + 95, 90, 19, 19);

        // DRAW WHO'S TURN or WINNER
        page.setColor(black);
        Font font1 = new Font(" Courier", Font.BOLD, 18);
        page.setFont(font1);

        if (gameDone) {
            if (winner == computer) { // x
                page.drawString("The winner is", 310, 150);
                page2.setStroke(new BasicStroke(7));
                page2.drawLine(352, 170, 382,200);
                page2.drawLine(382, 170, 352,200);
            } else if (winner == human) { // o
                page.drawString("The winner is", 310, 150);
                page.setColor(black);
                page.fillOval(332, 160, 50, 50);
                page.setColor(white);
                page.fillOval(342, 170, 30, 30);
            } else if (winner == tie) { // tie
                page.drawString("It's a tie", 330, 178);
            }
        } else {
            Font font2 = new Font(" Courier", Font.BOLD, 24);
            page.setFont(font2);
            page.drawString("", 350, 160);
            if (playerX) {
                page.drawString("X's Turn", 315, 180);
            } else {
                page.drawString("O's Turn", 315, 180);
            }
        }
        Font c = new Font("Courier", Font.BOLD, 13);
        page.setFont(c);
    }

    public void drawGame(Graphics page) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == computer) {
                    Graphics2D page2 = (Graphics2D) page;
                    page2.setStroke(new BasicStroke(9));
                    page2.setColor(black);
                    page2.drawLine(15 + (96 * i) + 20, 15 + (96 * j) + 20, 15 + (96 * i) + 60,15 + (96 * j) + 60);
                    page2.drawLine(15 + (96 * i) + 60, 15 + (96 * j) + 20, 15 + (96 * i) + 20,15 + (96 * j) + 60);
                } else if (board[i][j] == human) {
                    page.setColor(black);
                    page.fillOval(30 + 95 * i, 30 + 95 * j, 50, 50);
                    page.setColor(white);
                    page.fillOval(40 + 95 * i, 40 + 95 * j, 30, 30);
                }
            }
        }
        repaint();
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Tic Tac Toe");
        frame.getContentPane();
        TicTacToe gamePanel = new TicTacToe();
        frame.add(gamePanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.pack();
        frame.setVisible(true);
    }

    private class XOListener implements MouseListener {

        public void mouseClicked(MouseEvent event) {
            selX = -1;
            selY = -1;
            if (!gameDone && !playerX) {
                a = event.getX();
                b = event.getY();
                int selX, selY;
                if (a > 12 && a < 99) {
                    selX = 0;
                } else if (a > 103 && a < 195) {
                    selX = 1;
                } else if (a > 200 && a < 287) {
                    selX = 2;
                } else {
                    selX = -1;
                }

                if (b > 12 && b < 99) {
                    selY = 0;
                } else if (b > 103 && b < 195) {
                    selY = 1;
                } else if (b > 200 && b < 287) {
                    selY = 2;
                } else {
                    selY = -1;
                }
                if (selX != -1 && selY != -1) {

                    if (board[selX][selY] == empty) {
                        if (!playerX) {
                            board[selX][selY] = human;
                            playerX = true;
                        }

                        if (checkBoardFull(board)) {
                            gameDone = true;
                            winner = tie;
                            xWins += pointValue;
                            oWins += pointValue;
                            resetButton.setVisible(true);
                        } else {
                            new java.util.Timer().schedule(
                                    new java.util.TimerTask() {
                                        @Override
                                        public void run() {
                                            xThinking = true;
                                            playerX();
                                            xThinking = false;
                                            repaint();
                                            int status = checkBoardStatus(board);
                                            if (status == pointValue) {
                                                gameDone = true;
                                                winner = computer;
                                                xWins += pointValue;
                                                resetButton.setVisible(true);
                                            }
                                            if (status == -pointValue) {
                                                gameDone = true;
                                                winner = human;
                                                oWins += pointValue;
                                                resetButton.setVisible(true);
                                            }
                                        }
                                    },
                                    500
                            );
                        }
                    }
                }
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {
        }
        @Override
        public void mouseReleased(MouseEvent e) {
        }
        @Override
        public void mouseEntered(MouseEvent e) {
        }
        @Override
        public void mouseExited(MouseEvent e) {
        }
    }

    public static void playerX() {
        bestMove = findBestMove(board);
        board[bestMove[0]][bestMove[1]] = computer;
        playerX = false;
    }

    public static int[] findBestMove(char[][] board) {
        int bestVal = Integer.MIN_VALUE;
        bestMove = new int[]{-1, -1};

        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board[row].length; col++) {
                if (board[row][col] == empty) {
                    board[row][col] = computer;
                    int minimaxVal = minimax(board, 0, false);
                    board[row][col] = empty;
                    if (minimaxVal > bestVal) {
                        bestMove[0] = row;
                        bestMove[1] = col;
                        bestVal = minimaxVal;
                    }
                }
            }
        }
        return bestMove;
    }

    public static int minimax(char[][] board, int depth, Boolean isMax) {
        if (checkBoardStatus(board) == pointValue || checkBoardStatus(board) == -pointValue) {
            return checkBoardStatus(board);
        } else if (checkBoardFull(board)) {
            return 0;
        }

        int best;
        if (isMax) {
            best = Integer.MIN_VALUE;
            for (int row = 0; row < board.length; row++) {
                for (int col = 0; col < board[row].length; col++) {
                    if (board[row][col] == empty) {
                        board[row][col] = computer;
                        best = Math.max(best, minimax(board, depth++, false));
                        board[row][col] = empty;
                    }
                }
            }
        } else {
            best = Integer.MAX_VALUE;
            for (int row = 0; row < board.length; row++) {
                for (int col = 0; col < board[row].length; col++) {
                    if (board[row][col] == empty) {
                        board[row][col] = human;
                        best = Math.min(best, minimax(board, depth++, true));
                        board[row][col] = empty;
                    }
                }
            }
        }
        return best;
    }

    static Boolean checkBoardFull(char[][] board) {
        for (char[] rows : board) {
            for (int element : rows) {
                if (element == empty) {
                    return false;
                }
            }
        }
        return true;
    }

    public static int checkBoardStatus(char[][] board) {

        // Check rows for win
        for (char[] rows : board) {
            if (rows[0] == rows[1] && rows[0] == rows[2]) {
                if (rows[0] == computer) {
                    return pointValue;
                } else if (rows[0] == human) {
                    return -pointValue;
                }
            }
        }

        // Check columns for win
        for (int col = 0; col < board[0].length; col++) {
            if (board[0][col] == board[1][col] && board[0][col] == board[2][col]) {
                if (board[0][col] == computer) {
                    return pointValue;
                } else if (board[0][col] == human) {
                    return -pointValue;
                }
            }
        }

        // Check right diagonal for win
        if (board[0][0] == board[1][1] && board[0][0] == board[2][2]) {
            if (board[0][0] == computer) {
                return pointValue;
            } else if (board[0][0] == human) {
                return -pointValue;
            }
        }

        // Check left diagonal for win
        if (board[0][2] == board[1][1] && board[0][2] == board[2][0]) {
            if (board[0][2] == computer) {
                return pointValue;
            } else if (board[0][2] == human) {
                return -pointValue;
            }
        }

        return 0;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        newGame();
    }

}