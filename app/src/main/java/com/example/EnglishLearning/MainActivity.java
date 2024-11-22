package com.example.englishlearning;

import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private static final int SPEECH_REQUEST_CODE = 1;

    private TextView tvWord, tvFeedback, tvScore;
    private Button btnSpeak;
    private ImageView ivMic;

    private String currentWord = "apple"; // Example word, can be dynamic
    private int currentScore = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvWord = findViewById(R.id.tvWord);
        tvFeedback = findViewById(R.id.tvFeedback);
        tvScore = findViewById(R.id.tvScore);
        btnSpeak = findViewById(R.id.btnSpeak);
        ivMic = findViewById(R.id.ivMic);

        btnSpeak.setOnClickListener(v -> startSpeechRecognition());
    }

    private void startSpeechRecognition() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        startActivityForResult(intent, SPEECH_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SPEECH_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            ArrayList<String> results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (results != null && !results.isEmpty()) {
                String userPronunciation = results.get(0).toLowerCase();
                evaluatePronunciation(userPronunciation);
            }
        }
    }

    private void evaluatePronunciation(String userPronunciation) {
        if (userPronunciation.equalsIgnoreCase(currentWord)) {
            currentScore = 10; // Perfect match
            tvFeedback.setText("Good job!");
        } else {
            // Basic similarity check for demo purposes
            int score = calculateSimilarity(currentWord, userPronunciation);
            currentScore = score;
            tvFeedback.setText(score > 5 ? "Almost there!" : "Try again.");
        }
        tvScore.setText("Score: " + currentScore + "/10");
    }

    private int calculateSimilarity(String correctWord, String userWord) {
        // Basic similarity logic: count matching characters
        int matches = 0;
        int length = Math.min(correctWord.length(), userWord.length());
        for (int i = 0; i < length; i++) {
            if (correctWord.charAt(i) == userWord.charAt(i)) {
                matches++;
            }
        }
        return (matches * 10) / correctWord.length(); // Scale to 10
    }
}
