/*
GitHub: https://github.com/BenjaminThio
Instagram: https://www.instagram.com/benjamin_thio70

How to play:
1. Compile and run the program.
2. Press number keys - 1, 2, 3, 4, 5, 6, 7, 8, 9 to mark up.

Map:
1 2 3
4 5 6
7 8 9

3. Press r key to restart the game.
*/

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyAdapter;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.Graphics2D;

import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.lang.Thread;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.ImageIcon;

import java.util.Arrays;
import java.util.ArrayList;

public class TicTacToe {
    private JFrame window;
    
    private final String path = "Sprites\\Original\\";
    private final String extension = ".png";
    private final String empty = path + "white-square" + extension;
    private final String[] lineTypes = new String[]{"horizontal-line", "vertical-line" , "left-diagonal-line", "right-diagonal-line"};
    private final String horizontalLine = path + lineTypes[0] + extension;
    private final String verticalLine = path + lineTypes[1] + extension;
    private final String leftDiagonalLine = path + lineTypes[2] + extension;
    private final String rightDiagonalLine = path + lineTypes[3] + extension;
    private final String[] lines = new String[]{horizontalLine, verticalLine, leftDiagonalLine, rightDiagonalLine};

    private String playerMark = path + "x" + extension;
    private String botMark = path + "o" + extension;
    private String playerWinMark = null;
    private String botWinMark = null;

    private boolean gameOver = false; 
    private boolean isKeyPressed = false;
    private String[] board = new String[9];
    private int[] winCoords = null;

    public static void main(String[] args)
    {
        new TicTacToe();
    }

    private void print(Object object)
    {
        System.out.println(object);
    }

    private void GenerateCombinedImages()
    {
        File file = new File("Sprites/Combined");
        if (!file.exists())
        {
            file.mkdir();
        }
        try
        {
            final String newPath = "Sprites\\Combined\\";
            final String newPlayerMark = newPath + "x" + extension;
            final String newBotMark = newPath + "o" + extension;
            
            ImageIO.write(CombineImage(empty, playerMark), "PNG", new File(newPlayerMark));
            ImageIO.write(CombineImage(empty, botMark), "PNG", new File(newBotMark));
            for (int i = 0; i < lines.length; i++)
            {
                ImageIO.write(CombineImage(newPlayerMark, lines[i]), "PNG", new File(newPath + "player-" + lineTypes[i] + extension));
                ImageIO.write(CombineImage(newBotMark, lines[i]), "PNG", new File(newPath + "bot-" + lineTypes[i] + extension));
            }
            playerMark = newPlayerMark;
            botMark = newBotMark;
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private BufferedImage CombineImage(String... paths)
    {
        BufferedImage combinedImage = new BufferedImage(50, 50, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics2d = combinedImage.createGraphics();
        for (String path : paths)
        {
            try {
                BufferedImage image = ImageIO.read(getClass().getClassLoader().getResource(path));
                graphics2d.drawImage(image, 0, 0, null);
            }
            catch (IOException e)
            {
                e.printStackTrace();
                return null;
            }
        }
        return combinedImage;
    }

    private void RenderWindow()
    {
        window = new JFrame();
        window.setTitle("Sokoban");
        window.setSize((3 * 50) + 16, (3 * 50) + 39);
        window.setLocationRelativeTo(null);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.getContentPane().setBackground(Color.BLACK);
        window.setLayout(null);
        window.setResizable(false);
        window.setVisible(true);

        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean bool = false;
                String[] logos = new String[]{playerMark, botMark}; 
                while (true)
                {
                    try
                    {
                        window.setIconImage(Toolkit.getDefaultToolkit().getImage(logos[bool ? 1 : 0]));
                        Thread.sleep(1000);
                        bool = !bool;
                    }
                    catch (InterruptedException e)
                    {
                        print(e);
                    }
                }
            }
        }).start();
    }

    private void GenerateBoard()
    {
        for (int i = 0; i < board.length; i++)
        {
            board[i] = empty;
        }
    }

    private ArrayList<ArrayList<String>> GenerateBoardMap()
    {
        ArrayList<ArrayList<String>> boardMap = new ArrayList<ArrayList<String>>();
        int counter = 0;
        for (int i = 0; i < board.length; i++)
        {
            if (boardMap.size() == 0 || counter == 3)
            {
                boardMap.add(new ArrayList<String>());
                counter = 0;
            }
            boardMap.get(boardMap.size() - 1).add(board[i]);
            counter += 1;
        }
        return boardMap;
    }

    private boolean ContainsCoord(int[] coordsArray, int coord)
    {
        if (coordsArray == null)
        {
            return false;
        }
        for (int i : coordsArray)
        {
            if (coord == i)
            {
                return true;
            }
        }
        return false;
    }

    private void RenderBoard()
    {
        Container container = window.getContentPane();
        container.removeAll();

        ArrayList<ArrayList<String>> boardMap = GenerateBoardMap();
        int counter = 0;
        for (int y = 0; y < boardMap.size(); y++)
        {
            for (int x = 0; x < boardMap.get(y).size(); x++)
            {
                JLabel label = new JLabel();
                if (ContainsCoord(winCoords, counter) && boardMap.get(y).get(x) == playerMark)
                {
                    label.setIcon(new ImageIcon(playerWinMark));
                }
                else if (ContainsCoord(winCoords, counter) && boardMap.get(y).get(x) == botMark)
                {
                    label.setIcon(new ImageIcon(botWinMark));
                }
                else
                {
                    label.setIcon(new ImageIcon(boardMap.get(y).get(x)));
                }
                Dimension size = label.getPreferredSize();
                label.setBounds(x * 50, y * 50, size.width, size.height);
                container.add(label);
                counter += 1;
            }
        }
        window.repaint();
    }

