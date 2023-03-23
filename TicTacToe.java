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
    static int winner = -1; // 0 if X wins, 1 if O wins, -1 if no winner yet
    static int xWins = 0;
    static int oWins = 0; // number of wins for each player
    static int[][] board = new int[3][3]; // 0 if empty, 1 if X, 2 if O
    static int computer = 1, human = 2;
    static int[] bestMove = {-1, -1};

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
        board = new int[3][3];
        winner = -1;
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
            if (winner == 1) { // x
                page.drawString("The winner is", 310, 150);
                page2.setStroke(new BasicStroke(7));
                page2.drawLine(352, 170, 382,200);
                page2.drawLine(382, 170, 352,200);
            } else if (winner == 2) { // o
                page.drawString("The winner is", 310, 150);
                page.setColor(black);
                page.fillOval(332, 160, 50, 50);
                page.setColor(white);
                page.fillOval(342, 170, 30, 30);
            } else if (winner == 3) { // tie
                page.drawString("It's a tie", 330, 178);
            }
        } else {
            Font font2 = new Font(" Courier", Font.BOLD, 24);
            page.setFont(font2);
            page.drawString("", 350, 160);
            if (playerX) {
                page.drawString("X 's Turn", 315, 180);
            } else {
                page.drawString("O 's Turn", 315, 180);
            }
        }
        Font c = new Font("Courier", Font.BOLD + Font.CENTER_BASELINE, 13);
        page.setFont(c);
    }

    public void drawGame(Graphics page) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == 1) {
                    Graphics2D page2 = (Graphics2D) page;
                    page2.setStroke(new BasicStroke(9));
                    page2.setColor(black);
                    page2.drawLine(15 + (96 * i) + 20, 15 + (96 * j) + 20, 15 + (96 * i) + 60,15 + (96 * j) + 60);
                    page2.drawLine(15 + (96 * i) + 60, 15 + (96 * j) + 20, 15 + (96 * i) + 20,15 + (96 * j) + 60);
                } else if (board[i][j] == 2) {
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
                int selX = 0, selY = 0;
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

                    if (board[selX][selY] == 0) {
                        if (!playerX) {
                            board[selX][selY] = 2;
                            playerX = true;
                        }

                        if (!checkBoardFull(board)) {
                            gameDone = true;
                            winner = 3;
                            xWins += 1;
                            oWins += 1;
                            resetButton.setVisible(true);
                        } else {
                            new java.util.Timer().schedule(
                                    new java.util.TimerTask() {
                                        @Override
                                        public void run() {
                                            xThinking = true;
                                            playerx();
                                            xThinking = false;
                                            repaint();
                                            int status = checkBoardStatus(board);
                                            if (status == 1) {
                                                gameDone = true;
                                                winner = computer;
                                                xWins += 1;
                                                resetButton.setVisible(true);
                                            }
                                            if (status == -1) {
                                                gameDone = true;
                                                winner = human;
                                                oWins += 1;
                                                resetButton.setVisible(true);
                                            }
                                        }
                                    },
                                    500
                            );
                        }
                        //System.out.println(" CLICK= x:" + a + ",y: " + b + "; selX,selY: " + selX + "," + selY);
                    }
                }
            }
        }

        public void mouseReleased(MouseEvent event) {
        }

        public void mouseEntered(MouseEvent event) {
        }

        public void mouseExited(MouseEvent event) {
        }

        public void mousePressed(MouseEvent event) {
        }
    }

    public static void playerx() {
        bestMove = findBestMove(board);
        //System.out.println(bestMove.getRow());
        //System.out.println(bestMove.getCol());
        board[bestMove[0]][bestMove[1]] = 1;
        playerX = false;
    }

    public static int[] findBestMove(int[][] board) {
        int bestVal = Integer.MIN_VALUE;
        bestMove = new int[]{-1, -1};

        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board[row].length; col++) {
                if (board[row][col] == 0) {
                    board[row][col] = computer;
                    int minimaxVal = minimax(board, 0, false);
                    board[row][col] = 0;
                    //System.out.println(minimaxVal);
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

    public static int minimax(int board[][], int depth, Boolean isMax) {
        int score = checkBoardStatus(board);
        if (score == 1 || score == -1) {
            return score;
        }

        if (checkBoardFull(board) == false) {
            return 0;
        }

        if (isMax) {
            int best = Integer.MIN_VALUE;
            for (int row = 0; row < board.length; row++) {
                for (int col = 0; col < board[row].length; col++) {
                    if (board[row][col] == 0) {
                        board[row][col] = computer;
                        best = Math.max(best, minimax(board, depth++, !isMax));
                        board[row][col] = 0;
                    }
                }
            }
            return best;
        } else {
            int best = Integer.MAX_VALUE;
            for (int row = 0; row < board.length; row++) {
                for (int col = 0; col < board[row].length; col++) {
                    if (board[row][col] == 0) {
                        board[row][col] = human;
                        best = Math.min(best, minimax(board, depth++, !isMax));
                        board[row][col] = 0;
                    }
                }
            }
            return best;
        }
    }

    static Boolean checkBoardFull(int board[][]) {
        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board[row].length; col++) {
                if (board[row][col] == 0) {
                    return true;
                }
            }
        }
        return false;
    }

    public static int checkBoardStatus(int board[][]) {

        // Checking rows for win
        for (int row = 0; row < board.length; row++) {
            if (board[row][0] == board[row][1] && board[row][0] == board[row][2]) {
                if (board[row][0] == computer) {
                    return 1;
                } else if (board[row][0] == human) {
                    return -1;
                }
            }
        }

        // Checking columns for win
        for (int col = 0; col < board[0].length; col++) {
            if (board[0][col] == board[1][col] && board[0][col] == board[2][col]) {
                if (board[0][col] == computer) {
                    return 1;
                } else if (board[0][col] == human) {
                    return -1;
                }
            }
        }

        // Checking diagonals for win
        if (board[0][0] == board[1][1] && board[0][0] == board[2][2]) {
            if (board[0][0] == computer) {
                return 1;
            } else if (board[0][0] == human) {
                return -1;
            }
        }
        if (board[0][2] == board[1][1] && board[0][2] == board[2][0]) {
            if (board[0][2] == computer) {
                return 1;
            } else if (board[0][2] == human) {
                return -1;
            }
        }

        return 0;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        newGame();
    }

}