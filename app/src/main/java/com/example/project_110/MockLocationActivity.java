package com.example.project_110;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class MockLocationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mock_location);
    }

    public void onSaveButtonClick(View view) {
        EditText e = findViewById(R.id.enter_json);
        Intent intent=new Intent();
        intent.putExtra("mockLocation", e.getText().toString());
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    public void onCancelButtonClick(View view) {
        Intent intent=new Intent();
        intent.putExtra("mockLocation", "");
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    public void onMockStartClick(View view) {
        EditText e = findViewById(R.id.enter_json);
        String mockStart = "[{ \"lat\": 32.73561,\n" +
                             "\"lng\": -117.14936}]";
        e.setText(mockStart);
    }
}