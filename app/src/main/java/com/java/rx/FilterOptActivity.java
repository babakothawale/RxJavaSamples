package com.java.rx;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observer;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class FilterOptActivity extends AppCompatActivity {

    private static final String TAG = FilterOptActivity.class.getSimpleName();
    CompositeDisposable disposables = new CompositeDisposable();
    TextView mTextViewResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_opt);

        mTextViewResult = findViewById(R.id.textview_result);

        RxView.clicks(findViewById(R.id.button_filter))
                .debounce(600, TimeUnit.MILLISECONDS)
                .observeOn(rx.android.schedulers.AndroidSchedulers.mainThread())
                .doOnError((error)->{})
                .subscribe(aVoid -> {
                    callFilter();
                });

        RxView.clicks(findViewById(R.id.button_take))
                .debounce(600, TimeUnit.MILLISECONDS)
                .observeOn(rx.android.schedulers.AndroidSchedulers.mainThread())
                .doOnError((error)->{})
                .subscribe(aVoid -> {
                    callTake();
                });

        RxView.clicks(findViewById(R.id.button_takelast))
                .debounce(600, TimeUnit.MILLISECONDS)
                .observeOn(rx.android.schedulers.AndroidSchedulers.mainThread())
                .doOnError((error)->{})
                .subscribe(aVoid -> {
                    callTakeLast();
                });

        RxView.clicks(findViewById(R.id.button_first))
                .debounce(600, TimeUnit.MILLISECONDS)
                .observeOn(rx.android.schedulers.AndroidSchedulers.mainThread())
                .doOnError((error)->{})
                .subscribe(aVoid -> {
                    callFirst();
                });

        RxView.clicks(findViewById(R.id.button_elementat))
                .debounce(600, TimeUnit.MILLISECONDS)
                .observeOn(rx.android.schedulers.AndroidSchedulers.mainThread())
                .doOnError((error)->{})
                .subscribe(aVoid -> {
                    callElementAt();
                });

        RxView.clicks(findViewById(R.id.button_skip))
                .debounce(600, TimeUnit.MILLISECONDS)
                .observeOn(rx.android.schedulers.AndroidSchedulers.mainThread())
                .doOnError((error)->{})
                .subscribe(aVoid -> {
                    callSkip();
                });
    }


    private void callFilter(){
        FilterOperators.optFilter().subscribe(getObserver());
    }

    private void callTake(){
        FilterOperators.optTake(3).subscribe(getObserver());
    }

    private void callTakeLast(){
        FilterOperators.optTakeLast(4).subscribe(getObserver());
    }

    private void callFirst(){
        FilterOperators.optFirst().subscribe(getObserver());
    }

    private void callElementAt(){
        FilterOperators.optElementAt().subscribe(getObserver());
    }

    private void callSkip(){
        FilterOperators.optSkip(2).subscribe(getObserver());
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
        if(disposables != null) {
            disposables.clear();
        }
    }
}
