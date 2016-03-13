package com.example.clarkyan.othellogame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by clarkyan on 25/10/15.
 * To create a chessboard by using canvas and view
 */
public class BoardView extends View {

    protected static int mBoardSize;

    protected static int mXBoardCount;
    protected static int mYBoardCount;

    private static int mXOffset;
    private static int mYOffset;

    private Bitmap[] mBoardArray;

    private int[][] mBoardGrid;

    private final Paint mPaint = new Paint();

    private Object Rect;

    public BoardView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        initBoardView(8, 8);
    }

    public BoardView(Context context, AttributeSet attrs) {
        super(context, attrs);

        initBoardView(8, 8);
    }

    void initBoardView(int xBoardCount, int yBoardCount)
    {
        mXBoardCount = xBoardCount;
        mYBoardCount = yBoardCount;

        mBoardSize = 48; //size of a piece of chessboard

        mXOffset = 1;
        mYOffset = 1;

        mBoardGrid = new int[mXBoardCount][mYBoardCount];
        clearBoard();
    }

    public void resetBoard(int boardcount) {
        mBoardArray = new Bitmap[boardcount];
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        int temp;
        mBoardSize = w / mXBoardCount;
        temp = h / mYBoardCount;
        if(mBoardSize > temp) {
            mBoardSize = temp;
        }
        if(mBoardSize > 48) {
            mBoardSize = 48;
        }
    }

    public void loadBoard(int key, Drawable board) {

        Bitmap bitmap = Bitmap.createBitmap(mBoardSize, mBoardSize, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        board.setBounds(0, 0, mBoardSize, mBoardSize);
        board.draw(canvas);

        mBoardArray[key] = bitmap;
    }

    public void clearBoard() {
        for (int x = 0; x < mXBoardCount; x++) {
            for (int y = 0; y < mYBoardCount; y++) {
                setBoard(0, x, y);
            }
        }
    }

    public void setBoard(int boardindex, int x, int y) {
        mBoardGrid[x][y] = boardindex;
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int x = 0; x < mXBoardCount; x++) {
            for (int y = 0; y < mYBoardCount; y++) {
                if (mBoardGrid[x][y] >= 0) {
                    canvas.drawBitmap(mBoardArray[mBoardGrid[x][y]],
                            mXOffset + x * mBoardSize,
                            mYOffset + y * mBoardSize,
                            mPaint);
                }
            }
        }

    }

}
