package com.example.clarkyan.othellogame;

/**
 * Created by clarkyan on 4/11/15.
 */
public class CheckValid implements Constant{

    ChessBoard chessBoard;

    public CheckValid()
    {
        super();
        chessBoard = new ChessBoard();
    }

    public int turnJudgement(Board cb, int row, int col, int turn) //turn = -1 black, = 1 white, = 0 end
    {
        if (turn < 0) {
            return getRulesBlack(cb, row, col, turn);
        } else {
            return getRulesWhite(cb, row, col, turn);
        }

    }

    public boolean isOnBoard(Board cb, int row, int col) { //check this click is on chessboard
        if (row >= 0 && col >= 0 && row < cb.Rows && col < cb.Cols) {
            return true;
        }
        return false;
    }

    public boolean isEmpty(Board cb, int row, int col) { //check this click is not on an exist piece
        if (cb.board[row][col] != playerBlack && cb.board[row][col] != playerWhite) {
            return true;
        }
        return false;
    }

    public boolean isFull(Board cb) { //check this chessboard is full
        int i, j;
        for (i = 0; i < cb.Rows; i++)
            for (j = 0; j < cb.Cols; j++) {
                if ((cb.board[i][j] != playerBlack) && (cb.board[i][j] != playerWhite)) {
                    return false;
                }
            }
        return true;
    }

    public int getRulesBlack(Board cb, int row, int col, int turn) { //check this click is a valid piece

        int dirX, dirY, killed = 0, count;
        int firstX, firstY, secondX, secondY, thirdX, thirdY;
        if (cb == null) {
            return 0;
        }
        if (!isOnBoard(cb, row, col)) {
            return 0;
        }
        if (!isEmpty(cb, row, col)) {
            return 0;
        }

        for (dirX = -1; dirX < 2; dirX++)
            for (dirY = -1; dirY < 2; dirY++) { //judge the eight directions of each piece to find a neighbor
                if (dirX == 0 && dirY == 0)
                    continue;
                count = 0;
                firstX = row + dirY;
                firstY = col + dirX;
                if (firstX < cb.Rows && firstY < cb.Cols && firstX >= 0 && firstY >= 0 && cb.board[firstX][firstY] == turn * (-1)) { //the neighbor is the different color

                    count++;

                    for (secondX = row + 2 * dirY, secondY = col + 2 * dirX; secondX >= 0 && secondY >= 0 && secondX < cb.Rows && secondY < cb.Cols; secondX += dirY, secondY += dirX) {
                        if (cb.board[secondX][secondY] == turn * (-1)) { //the next neighbor is the different color
                            count++;
                            continue;
                        } else if (cb.board[secondX][secondY] == turn) { //the next neighbor is the same color
                            killed += count;

                            for (thirdX = row + dirY, thirdY = col + dirX; thirdX <= row + count && thirdX >= row - count && thirdY <= col + count && thirdY >= col - count; thirdX += dirY, thirdY += dirX) {
                                //do the recursive check
                                if (!chessBoard.hint) {
                                    cb.board[thirdX][thirdY] = turn;
                                }

                            }
                            break;
                        } else break;
                    }
                }
            }

        if (killed > 0 && !chessBoard.hint){ //is a valid piece and set this piece

            cb.board[row][col] = turn;

        }
        return killed;
    }

    //the same as above algorithm for white chess
    public int getRulesWhite(Board cb, int row, int col, int turn) {

        int dirX, dirY, killed = 0, count = 0;
        int firstX, firstY, secondX, secondY, thirdX, thirdY;
        if (cb == null) {
            return 0;
        }
        if (!isOnBoard(cb, row, col)) {
            return 0;
        }
        if (!isEmpty(cb, row, col)) {
            return 0;
        }

        for (dirX = -1; dirX < 2; dirX++)
            for (dirY = -1; dirY < 2; dirY++) {
                if (dirX == 0 && dirY == 0)
                    continue;
                count = 0;
                firstX = row + dirY;
                firstY = col + dirX;
                if (firstX < cb.Rows && firstY < cb.Cols && firstX >= 0 && firstY >= 0 && cb.board[firstX][firstY] == turn * (-1)) {

                    count++;

                    for (secondX = row + 2 * dirY, secondY = col + 2 * dirX; secondX >= 0 && secondY >= 0 && secondX < cb.Rows && secondY < cb.Cols; secondX += dirY, secondY += dirX) {
                        if (cb.board[secondX][secondY] == turn * (-1)) {
                            count++;
                            continue;
                        } else if (cb.board[secondX][secondY] == turn) {
                            killed += count;

                            for (thirdX = row + dirY, thirdY = col + dirX; thirdX <= row + count && thirdX >= row - count && thirdY <= col + count && thirdY >= col - count; thirdX += dirY, thirdY += dirX) {
                                if (!chessBoard.hint){
                                    cb.board[thirdX][thirdY] = turn;
                                }

                            }
                            break;
                        } else break;
                    }
                }
            }

        if (killed > 0 && !chessBoard.hint){
            cb.board[row][col] = turn;

        }
        return killed;
    }
}
