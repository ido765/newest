package com.example.projprob;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Instructions extends AppCompatActivity implements View.OnClickListener {

    private Button back;
    private ConstraintLayout inst_layout;
    private TextView instructionsText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructions);
        init();
        updateBackgroundColors();
        setInstructionsText();
    }

    private void init() {
        back = findViewById(R.id.back_ins);
        back.setOnClickListener(this);
        inst_layout = findViewById(R.id.inst_layout);
        instructionsText = findViewById(R.id.instructions_text);
    }

    private void setInstructionsText() {
        instructionsText.setText(getString(R.string.insts));
    }

    @Override
    public void onClick(View v) {
        if (back == v) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }

    public void updateBackgroundColors() {
        int colorRes;
        String color = MainActivity.staticColorRes;

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
        inst_layout.setBackgroundColor(colorRes);

        int textColor = color.equalsIgnoreCase("white") ? Color.BLACK : Color.WHITE;
        back.setTextColor(textColor);
        instructionsText.setTextColor(textColor); // Ensure instructions text is visible
    }
}