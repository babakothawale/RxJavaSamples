package com.java.rx;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxTextView;

public class RxAndroidActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rx_android);
        Button button = (Button) findViewById(R.id.button);
        TextView textView = (TextView) findViewById(R.id.textView);
        EditText editText = (EditText) findViewById(R.id.editText);


        RxView.clicks(button)
                .subscribe(aVoid -> {
                    Toast.makeText(RxAndroidActivity.this, "RxView.clicks", Toast.LENGTH_SHORT).show();
                });

        RxTextView.textChanges(editText)
                .subscribe(textView::setText);
    }
}
