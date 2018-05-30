package com.java.rx;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.jakewharton.rxbinding.view.RxView;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        RxView.clicks(findViewById(R.id.button_filter))
                 .subscribe(aVoid -> {
                     intentFilterOptActivity();
        });

        RxView.clicks(findViewById(R.id.button_combining))
                .subscribe(aVoid -> {
                    intentCombineOptActivity();
        });

        RxView.clicks(findViewById(R.id.button_transforming))
                .subscribe(aVoid -> {
                    intentTransformingOptActivity();
                });

        
        RxView.clicks(findViewById(R.id.button_simpleObservable))
                 .subscribe(aVoid -> {                           
                     intentCreationalOptActivity();
                 });

        RxView.clicks(findViewById(R.id.button_parallel))
                  .subscribe(aVoid -> {
                      intentParallelExecutionActivity();
                  });

        RxView.clicks(findViewById(R.id.button_conditional))
                .subscribe(aVoid -> {
                    intentConditionalOptActivity();
                });

        RxView.clicks(findViewById(R.id.button_search))
                .subscribe(aVoid -> {
                    startActivity(new Intent(this, SearchExampleActivity.class));
                });
    }

    private void intentCombineOptActivity(){
        Intent intent = new Intent(MainActivity.this,CombiningOptActivity.class);
        startActivity(intent);
    }

    private void intentTransformingOptActivity(){
        Intent intent = new Intent(MainActivity.this,TransformationOptActivity.class);
        startActivity(intent);
    }

    private void intentParallelExecutionActivity(){
        Intent intent = new Intent(MainActivity.this, ParallelExecutionExampleActivity.class);
        startActivity(intent);
    }

    private void intentFilterOptActivity(){

        Intent intent = new Intent(MainActivity.this,FilterOptActivity.class);
        startActivity(intent);

    }
    private void intentCreationalOptActivity(){

       Intent intent = new Intent(MainActivity.this,CreationalOptActivity.class);
       startActivity(intent);

    }

    private void intentConditionalOptActivity(){

        Intent intent = new Intent(MainActivity.this, ConditionalOptActivity.class);
        startActivity(intent);

    }

}


