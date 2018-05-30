package com.java.rx;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observer;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class CombiningOptActivity extends AppCompatActivity {

    private static final String TAG = CombiningOptActivity.class.getSimpleName();
    CompositeDisposable disposables = new CompositeDisposable();
    TextView mTextViewResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_combining_opt);

        mTextViewResult = findViewById(R.id.textview_result);

        RxView.clicks(findViewById(R.id.button_merge))
                .debounce(300, TimeUnit.MILLISECONDS)
                .observeOn(rx.android.schedulers.AndroidSchedulers.mainThread())
                .doOnError((error)->{})
                .subscribe(aVoid -> {
                    callMerge();
                });

        RxView.clicks(findViewById(R.id.button_zip))
                .debounce(300, TimeUnit.MILLISECONDS)
                .observeOn(rx.android.schedulers.AndroidSchedulers.mainThread())
                .doOnError((error)->{})
                .subscribe(aVoid -> {
                    callZip();
                });

        RxView.clicks(findViewById(R.id.button_combinelatest))
                .debounce(300, TimeUnit.MILLISECONDS)
                .observeOn(rx.android.schedulers.AndroidSchedulers.mainThread())
                .doOnError((error)->{})
                .subscribe(aVoid -> {
                    callCombineLatest();
                });
    }

    private void callMerge(){
        CombiningOperators.optMerge().subscribe(getObserver());
    }

    private void callZip(){
        CombiningOperators.optZip().subscribe(getObserver());
    }
    private void callCombineLatest(){
        CombiningOperators.optCombine().subscribe(getObserver());
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
        disposables.clear();
    }
}
