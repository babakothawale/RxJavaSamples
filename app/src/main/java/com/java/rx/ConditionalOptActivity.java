package com.java.rx;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observer;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class ConditionalOptActivity extends AppCompatActivity {

    private static final String TAG = ConditionalOptActivity.class.getSimpleName();
    private CompositeDisposable disposables = new CompositeDisposable();
    private TextView mTextViewResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conditional_opt);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mTextViewResult = findViewById(R.id.textview_result);
        RxView.clicks(findViewById(R.id.button_repeat_when))
                .debounce(300, TimeUnit.MILLISECONDS)
                .observeOn(rx.android.schedulers.AndroidSchedulers.mainThread())
                .doOnError((error) -> {
                })
                .subscribe(aVoid -> {
                    callRepeatWhen();
                });

        RxView.clicks(findViewById(R.id.button_take_until))
                .debounce(300, TimeUnit.MILLISECONDS)
                .observeOn(rx.android.schedulers.AndroidSchedulers.mainThread())
                .doOnError((error) -> {
                })
                .subscribe(aVoid -> {
                    callTakeUntil();
                });

        RxView.clicks(findViewById(R.id.button_take_while))
                .debounce(300, TimeUnit.MILLISECONDS)
                .observeOn(rx.android.schedulers.AndroidSchedulers.mainThread())
                .doOnError((error) -> {
                })
                .subscribe(aVoid -> {
                    callTakeWhile();
                });
    }

    private void callTakeWhile() {
        ConditionalOperators.optTakeWhile().subscribe(getObserver());
    }

    private void callTakeUntil() {
        ConditionalOperators.optTakeUntil().subscribe(getObserver());
    }

    private void callRepeatWhen() {
        ConditionalOperators.optRepeatWhen().subscribe(getObserver());
    }


    private Observer getObserver() {
        return new Observer<Object>() {
            @Override
            public void onSubscribe(Disposable d) {
                disposables.add(d);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (disposables != null) {
            disposables.clear();
        }
    }

}
