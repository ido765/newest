package com.example.projprob;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

import androidx.annotation.NonNull;

import java.util.Random;

public class Board extends View {
    public static final int MODE_ERASE = 0;
    public static final int MODE_PENCIL = 1;
    private int mode = MODE_ERASE;
    private int size;
    private Square[][] arr;
    private int[] rowSums;
    private int[] colSums;
    private int[] currentRowSums;
    private int[] currentColSums;
    private float offsetX, offsetY;

    public Board(Context context, int size) {
        super(context);
        this.size = size + 1;
        rowSums = new int[this.size];
        colSums = new int[this.size];
        currentRowSums = new int[this.size];
        currentColSums = new int[this.size];
        new Thread(this::init).start();
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    private void init() {
        arr = new Square[size][size];
        fillArr(arr);
        calculateTargetSums();
        calculateCurrentSums();
        postInvalidate();
    }

    private void fillArr(Square[][] arr) {
        Random rand = new Random();
        for (int i = 0; i < size; i++) {
            for (int k = 0; k < size; k++) {
                if (i == 0 && k == 0) {
                    arr[i][k] = new Square(0, 0, 0, i, k, false, false, false, false);
                } else if (i == 0 || k == 0) {
                    arr[i][k] = new Square(0, 0, 0, i, k, false, false, false, false);
                } else {
                    int num = rand.nextInt(9) + 1;
                    arr[i][k] = new Square(0, 0, num, i, k, false, false, false, false);
                }
            }
        }

        for (int i = 1; i < size; i++) {
            int randomCol = rand.nextInt(size - 1) + 1;
            arr[i][randomCol] = new Square(0, 0, arr[i][randomCol].getNum(), i, randomCol, true, false, false, false);
            for (int k = 1; k < size; k++) {
                if (k != randomCol && rand.nextBoolean()) {
                    arr[i][k] = new Square(0, 0, arr[i][k].getNum(), i, k, true, false, false, false);
                }
            }
        }
        for (int k = 1; k < size; k++) {
            boolean hasSelected = false;
            for (int i = 1; i < size; i++) {
                if (arr[i][k].shouldBeUsed()) {
                    hasSelected = true;
                    break;
                }
            }
            if (!hasSelected) {
                int randomRow = rand.nextInt(size - 1) + 1;
                arr[randomRow][k] = new Square(0, 0, arr[randomRow][k].getNum(), randomRow, k, true, false, false, false);
            }
        }
    }

    private void calculateTargetSums() {
        for (int i = 1; i < size; i++) {
            int sum = 0;
            for (int k = 1; k < size; k++) {
                if (arr[i][k].shouldBeUsed()) {
                    sum += arr[i][k].getNum();
                }
            }
            rowSums[i] = sum;
        }
        for (int k = 1; k < size; k++) {
            int sum = 0;
            for (int i = 1; i < size; i++) {
                if (arr[i][k].shouldBeUsed()) {
                    sum += arr[i][k].getNum();
                }
            }
            colSums[k] = sum;
        }
    }

    private void calculateCurrentSums() {
        for (int i = 1; i < size; i++) {
            int sum = 0;
            for (int k = 1; k < size; k++) {
                if (arr[i][k].shouldBeUsed() && !arr[i][k].isPenciled()) {
                    sum += arr[i][k].getNum();
                }
            }
            currentRowSums[i] = sum;
        }
        for (int k = 1; k < size; k++) {
            int sum = 0;
            for (int i = 1; i < size; i++) {
                if (arr[i][k].shouldBeUsed() && !arr[i][k].isPenciled()) {
                    sum += arr[i][k].getNum();
                }
            }
            currentColSums[k] = sum;
        }
    }

    public void updateSquareSizes() {
        int boardWidth = getWidth();
        int boardHeight = getHeight() - 150;
        int squareSize = Math.min(boardWidth / size, boardHeight / size);
        for (int i = 0; i < size; i++) {
            for (int k = 0; k < size; k++) {
                arr[i][k].height = squareSize;
                arr[i][k].width = squareSize;
            }
        }
        offsetX = (getWidth() - (size * squareSize)) / 2;
        offsetY = 50;
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);
        if (arr == null || arr[1][1].height == 0 || arr[1][1].width == 0) {
            updateSquareSizes();
            return;
        }

        Paint paint = new Paint();
        paint.setTextSize(arr[0][0].height / 2);
        paint.setAntiAlias(true);

        paint.setColor(Color.WHITE);
        paint.setTextSize(40);
        canvas.drawText("Level 1", getWidth() / 2 - 50, 40, paint);
        paint.setTextSize(arr[0][0].height / 2);

        for (int i = 0; i < size; i++) {
            for (int k = 0; k < size; k++) {
                Square sq = arr[i][k];
                float x = offsetX + k * sq.width;
                float y = offsetY + i * sq.height;

                paint.setStyle(Paint.Style.FILL);
                if (i == 0 || k == 0) {
                    paint.setColor(Color.BLUE);
                } else if (sq.isPenciled()) {
                    paint.setColor(Color.LTGRAY);
                } else {
                    paint.setColor(Color.WHITE);
                }
                canvas.drawRect(x, y, x + sq.width, y + sq.height, paint);

                paint.setStyle(Paint.Style.STROKE);
                paint.setColor(Color.BLACK);
                paint.setStrokeWidth(3);
                canvas.drawRect(x, y, x + sq.width, y + sq.height, paint);

                paint.setStyle(Paint.Style.FILL);
                if (i == 0 && k > 0) {
                    paint.setColor(Color.WHITE);
                    canvas.drawText(String.valueOf(colSums[k]), x + sq.width / 2 - paint.measureText(String.valueOf(colSums[k])) / 2, y + sq.height / 2 + paint.getTextSize() / 3, paint);
                } else if (k == 0 && i > 0) {
                    paint.setColor(Color.WHITE);
                    canvas.drawText(String.valueOf(rowSums[i]), x + sq.width / 2 - paint.measureText(String.valueOf(rowSums[i])) / 2, y + sq.height / 2 + paint.getTextSize() / 3, paint);
                } else if (sq.getNum() != 0) {
                    paint.setColor(Color.BLACK);
                    canvas.drawText(String.valueOf(sq.getNum()), x + sq.width / 2 - paint.measureText(String.valueOf(sq.getNum())) / 2, y + sq.height / 2 + paint.getTextSize() / 3, paint);
                }
            }
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        updateSquareSizes();
    }
}