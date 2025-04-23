package com.example.projprob;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.firebase.database.FirebaseDatabase;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {
    private Button BackSet;
    private Spinner sizeSpinner;
    private Spinner colorSpinner;
    private boolean isFirstTimeSize = true;
    private boolean isFirstTimeColor = true;
    private String selectedSize;
    private ConstraintLayout SettingsLayout;

    String[] sizes = {"choose grid size", "3x3", "4x4", "5x5", "6x6", "7x7"};
    String[] colors = {"choose background color", "Black", "White", "Blue", "Red", "Green"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        init();
        setupSpinners();
        updateBackgroundColors();
    }

    private void init() {
        BackSet = findViewById(R.id.back_set);
        BackSet.setOnClickListener(this);
        SettingsLayout = findViewById(R.id.setting_layout);
    }

    private void setupSpinners() {
        sizeSpinner = findViewById(R.id.spin);
        ArrayAdapter<String> sizeAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, sizes);
        sizeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sizeSpinner.setAdapter(sizeAdapter);

        sizeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!isFirstTimeSize && position > 0) {
                    selectedSize = sizes[position];
                    sendResult("size", selectedSize);
                }
                isFirstTimeSize = false;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Not needed since Spinner always has a default value
            }
        });

        colorSpinner = findViewById(R.id.color_spin);
        ArrayAdapter<String> colorAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, colors);
        colorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        colorSpinner.setAdapter(colorAdapter);

        colorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!isFirstTimeColor && position > 0) {
                    String selectedColor = colors[position];
                    sendResult("color", selectedColor);
                }
                isFirstTimeColor = false;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Not needed since Spinner always has a default value
            }
        });
    }

    private void sendResult(String key, String value) {
        Intent resultIntent = new Intent();
        resultIntent.putExtra(key, value);
        setResult(RESULT_OK, resultIntent);
    }

    @Override
    public void onClick(View v) {
        if (v == BackSet) {
            if (selectedSize != null && !selectedSize.equals("choose grid size")) {
                FirebaseDatabase.getInstance().getReference()
                        .child("size")
                        .setValue(selectedSize);
            }

            String selectedColor = colorSpinner.getSelectedItem().toString();
            if (!selectedColor.equals("choose background color")) {
                FirebaseDatabase.getInstance().getReference()
                        .child("backgroundColor")
                        .setValue(selectedColor);
            }

            finish();
        }
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
        SettingsLayout.setBackgroundColor(colorRes);

        int textColor = color.equalsIgnoreCase("white") ? Color.BLACK : Color.WHITE;
        BackSet.setTextColor(textColor);
    }
}