/* 
GitHub: https://github.com/BenjaminThio
Instagram: https://www.instagram.com/benjamin_thio70
*/

import java.util.Arrays;
import java.util.Scanner;

public class Prototype {
    private final String empty = "#";
    private final String playerMark = "X";
    private final String botMark = "O";

    private boolean gameOver = false;
    private String[] board = new String[9];
    
    private void print(Object object)
    {
        System.out.println(object);
    }

    private void Insert(String mark, int position)
    {
        board[position] = mark;
        if (Win(playerMark) || Win(botMark) || Draw())
        {
            RenderBoard();
            if (Win(playerMark))
            {
                print("Player won!");
            }
            else if (Win(botMark))
            {
                print("Bot won!");
            }
            else if (Draw())
            {
                print("Draw!");  
            }
            gameOver = true;
        }
    }

    private boolean Draw()
    {
        if (!Arrays.asList(board).contains(empty))
        {
            return true;
        }
        return false;
    }

    private boolean Win(String mark)
    {
        if (board[0] == board[1] && board[1] == board[2] && board[2] == mark)
        {
            return true;
        }
        else if (board[3] == board[4] && board[4] == board[5] && board[5] == mark)
        {
            return true;
        }
        else if (board[6] == board[7] && board[7] == board[8] && board[8] == mark)
        {
            return true;
        }
        else if (board[0] == board[3] && board[3] == board[6] && board[6] == mark)
        {
            return true;
        }
        else if (board[1] == board[4] && board[4] == board[7] && board[7] == mark)
        {
            return true;
        }
        else if (board[2] == board[5] && board[5] == board[8] && board[8] == mark)
        {
            return true;
        }
        else if (board[0] == board[4] && board[4] == board[8] && board[8] == mark)
        {
            return true;
        }
        else if (board[2] == board[4] && board[4] == board[6] && board[6] == mark)
        {
            return true;
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

    private void GenerateBoard()
    {
        for (int i = 0; i < board.length; i++)
        {
            board[i] = empty;
        }
    }

    private void RenderBoard()
    {
        String renderer = "";
        int counter = 0;
        for (int i = 0; i < board.length; i++)
        {
            renderer += board[i];
            counter += 1;
            if (i < board.length - 1 && counter == 3)
            {
                renderer += "\n";
                counter = 0;
            }
        }
        print(renderer);
    }

    public static void main(String[] args)
    {
        new Prototype();
    }

    private Prototype()
    {
        Scanner scanner = new Scanner(System.in);
        GenerateBoard();
        while (!gameOver)
        {
            RenderBoard();
            print("Please type a number between 1 - 9: ");
            int position = Integer.parseInt(scanner.nextLine());
            if (position >= 1 && position <= 9 && board[position - 1] == empty)
            {
                Insert(playerMark, position - 1);
                if (gameOver)
                {
                    break;
                }
            }
            else
            {
                continue;
            }
            Insert(botMark, AIFindBestPosition());
            if (gameOver)
            {
                break;
            }
        }
        scanner.close();
    }
}
