package com.example.projprob;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class CustomDialog extends Dialog implements View.OnClickListener {

    private final Context context;
    Button btnYes, btnNo;

    public CustomDialog(@NonNull Context context) {
        super(context);
        setContentView(R.layout.activity_custom_dialog); // Sets the dialog layout
        this.context = context;

        btnYes = findViewById(R.id.btnYes); // Initializes the "Yes" button
        btnNo = findViewById(R.id.btnNo); // Initializes the "No" button
        btnYes.setOnClickListener(this); // Listens for clicks on the "Yes" button
        btnNo.setOnClickListener(this); // Listens for clicks on the "No" button
    }

    @Override
    public void onClick(View v) {
        if (btnNo == v) {
            dismiss(); // Closes the dialog
        }

        if (btnYes == v) {
            ((GameActivity) context).finish(); // Ends the game activity
        }
    }
}