    private boolean Insert(String mark, int position)
    {
        if (!gameOver && board[position] == empty)
        {
            board[position] = mark;
            if (CleanWinCheck(playerMark) || CleanWinCheck(botMark) || Draw())
            {
                gameOver = true;
            }
            return true;
        }
        return false;
    }

    private boolean Draw()
    {
        if (!Arrays.asList(board).contains(empty))
        {
            return true;
        }
        return false;
    }

    private boolean CleanWinCheck(String mark)
    {
        int counter = 0;
        int[][] winCases = new int[][]
        {
            {0, 1, 2},
            {3, 4, 5},
            {6, 7, 8},
            {0, 3, 6},
            {1, 4, 7},
            {2, 5, 8},
            {0, 4, 8},
            {2, 4, 6}
        };
        String newPath = "Sprites\\Combined\\";
        for (int[] winCase : winCases)
        {
            if (board[winCase[0]] == board[winCase[1]] && board[winCase[1]] == board[winCase[2]] && board[winCase[2]] == mark)
            {
                winCoords = winCase;
                if (counter <= 2)
                {
                    playerWinMark = newPath + "player-" + lineTypes[0] + extension;
                    botWinMark = newPath + "bot-" + lineTypes[0] + extension;
                }
                else if (counter > 2 && counter <= 5)
                {
                    playerWinMark = newPath + "player-" + lineTypes[1] + extension;
                    botWinMark = newPath + "bot-" + lineTypes[1] + extension;
                }
                else if (counter > 5 && counter <= 6)
                {
                    playerWinMark = newPath + "player-" + lineTypes[2] + extension;
                    botWinMark = newPath + "bot-" + lineTypes[2] + extension;
                }
                else if (counter > 6 && counter <= 7)
                {
                    playerWinMark = newPath + "player-" + lineTypes[3] + extension;
                    botWinMark = newPath + "bot-" + lineTypes[3] + extension;
                }
                return true;
            }
            counter += 1;
        }
        return false;
    }

    private boolean Win(String mark)
    {
        int[][] winCases = new int[][]
        {
            {0, 1, 2},
            {3, 4, 5},
            {6, 7, 8},
            {0, 3, 6},
            {1, 4, 7},
            {2, 5, 8},
            {0, 4, 8},
            {2, 4, 6}
        };
        for (int[] winCase : winCases)
        {
            if (board[winCase[0]] == board[winCase[1]] && board[winCase[1]] == board[winCase[2]] && board[winCase[2]] == mark)
            {
                return true;
            }
        }
        return false;
    }

    private int Minimax(String[] board, int depth, boolean maximizing)
    {
        if (Win(botMark))
        {
            return 1;
        }
        else if (Win(playerMark))
        {
            return -1;
        }
        else if (Draw())
        {
            return 0;
        }
        if (maximizing)
        {
            int bestScore = -1;
            for (int i = 0; i < board.length; i++)
            {
                if (board[i] == empty)
                {
                    board[i] = botMark;
                    int score = Minimax(board, depth + 1, false);
                    board[i] = empty;
                    if (score > bestScore)
                    {
                        bestScore = score;
                    }
                }
            }
            return bestScore;
        }
        else
        {
            int bestScore = 1;
            for (int i = 0; i < board.length; i++)
            {
                if (board[i] == empty)
                {
                    board[i] = playerMark;
                    int score = Minimax(board, depth + 1, true);
                    board[i] = empty;
                    if (score < bestScore)
                    {
                        bestScore = score;
                    }
                }
            }
        return bestScore;
        }
    }

    private int AIFindBestPosition()
    {
        int bestScore = -1;
        for (int i = 0; i < board.length; i++)
        {
            if (board[i] == empty)
            {
                board[i] = botMark;
                int score = Minimax(board, 0, false);
                board[i] = empty;
                if (score > bestScore)
                {
                    bestScore = score;
                    return i;
                }
            }
        }
        return 0;
    }

    public TicTacToe()
    {
        RenderWindow();
        GenerateCombinedImages();
        GenerateBoard();
        RenderBoard();

        window.addKeyListener(
            new KeyAdapter() {
                public void keyPressed(KeyEvent e)
                {
                    if (isKeyPressed)
                    {
                        return;
                    }
                    else
                    {
                        isKeyPressed = true;
                    }
                    int key = e.getKeyCode();
                    if (key == KeyEvent.VK_1 || key == KeyEvent.VK_2 || key == KeyEvent.VK_3 || key == KeyEvent.VK_4 || key == KeyEvent.VK_5 || key == KeyEvent.VK_6 || key == KeyEvent.VK_7 || key == KeyEvent.VK_8 || key == KeyEvent.VK_9)
                    {
                        if (Insert(playerMark, Integer.parseInt(KeyEvent.getKeyText(key)) - 1))
                        {
                            Insert(botMark, AIFindBestPosition());
                        }
                    }
                    else if (key == KeyEvent.VK_R)
                    {
                        GenerateBoard();
                        gameOver = false;
                        playerWinMark = null;
                        botWinMark = null;
                        winCoords = null;
                    }
                    
                    RenderBoard();
                }

                public void keyReleased(KeyEvent event)
                {
                    isKeyPressed = false;
                }
            }
        );
    }
}