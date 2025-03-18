package com.example.projprob;

import androidx.annotation.NonNull;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FBmodule {
    MainActivity mainActivity;
    GameActivity gameActivity;

    private static final String SIZE_KEY = "size";
    private static final String COLOR_KEY = "backgroundColor";

    public FBmodule(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        setupFirebaseListeners();
    }

    private void setupFirebaseListeners() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

        // Listen for size changes
        DatabaseReference sizeRef = firebaseDatabase.getReference(SIZE_KEY);
        sizeRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String size = snapshot.getValue(String.class);
                if (size != null) {
                    mainActivity.updateSize(size);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
            }
        });

        // Listen for color changes
        DatabaseReference colorRef = firebaseDatabase.getReference(COLOR_KEY);
        colorRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String color = snapshot.getValue(String.class);
                if (color != null) {
                    mainActivity.updateBackgroundColor(color);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
            }
        });
    }

    public void changeSizeInFirebase(String size) {
        FirebaseDatabase.getInstance().getReference(SIZE_KEY).setValue(size);
    }

    public void changeColorInFirebase(String color) {
        FirebaseDatabase.getInstance().getReference(COLOR_KEY).setValue(color);
    }
}