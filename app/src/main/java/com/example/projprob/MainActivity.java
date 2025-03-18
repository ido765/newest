package com.example.projprob;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btntogame, btntoinst, btntoset;
    private ActivityResultLauncher<Intent> activityResultLauncher;
    private FBmodule fbmodule;
    private ConstraintLayout mainLayout;
    public static String staticColorRes;
    public static String StaticSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        // Initialize buttons
        btntogame = findViewById(R.id.to_game);
        btntogame.setOnClickListener(this);
        btntoinst = findViewById(R.id.inst);
        btntoinst.setOnClickListener(this);
        btntoset = findViewById(R.id.settings);
        btntoset.setOnClickListener(this);

        // Initialize main layout
        mainLayout = findViewById(R.id.main_layout);

        // Initialize ActivityResultLauncher
        activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Intent data = result.getData();
                        String size = data.getStringExtra("size");
                        String color = data.getStringExtra("color");

                        if (size != null) {
                            Toast.makeText(MainActivity.this, "Size selected: " + size, Toast.LENGTH_SHORT).show();
                            fbmodule.changeSizeInFirebase(size);
                        }

                        if (color != null) {
                            Toast.makeText(MainActivity.this, "Color selected: " + color, Toast.LENGTH_SHORT).show();
                            fbmodule.changeColorInFirebase(color);
                        }
                    }
                }
        );

        // Initialize Firebase module
        fbmodule = new FBmodule(this);
    }

    public void updateSize(String size) {
        // Update UI or game logic based on size
        StaticSize = size;
        // You can add additional logic here to handle size changes
    }

    public void updateBackgroundColor(String color) {
        int colorRes;
        staticColorRes = color;
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
        mainLayout.setBackgroundColor(colorRes);

        // Update color for all buttons to ensure visibility
        int textColor = color.equalsIgnoreCase("white") ? Color.BLACK : Color.WHITE;
        btntogame.setTextColor(textColor);
        btntoinst.setTextColor(textColor);
        btntoset.setTextColor(textColor);
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;

        if (btntogame == v) {
            intent = new Intent(this, GameActivity.class);
        } else if (btntoinst == v) {
            intent = new Intent(this, Instructions.class);
        } else if (btntoset == v) {
            intent = new Intent(this, SettingsActivity.class);
        }

        if (intent != null) {
            activityResultLauncher.launch(intent);
        }
    }
}