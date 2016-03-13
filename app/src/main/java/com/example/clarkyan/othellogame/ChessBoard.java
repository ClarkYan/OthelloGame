package com.example.clarkyan.othellogame;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


public class ChessBoard extends Activity implements View.OnClickListener, Constant {

    CheckValid checkValid;
    Board mBoard;
    BoardView mView;

    MediaPlayer mediaPlayer = new MediaPlayer(); // music player

    private Button nGame;
    private TextView black, white;
    private ImageView win;
    private TextView full;
    private int playerTurn = playerBlack;
    private Button retract;
    private Button hints;
    public static boolean hint = false;
    private boolean follow = false;
    private String result = "";
    private Button audio;
    private Button bgchange;
    private LinearLayout background;
    private int change = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_chess_board);

        nGame = (Button) findViewById(R.id.ngame);

        black = (TextView) findViewById(R.id.black);  //Count the number of black chess
        white = (TextView) findViewById(R.id.white);  //Count the number of white chess
        win = (ImageView) findViewById(R.id.win);     //Show the turn
        full = (TextView) findViewById(R.id.turn);    //Show the result
        retract = (Button) findViewById(R.id.retract);//do the retract function
        hints = (Button) findViewById(R.id.hintsOn);//do the hints function
        audio = (Button) findViewById(R.id.audio);//play music
        bgchange = (Button) findViewById(R.id.bgchange);//change background
        background = (LinearLayout) findViewById(R.id.background);

        nGame.setTag(0);
        retract.setTag(1);
        hints.setTag(2);
        audio.setTag(4);
        bgchange.setTag(6);
        nGame.setOnClickListener(this);
        retract.setOnClickListener(this);
        hints.setOnClickListener(this);
        audio.setOnClickListener(this);
        bgchange.setOnClickListener(this);

        mediaPlayer = MediaPlayer.create(this, R.raw.music); //new a music object

        win.setTag(0); //0 turn black, 1 turn white, 2 game over

        hint = false;
        checkValid = new CheckValid();
        Resources r = this.getResources();
        mView = (BoardView) findViewById(R.id.img);

        mView.resetBoard(5);
        mView.loadBoard(0, r.getDrawable(R.drawable.empty)); //load the chessboard variables
        mView.loadBoard(1, r.getDrawable(R.drawable.black));
        mView.loadBoard(2, r.getDrawable(R.drawable.white));
        mView.loadBoard(3, r.getDrawable(R.drawable.black_t));
        mView.loadBoard(4, r.getDrawable(R.drawable.white_t));

        mBoard = new Board();
        mBoard.displayBoard(mView, mBoard.board);
        init();
    }


    public void init() {

        mView.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent e) { //touch listener for players click events
                int row, col, x, y;
                x = (int) e.getX();
                y = (int) e.getY();
                col = (int) Math.floor(x / 48);
                row = (int) Math.floor(y / 48);

                mBoard.back();

                if (checkValid.isOnBoard(mBoard, row, col)) {
                    hint = false;

                    int killCount = checkValid.turnJudgement(mBoard, row, col, playerTurn);

                    if (killCount > 0 && playerTurn < 0) {
                        mBoard.displayBoard(mView, mBoard.board);
                        win.setTag(1);

                        if (checkValid.isFull(mBoard)) {

                            gameOver(mBoard);
                        } else {
                            if ((!isBlackHint(mBoard)) && (!isWhiteHint(mBoard))) {

                                gameOver(mBoard);
                            } else {

                                if (!isWhiteHint(mBoard)) {
                                    int[] chess = new int[3];
                                    mBoard.countChess(chess);
                                    black.setText(chess[0] + "");
                                    white.setText(chess[1] + "");
                                    if (follow) {
                                        hints(mBoard);
                                    }
                                } else {
                                    playerTurn = playerTurn * (-1);
                                    win.setImageResource(R.drawable.white_chess);
                                    int[] chess = new int[3];
                                    mBoard.countChess(chess);
                                    black.setText(chess[0] + "");
                                    white.setText(chess[1] + "");
                                    retract.setEnabled(true);
                                    //mBoard.back(null);
                                    mBoard.saveBoard();
                                    if (follow) {
                                        hints(mBoard);
                                    }
                                    //}
                                }
                            }
                        }
                    } else if (killCount > 0 && playerTurn > 0) {
                        mBoard.displayBoard(mView, mBoard.board);
                        win.setTag(0);
                        if (checkValid.isFull(mBoard)) {

                            gameOver(mBoard);
                        } else {

                            if ((!isBlackHint(mBoard)) && (!isWhiteHint(mBoard))) {
                                gameOver(mBoard);
                            } else {
                                if (!isBlackHint(mBoard)) {

                                    int[] chess = new int[3];
                                    mBoard.countChess(chess);
                                    black.setText(chess[0] + "");
                                    white.setText(chess[1] + "");
                                    if (follow) {
                                        hints(mBoard);
                                    }
                                } else {
                                    playerTurn = playerTurn * (-1);
                                    win.setImageResource(R.drawable.black_chess);

                                    int[] chess = new int[3];
                                    mBoard.countChess(chess);
                                    black.setText(chess[0] + "");
                                    white.setText(chess[1] + "");
                                    retract.setEnabled(true);

                                    mBoard.saveBoard();
                                    if (follow) {
                                        hints(mBoard);
                                    }
                                }
                            }
                        }
                    }
                }

                return false;

            }
        });
    }

    @Override
    public void onClick(View v) {
        int tag = (Integer) v.getTag();
        switch (tag) {
            case 0:
                newGame();
                break;
            case 1:
                retractGame();
                break;
            case 2:
                hintsOn();
                break;
            case 3:
                hintsOff();
                break;
            case 4:
                musicOn();
                break;
            case 5:
                musicOff();
                break;
            case 6:
                changeBackground();
                break;
        }

    }

    public void hints(Board cb) { //show the hints
        int i, j;
        hint = true;
        clearHints(cb);
        for (i = 0; i < cb.Rows; i++)
            for (j = 0; j < cb.Cols; j++) { //judge the hints on eight directions
                if (win.getTag().equals(0)) {
                    if (cb.board[i][j] == playerWhite) {
                        if (checkValid.getRulesBlack(cb, i, j + 1, -1) > 0) {
                            cb.board[i][j + 1] = mBlack_t;
                        }
                        if (checkValid.getRulesBlack(cb, i + 1, j, -1) > 0) {
                            cb.board[i + 1][j] = mBlack_t;
                        }
                        if (checkValid.getRulesBlack(cb, i - 1, j, -1) > 0) {
                            cb.board[i - 1][j] = mBlack_t;
                        }
                        if (checkValid.getRulesBlack(cb, i, j - 1, -1) > 0) {
                            cb.board[i][j - 1] = mBlack_t;
                        }
                        if (checkValid.getRulesBlack(cb, i + 1, j + 1, -1) > 0) {
                            cb.board[i + 1][j + 1] = mBlack_t;
                        }
                        if (checkValid.getRulesBlack(cb, i + 1, j - 1, -1) > 0) {
                            cb.board[i + 1][j - 1] = mBlack_t;
                        }
                        if (checkValid.getRulesBlack(cb, i - 1, j + 1, -1) > 0) {
                            cb.board[i - 1][j + 1] = mBlack_t;
                        }
                        if (checkValid.getRulesBlack(cb, i - 1, j - 1, -1) > 0) {
                            cb.board[i - 1][j - 1] = mBlack_t;
                        }
                    }
                } else if (win.getTag().equals(1)) {
                    if (cb.board[i][j] == playerBlack) {
                        if (checkValid.getRulesWhite(cb, i, j + 1, 1) > 0) {
                            cb.board[i][j + 1] = mWhite_t;
                        }
                        if (checkValid.getRulesWhite(cb, i + 1, j, 1) > 0) {
                            cb.board[i + 1][j] = mWhite_t;
                        }
                        if (checkValid.getRulesWhite(cb, i - 1, j, 1) > 0) {
                            cb.board[i - 1][j] = mWhite_t;
                        }
                        if (checkValid.getRulesWhite(cb, i, j - 1, 1) > 0) {
                            cb.board[i][j - 1] = mWhite_t;
                        }
                        if (checkValid.getRulesWhite(cb, i + 1, j + 1, 1) > 0) {
                            cb.board[i + 1][j + 1] = mWhite_t;
                        }
                        if (checkValid.getRulesWhite(cb, i + 1, j - 1, 1) > 0) {
                            cb.board[i + 1][j - 1] = mWhite_t;
                        }
                        if (checkValid.getRulesWhite(cb, i - 1, j + 1, 1) > 0) {
                            cb.board[i - 1][j + 1] = mWhite_t;
                        }
                        if (checkValid.getRulesWhite(cb, i - 1, j - 1, 1) > 0) {
                            cb.board[i - 1][j - 1] = mWhite_t;
                        }
                    }
                }
            }
        mBoard.displayBoard(mView, cb.board);
    }

    public boolean isBlackHint(Board cb) { //judge the hints on eight directions of black chess
        int i, j;
        hint = true;
        for (i = 0; i < cb.Rows; i++)
            for (j = 0; j < cb.Cols; j++) {
                if (cb.board[i][j] == playerWhite) {
                    if (checkValid.getRulesBlack(cb, i, j + 1, -1) > 0) {
                        return true;
                    } else if (checkValid.getRulesBlack(cb, i + 1, j, -1) > 0) {
                        return true;
                    } else if (checkValid.getRulesBlack(cb, i - 1, j, -1) > 0) {
                        return true;
                    } else if (checkValid.getRulesBlack(cb, i, j - 1, -1) > 0) {
                        return true;
                    } else if (checkValid.getRulesBlack(cb, i + 1, j + 1, -1) > 0) {
                        return true;
                    } else if (checkValid.getRulesBlack(cb, i + 1, j - 1, -1) > 0) {
                        return true;
                    } else if (checkValid.getRulesBlack(cb, i - 1, j + 1, -1) > 0) {
                        return true;
                    } else if (checkValid.getRulesBlack(cb, i - 1, j - 1, -1) > 0) {
                        return true;
                    }
                }
            }
        return false;
    }

    public boolean isWhiteHint(Board cb) { //judge the hints on eight directions of white chess
        int i, j;
        hint = true;
        for (i = 0; i < cb.Rows; i++)
            for (j = 0; j < cb.Cols; j++) {
                if (cb.board[i][j] == playerBlack) {
                    if (checkValid.getRulesWhite(cb, i, j + 1, 1) > 0) {
                        return true;
                    } else if (checkValid.getRulesWhite(cb, i + 1, j, 1) > 0) {
                        return true;
                    } else if (checkValid.getRulesWhite(cb, i - 1, j, 1) > 0) {
                        return true;
                    } else if (checkValid.getRulesWhite(cb, i, j - 1, 1) > 0) {
                        return true;
                    } else if (checkValid.getRulesWhite(cb, i + 1, j + 1, 1) > 0) {
                        return true;
                    } else if (checkValid.getRulesWhite(cb, i + 1, j - 1, 1) > 0) {
                        return true;
                    } else if (checkValid.getRulesWhite(cb, i - 1, j + 1, 1) > 0) {
                        return true;
                    } else if (checkValid.getRulesWhite(cb, i - 1, j - 1, 1) > 0) {
                        return true;
                    }
                }
            }
        return false;
    }


    public void clearHints(Board cb) { //clear the hints
        int i, j;
        for (i = 0; i < cb.Rows; i++)
            for (j = 0; j < cb.Cols; j++) {
                if (cb.board[i][j] != playerBlack && cb.board[i][j] != playerWhite && cb.board[i][j] != mNone) {
                    cb.board[i][j] = mNone;
                }
            }
        mBoard.displayBoard(mView, cb.board);
    }

    public void newGame() {
        playerTurn = playerBlack;
        mBoard = new Board();
        win.setImageResource(R.drawable.black_chess);
        full.setText("Turn");
        win.setTag(0);
        int[] chess = new int[3];
        mBoard.countChess(chess);
        black.setText(chess[0] + "");
        white.setText(chess[1] + "");
        retract.setEnabled(false);
        if (follow == true) {
            hints(mBoard);
        } else {
            follow = false;
            hint = false;
            mBoard.displayBoard(mView, mBoard.board);
        }
    }

    //***********Additional function*************

    public void retractGame()  //do the retract function
    {
        mBoard.restore();
        clearHints(mBoard);
        playerTurn = playerTurn * (-1);
        if(playerTurn < 0) {
            win.setImageResource(R.drawable.black_chess);
            win.setTag(0);
            int[] chess = new int[3];
            mBoard.countChess(chess);
            black.setText(chess[0] + "");
            white.setText(chess[1] + "");
            retract.setEnabled(false);
            mBoard.displayBoard(mView, mBoard.board);
        }
        else if(playerTurn > 0) {
            win.setImageResource(R.drawable.white_chess);
            win.setTag(1);
            int[] chess = new int[3];
            mBoard.countChess(chess);
            black.setText(chess[0] + "");
            white.setText(chess[1] + "");
            retract.setEnabled(false);
            mBoard.displayBoard(mView, mBoard.board);
        }
        if(follow == true)
        {
            hints(mBoard);
        }
    }

    public void hintsOn() {
        hints.setText("HINTS OFF");
        hints.setTag(3);
        follow = true;
        hints(mBoard);
    }

    public void hintsOff() {
        hints.setText("HINTS ON");
        hints.setTag(2);
        follow = false;
        clearHints(mBoard);
    }

    public void musicOn()
    {
        audio.setText("MUSIC OFF");
        audio.setTag(5);
        mediaPlayer.start();
    }

    public void musicOff()
    {
        audio.setText("MUSIC ON");
        audio.setTag(4);
        mediaPlayer.pause();
    }

    public void changeBackground()
    {
        if(change == 0)
        {
            background.setBackgroundResource(R.drawable.bg11);
            change++;
        }
        else if(change == 1)
        {
            background.setBackgroundResource(R.drawable.bg10);
            change++;
        }
        else if(change == 2)
        {
            background.setBackgroundResource(R.drawable.bg9);
            change++;
        }
        else if(change == 3)
        {
            background.setBackgroundResource(R.drawable.bg8);
            change++;
        }
        else if(change == 4)
        {
            background.setBackgroundResource(R.drawable.bg7);
            change++;
        }
        else
        {
            background.setBackgroundResource(R.drawable.bg6);
            change = 0;
        }
    }

    public void gameOver(Board cb) { //judge the game over
        int rest;
        int[] chess = new int[3];
        cb.countChess(chess);
        black.setText(chess[0] + "");
        white.setText(chess[1] + "");
        if (chess[0] > chess[1]) {
            full.setText("Win");
            win.setImageResource(R.drawable.black_chess);
            win.setTag(2);
            rest = chess[0] - chess[1];
            result = "Black Wins!\n"+"Win " + rest + " chess";
            retract.setEnabled(false);
            dialog();
        } else if (chess[0] == chess[1]) {
            full.setText("Draw");
            win.setImageResource(R.drawable.draw);
            win.setTag(2);
            result = "Draw Game!";
            retract.setEnabled(false);
            dialog();
        } else {
            full.setText("Win");
            win.setImageResource(R.drawable.white_chess);
            win.setTag(2);
            rest = chess[1] - chess[0];
            result = "White Wins!\n"+"Win " + rest + " chess";
            retract.setEnabled(false);
            dialog();
        }
    }

    protected void dialog() { //show the final result message
        AlertDialog.Builder builder = new AlertDialog.Builder(ChessBoard.this);
        builder.setMessage(result);

        builder.setTitle("Result");

        builder.setPositiveButton("Exit", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

                ChessBoard.this.finish();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.create().show();
    }


}
