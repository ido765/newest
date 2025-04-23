package com.example.projprob;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.Locale;

public class Instructions extends AppCompatActivity implements View.OnClickListener {

    private ConstraintLayout instructionsLayout;
    private TextView instructionsText;
    private Button languageBtn;
    private Button ttsBtn;
    private Button backBtn;
    private boolean isEnglish = false;
    private TextToSpeech textToSpeech;
    private boolean isSpeaking = false;
    private boolean isTtsInitialized = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructions);
        init();
        updateBackgroundColors();

        textToSpeech = new TextToSpeech(this, status -> {
            if (status == TextToSpeech.SUCCESS) {
                int result = textToSpeech.setLanguage(Locale.US);
                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    Toast.makeText(this, "TTS language not supported or missing data", Toast.LENGTH_LONG).show();
                } else {
                    isTtsInitialized = true;
                    Toast.makeText(this, "TTS initialized successfully", Toast.LENGTH_SHORT).show();

                    // הוספת מאזין כדי לעקוב אחרי התקדמות ה-TTS
                    textToSpeech.setOnUtteranceProgressListener(new UtteranceProgressListener() {
                        @Override
                        public void onStart(String utteranceId) {
                            runOnUiThread(() -> Toast.makeText(Instructions.this, "TTS started speaking", Toast.LENGTH_SHORT).show());
                        }

                        @Override
                        public void onDone(String utteranceId) {
                            runOnUiThread(() -> {
                                isSpeaking = false;
                                updateTtsButton();
                                Toast.makeText(Instructions.this, "TTS finished speaking", Toast.LENGTH_SHORT).show();
                            });
                        }

                        @Override
                        public void onError(String utteranceId) {
                            runOnUiThread(() -> {
                                isSpeaking = false;
                                updateTtsButton();
                                Toast.makeText(Instructions.this, "TTS error occurred", Toast.LENGTH_SHORT).show();
                            });
                        }
                    });
                }
            } else {
                Toast.makeText(this, "Text-to-Speech initialization failed", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void init() {
        instructionsLayout = findViewById(R.id.instructions_layout);
        instructionsText = findViewById(R.id.instructions_text);
        languageBtn = findViewById(R.id.language_btn);
        ttsBtn = findViewById(R.id.tts_btn);
        backBtn = findViewById(R.id.back_btn);
        backBtn.setOnClickListener(this);
        languageBtn.setOnClickListener(this);
        ttsBtn.setOnClickListener(this);

        updateInstructionsText();
    }

    private void updateInstructionsText() {
        Locale currentLocale = isEnglish ? new Locale("en") : new Locale("he");
        Resources res = getResources();
        Configuration config = new Configuration(res.getConfiguration());
        config.setLocale(currentLocale);
        Resources localizedResources = createConfigurationContext(config).getResources();
        String instructions = localizedResources.getString(R.string.insts);
        instructionsText.setText(instructions);

        languageBtn.setText(isEnglish ? "Switch to Hebrew" : "Switch to English");
        updateTtsButton();
    }

    private void updateTtsButton() {
        if (isSpeaking) {
            ttsBtn.setText("Stop");
        } else {
            ttsBtn.setText("Text to Speech");
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.back_btn) {
            finish();
        } else if (v.getId() == R.id.language_btn) {
            if (isSpeaking) {
                textToSpeech.stop();
                isSpeaking = false;
                updateTtsButton();
            }
            isEnglish = !isEnglish;
            updateInstructionsText();
        } else if (v.getId() == R.id.tts_btn) {
            if (isSpeaking) {
                textToSpeech.stop();
                isSpeaking = false;
                updateTtsButton();
            } else {
                if (isEnglish) {
                    if (!isTtsInitialized) {
                        Toast.makeText(this, "TTS is not initialized yet", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    String text = instructionsText.getText().toString();
                    if (text.isEmpty()) {
                        Toast.makeText(this, "No text to read", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    // שימוש ב-utteranceId כדי לעקוב אחרי הקריאה
                    int result = textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, "InstructionsTTS");
                    if (result == TextToSpeech.SUCCESS) {
                        isSpeaking = true;
                        updateTtsButton();
                    } else {
                        Toast.makeText(this, "Failed to start TTS", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "קריאה לא זמינה בעברית", Toast.LENGTH_SHORT).show();
                }
            }
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
        instructionsLayout.setBackgroundColor(colorRes);

        // הגדרת צבע טקסט וצבע רקע של ה-TextView בהתאם לרקע המסך
        int textColor;
        int textBackgroundColor;
        if (color.equalsIgnoreCase("white")) {
            textColor = Color.BLACK; // טקסט שחור על רקע בהיר
            textBackgroundColor = Color.LTGRAY; // רקע אפור בהיר ל-TextView
        } else {
            textColor = Color.WHITE; // טקסט לבן על רקע כהה
            textBackgroundColor = Color.DKGRAY; // רקע אפור כהה ל-TextView
        }

        instructionsText.setTextColor(textColor);
        instructionsText.setBackgroundColor(textBackgroundColor);

        // עדכון צבע הטקסט של הכפתורים
        languageBtn.setTextColor(textColor);
        ttsBtn.setTextColor(textColor);
        backBtn.setTextColor(textColor);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
    }
}