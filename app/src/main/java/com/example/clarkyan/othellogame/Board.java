package com.example.clarkyan.othellogame;

/**
 * Created by clarkyan on 4/11/15.
 */
public class Board implements Constant{

    public int[][] board;
    public int[][] back;
    public int[][] boardBack;
    public final int Rows = 8;
    public final int Cols = 8;

    public Board() //initialize the chessboard
    {
        board = new int[8][8];  //new chessboard
        back = new int[8][8];  //last chessboard
        boardBack = new int[8][8]; //temp chessboard

        for (int i = 0; i < 8; i++)
            for (int j = 0; j < 8; j++) {
                board[i][j] = mNone;
                boardBack[i][j] = mNone;
            }

        board[3][3] = mWhite;  //white chess position
        board[3][4] = mBlack;  //black chess position
        board[4][3] = mBlack;
        board[4][4] = mWhite;

        boardBack[3][3] = mWhite;  //white chess position
        boardBack[3][4] = mBlack;  //black chess position
        boardBack[4][3] = mBlack;
        boardBack[4][4] = mWhite;

    }

    public void back() {

        for (int i = 0; i < 8; i++)
            for (int j = 0; j < 8; j++)
            {
                back[i][j] = boardBack[i][j];  //temp chessboard -> last chessboard
            }

    }

    public void saveBoard() {
        for (int i = 0; i < 8; i++)
            for (int j = 0; j < 8; j++) {
                boardBack[i][j] = board[i][j]; //new chessboard -> temp chessboard
            }
    }

    public void restore() {
        for (int i = 0; i < 8; i++)
            for (int j = 0; j < 8; j++) {
                board[i][j] = back[i][j]; //last chessboard -> new chessboard
                boardBack[i][j] = board[i][j]; //new chessboard -> temp chessboard
            }
    }

    public int countChess(int[] chess) //0=black, 1=white, 2=empty  count chess
    {
        int i, j;
        chess[0] = 0;
        chess[1] = 0;
        chess[2] = 0;
        for (i = 0; i < Rows; i++)
            for (j = 0; j < Cols; j++) {
                if (board[i][j] == playerBlack)
                    (chess[0])++;
                else if (board[i][j] == playerWhite)
                    (chess[1])++;
                else
                    (chess[2])++;
            }
        return 0;
    }

    public void displayBoard(BoardView view, int[][] board) { //update the chessboard
        int col, row;
        for (col = 0; col < 8; col++)
            for (row = 0; row < 8; row++) {
                if (board[row][col] == mBlack)
                    view.setBoard(1, col, row);
                else if (board[row][col] == mWhite)
                    view.setBoard(2, col, row);
                else if (board[row][col] == mBlack_t)
                    view.setBoard(3, col, row);
                else if (board[row][col] == mWhite_t)
                    view.setBoard(4, col, row);
                else
                    view.setBoard(0, col, row);
            }
        view.invalidate();
    }

}


