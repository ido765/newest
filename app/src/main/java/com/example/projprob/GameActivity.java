package com.example.projprob;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class GameActivity extends AppCompatActivity implements View.OnClickListener {
    private Button giveupbtn;
    private ImageButton hintBtn;
    private ImageButton modeBtn;
    private ConstraintLayout gamelayout;
    private String size = "4x4";
    private int IntSize = 4;
    private Board board;
    private boolean isEraseMode = true;
    private ImageView heart1, heart2, heart3;
    private int heartsRemaining = 3; // מספר הלבבות שנותרו

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        init();
        size = MainActivity.StaticSize != null ? MainActivity.StaticSize : "4x4";
        IntSize = IntSizer(size);

        board = new Board(this, IntSize);
        board.setGameActivity(this); // העברת ה-GameActivity ל-Board
        LinearLayout linearLayout = findViewById(R.id.game);
        linearLayout.addView(board);
        updateBackgroundColors();
    }

    private void init() {
        giveupbtn = findViewById(R.id.giveupbtn);
        hintBtn = findViewById(R.id.hint_btn);
        modeBtn = findViewById(R.id.mode_btn);
        heart1 = findViewById(R.id.heart1);
        heart2 = findViewById(R.id.heart2);
        heart3 = findViewById(R.id.heart3);
        giveupbtn.setOnClickListener(this);
        hintBtn.setOnClickListener(this);
        modeBtn.setOnClickListener(this);
        gamelayout = findViewById(R.id.game_layout);
        updateModeButton();
    }

    private void updateModeButton() {
        if (isEraseMode) {
            modeBtn.setImageResource(R.drawable.erase_tool_icon);
            modeBtn.setBackgroundTintList(getResources().getColorStateList(android.R.color.darker_gray));
        } else {
            modeBtn.setImageResource(R.drawable.pencil_tool_icon);
            modeBtn.setBackgroundTintList(getResources().getColorStateList(android.R.color.holo_green_light));
        }
        modeBtn.animate().scaleX(0.9f).scaleY(0.9f).setDuration(100).withEndAction(() -> {
            modeBtn.animate().scaleX(1f).scaleY(1f).setDuration(100).start();
        }).start();
    }

    private void updateHintButton() {
        Toast.makeText(this, "Hint clicked!", Toast.LENGTH_SHORT).show();
        hintBtn.animate().scaleX(0.9f).scaleY(0.9f).setDuration(100).withEndAction(() -> {
            hintBtn.animate().scaleX(1f).scaleY(1f).setDuration(100).start();
        }).start();
    }

    private int IntSizer(String size) {
        switch (size) {
            case "3x3": return 3;
            case "4x4": return 4;
            case "5x5": return 5;
            case "6x6": return 6;
            case "7x7": return 7;
            default: return 4;
        }
    }

    @Override
    public void onClick(View v) {
        if (v == giveupbtn) {
            createDialog();
        } else if (v == modeBtn) {
            isEraseMode = !isEraseMode;
            updateModeButton();
            board.setMode(isEraseMode ? Board.MODE_ERASE : Board.MODE_PENCIL);
        } else if (v == hintBtn) {
            updateHintButton();
        }
    }

    private void createDialog() {
        CustomDialog customDialog = new CustomDialog(this);
        customDialog.show();
    }

    public void updateBackgroundColors() {
        int colorRes;
        String color = MainActivity.staticColorRes != null ? MainActivity.staticColorRes : "black";

        switch (color.toLowerCase()) {
            case "black":
                colorRes = Color.BLACK;
                break;
            case "white":
                colorRes = Color.WHITE;
                break;
            case "blue":
                colorRes = Color.BLUE;
                break;
            case "red":
                colorRes = Color.RED;
                break;
            case "green":
                colorRes = Color.GREEN;
                break;
            default:
                colorRes = Color.BLACK;
        }
        gamelayout.setBackgroundColor(colorRes);
        LinearLayout linearLayout = findViewById(R.id.game);
        linearLayout.setBackgroundColor(colorRes);
        int textColor = color.equalsIgnoreCase("white") ? Color.BLACK : Color.WHITE;
        giveupbtn.setTextColor(textColor);
    }

    // שיטה שתיקרא על ידי Board כאשר השחקן טועה
    public void onPlayerMistake() {
        if (heartsRemaining > 0) {
            heartsRemaining--;
            switch (heartsRemaining) {
                case 2:
                    heart3.setVisibility(View.GONE);
                    break;
                case 1:
                    heart2.setVisibility(View.GONE);
                    break;
                case 0:
                    heart1.setVisibility(View.GONE);
                    break;
            }
        } else {
            Toast.makeText(this, "טעות!", Toast.LENGTH_SHORT).show();
        }
    }
}