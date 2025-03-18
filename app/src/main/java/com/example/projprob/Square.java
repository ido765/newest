package com.example.projprob;

public class Square {
    public int height, width;
    private int num;
    private boolean shouldBeUsed, pressed, isCorrect, isLocked, isPenciled;
    private int row, col;

    public Square(int height, int width, int num, int row, int col, boolean shouldBeUsed,
                  boolean pressed, boolean isCorrect, boolean isLocked) {
        this.height = height;
        this.width = width;
        this.num = num;
        this.row = row;
        this.col = col;
        this.shouldBeUsed = shouldBeUsed;
        this.pressed = pressed;
        this.isCorrect = isCorrect;
        this.isLocked = isLocked;
        this.isPenciled = false;
    }

    public int getNum() { return num; }
    public boolean isPressed() { return pressed; }
    public void setPressed(boolean pressed) { this.pressed = pressed; }
    public boolean isPenciled() { return isPenciled; }
    public void setPenciled(boolean penciled) { this.isPenciled = penciled; }
    public boolean isCorrect() { return isCorrect; }
    public boolean isLocked() { return isLocked; }
    public int getRow() { return row; }
    public int getCol() { return col; }
    public boolean shouldBeUsed() { return shouldBeUsed; }
}