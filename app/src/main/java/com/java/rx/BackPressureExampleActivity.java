package com.java.rx;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;


import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.processors.PublishProcessor;
import io.reactivex.schedulers.Schedulers;


public class BackPressureExampleActivity extends AppCompatActivity {

    private static final String TAG = "BackPressureEx";
    private TextView mTextViewData;
    private TextView mTextViewResult;
    private CompositeDisposable disposables = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_back_pressure_example);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mTextViewData = findViewById(R.id.textview_data);
        mTextViewResult = findViewById(R.id.textview_result);
        findViewById(R.id.button_back_pressure)
                .setOnClickListener(view -> startBackPressureTest());

    }

    protected <T> ObservableTransformer<T, T> applySchedulers() {
        return upstream -> upstream.subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private void startBackPressureTest() {
        // Emit one item per millisecond...
         Observable
                .interval(1, TimeUnit.MILLISECONDS)

                .doOnNext((number) -> {
                    Log.v(TAG, "doOnNext() - Emitted number: " + number);
                    final Scheduler.Worker w = AndroidSchedulers.mainThread().createWorker();
                    w.schedule(() -> mTextViewData.setText(mTextViewData.getText() + " " + number));
                }).subscribeOn(Schedulers.io())

                //.compose(applySchedulers())

                // Sleep for 100ms for each emitted item. This will make we receive a BackpressureMissingException quickly.
                .subscribe(getObserver(1000));
    }


    @NonNull
    private Observer<Object> getObserver(final Integer consumeSlowByTime) {
        return new Observer<Object>() {

            @Override
            public void onSubscribe(Disposable d) {
                disposables.add(d);
                mTextViewResult.setText("");
            }

            @Override
            public void onComplete() {
                final rx.Scheduler.Worker w2 = rx.android.schedulers.AndroidSchedulers.mainThread().createWorker();
                w2.schedule(() -> mTextViewResult.setText(mTextViewResult.getText() + " - onCompleted"));
            }

            @Override
            public void onError(final Throwable e) {
                final rx.Scheduler.Worker w2 = rx.android.schedulers.AndroidSchedulers.mainThread().createWorker();
                w2.schedule(() -> mTextViewResult.setText(mTextViewResult.getText() + " - doOnError" + e));
            }

            @Override
            public void onNext(final Object number) {
                final rx.Scheduler.Worker w2 = rx.android.schedulers.AndroidSchedulers.mainThread().createWorker();
                w2.schedule(() -> mTextViewResult.setText(mTextViewResult.getText() + " " + number));

                try {
                    Thread.sleep(consumeSlowByTime);
                } catch (InterruptedException e) {
                    Log.v(TAG, "subscribe.onNext. We got a InterruptedException!");
                }
            }
        };

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(disposables != null) {
            disposables.clear();
        }
    }
}
