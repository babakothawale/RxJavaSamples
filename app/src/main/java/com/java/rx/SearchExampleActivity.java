package com.java.rx;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.SearchView;
import android.widget.TextView;

import com.jakewharton.rxbinding.widget.RxSearchView;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Scheduler;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class SearchExampleActivity extends AppCompatActivity {

    private TextView mTextViewResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rx_android);
        mTextViewResult = (TextView) findViewById(R.id.textView);
        SearchView searchView = findViewById(R.id.editText);

        RxSearchView.queryTextChanges(searchView)
                .debounce(500, TimeUnit.MILLISECONDS)
                .filter(s -> !TextUtils.isEmpty(s))
                .distinctUntilChanged()
                .switchMap(s -> doSearch(s.toString()))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mTextViewResult::setText, error -> {
                    error.printStackTrace();
                    mTextViewResult.setText(error.getMessage());
                });
    }

    private Observable<String> doSearch(String searchQuery) {
        Log.d("search", "doSearch: for " + searchQuery);
        Scheduler.Worker worker = AndroidSchedulers.mainThread().createWorker();
        worker.schedule(() -> mTextViewResult.setText("Searching for : " + searchQuery));
        return Observable.just("Search Result for " + searchQuery)
                .delay(3, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io());
    }


}
