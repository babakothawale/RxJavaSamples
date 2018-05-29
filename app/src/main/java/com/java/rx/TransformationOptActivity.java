package com.java.rx;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class TransformationOptActivity extends AppCompatActivity {

    private static final String TAG = TransformationOptActivity.class.getSimpleName();
    Disposable disposable;
    TextView mTextViewResult;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transformation_opt);

        mTextViewResult = findViewById(R.id.textview_result);

        RxView.clicks(findViewById(R.id.button_map))
                .debounce(600, TimeUnit.MILLISECONDS)
                .observeOn(rx.android.schedulers.AndroidSchedulers.mainThread())
                .doOnError((error)->{})
                .subscribe(aVoid -> {
                    callMap();
                });

        RxView.clicks(findViewById(R.id.button_flatmap))
                .debounce(600, TimeUnit.MILLISECONDS)
                .observeOn(rx.android.schedulers.AndroidSchedulers.mainThread())
                .doOnError((error)->{})
                .subscribe(aVoid -> {
                    callFlatMap();
                });

        RxView.clicks(findViewById(R.id.button_concatmap))
                .debounce(600, TimeUnit.MILLISECONDS)
                .observeOn(rx.android.schedulers.AndroidSchedulers.mainThread())
                .doOnError((error)->{})
                .subscribe(aVoid -> {
                    callConcatMap();
                });

        RxView.clicks(findViewById(R.id.button_switchmap))
                .debounce(600, TimeUnit.MILLISECONDS)
                .observeOn(rx.android.schedulers.AndroidSchedulers.mainThread())
                .doOnError((error)->{})
                .subscribe(aVoid -> {
                    callSwitchMap();
                });

        RxView.clicks(findViewById(R.id.button_buffer))
                .debounce(600, TimeUnit.MILLISECONDS)
                .observeOn(rx.android.schedulers.AndroidSchedulers.mainThread())
                .doOnError((error)->{})
                .subscribe(aVoid -> {
                    callBuffer();
                });
    }
    private void callMap(){
        TransformationOperators.optMap().subscribe(getObserver());
    }

    private void callFlatMap(){
        TransformationOperators.optFlatMap().subscribe(getObserver());
    }

    private void callSwitchMap(){
        TransformationOperators.optSwitchMap().subscribe(getObserver());
    }

    private void callConcatMap(){
        TransformationOperators.optConcatMap().subscribe(getObserver());
    }

    private void callBuffer(){
        TransformationOperators.optBuffer().subscribe(getObserver());
    }

    private Observer getObserver() {
        return new Observer<Object>() {
            @Override
            public void onSubscribe(Disposable d) {
                disposable = d;
                mTextViewResult.setText("");
            }

            @Override
            public void onNext(Object object) {
                mTextViewResult.setText(mTextViewResult.getText() + " " + String.valueOf(object));
            }

            @Override
            public void onError(Throwable e) {
                mTextViewResult.setText(mTextViewResult.getText() + " error");
            }

            @Override
            public void onComplete() {
                mTextViewResult.setText(mTextViewResult.getText() + " onComplete");
            }
        };
    }
}
