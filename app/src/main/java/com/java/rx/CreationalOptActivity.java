package com.java.rx;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observables.ConnectableObservable;
import io.reactivex.schedulers.Schedulers;

public class CreationalOptActivity extends AppCompatActivity {

    private static final String TAG = CreationalOptActivity.class.getSimpleName();
    private CompositeDisposable disposables = new CompositeDisposable();
    private TextView mTextViewResult;
    private TextView mTextViewData;
    private Disposable mLoadingDisposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creational_opt);

        mTextViewData = findViewById(R.id.textview_data);
        mTextViewResult = findViewById(R.id.textview_result);

        RxView.clicks(findViewById(R.id.button_just))
                .debounce(600, TimeUnit.MILLISECONDS)
                .observeOn(rx.android.schedulers.AndroidSchedulers.mainThread())
                .doOnError((error) -> {
                })
                .subscribe(aVoid -> {
                    callJust();
                });

        RxView.clicks(findViewById(R.id.button_create))
                .debounce(600, TimeUnit.MILLISECONDS)
                .observeOn(rx.android.schedulers.AndroidSchedulers.mainThread())
                .doOnError((error) -> {
                })
                .subscribe(aVoid -> {
                    callCreate();
                });

        RxView.clicks(findViewById(R.id.button_iterable))
                .debounce(600, TimeUnit.MILLISECONDS)
                .observeOn(rx.android.schedulers.AndroidSchedulers.mainThread())
                .doOnError((error) -> {
                })
                .subscribe(aVoid -> {
                    callIterable();
                });

        RxView.clicks(findViewById(R.id.button_array))
                .debounce(600, TimeUnit.MILLISECONDS)
                .observeOn(rx.android.schedulers.AndroidSchedulers.mainThread())
                .doOnError((error) -> {
                })
                .subscribe(aVoid -> {
                    callFromArray();
                });
        RxView.clicks(findViewById(R.id.button_callable))
                .debounce(600, TimeUnit.MILLISECONDS)
                .observeOn(rx.android.schedulers.AndroidSchedulers.mainThread())
                .doOnError((error) -> {
                })
                .subscribe(aVoid -> {
                    callCallable();
                });

        RxView.clicks(findViewById(R.id.button_connectible))
                .debounce(600, TimeUnit.MILLISECONDS)
                .observeOn(rx.android.schedulers.AndroidSchedulers.mainThread())
                .doOnError((error) -> {
                })
                .subscribe(aVoid -> {
                    createConnectible();
                });

        RxView.clicks(findViewById(R.id.button_cache))
                .debounce(600, TimeUnit.MILLISECONDS)
                .observeOn(rx.android.schedulers.AndroidSchedulers.mainThread())
                .doOnError((error) -> {
                })
                .subscribe(aVoid -> {
                    callOptCache1();
                });

        RxView.clicks(findViewById(R.id.button_cache1))
                .debounce(600, TimeUnit.MILLISECONDS)
                .observeOn(rx.android.schedulers.AndroidSchedulers.mainThread())
                .doOnError((error) -> {
                })
                .subscribe(aVoid -> {
                    callOptCache2();
                });
        RxView.clicks(findViewById(R.id.button_refcount))
                .debounce(600, TimeUnit.MILLISECONDS)
                .observeOn(rx.android.schedulers.AndroidSchedulers.mainThread())
                .doOnError((error) -> {
                })
                .subscribe(aVoid -> {
                    callRefCount1();
                });
        RxView.clicks(findViewById(R.id.button_refcount2))
                .debounce(600, TimeUnit.MILLISECONDS)
                .observeOn(rx.android.schedulers.AndroidSchedulers.mainThread())
                .doOnError((error) -> {
                })
                .subscribe(aVoid -> {
                    callRefCount2();
                });
    }

    private void callRefCount1() {
        CreationalOperators.optRefCount().observeOn(AndroidSchedulers.mainThread())
                .subscribe(getObserver(mTextViewData));
    }
    private void callRefCount2() {
        CreationalOperators.optRefCount().observeOn(AndroidSchedulers.mainThread())
                .subscribe(getObserver(mTextViewResult));
    }

    private void callOptCache1() {
        CreationalOperators.optCache().
                observeOn(AndroidSchedulers.mainThread())
                .subscribe(getObserver(mTextViewData));
    }
    private void callOptCache2() {
        CreationalOperators.optCache().
                observeOn(AndroidSchedulers.mainThread())
                .subscribe(getObserver(mTextViewResult));
    }

    private void callJust() {
        CreationalOperators.optJust().subscribe(getObserver());
    }

    private void callCreate() {
        CreationalOperators.optCreate().subscribe(getObserver());
    }

    private void callIterable() {
        CreationalOperators.optFromIterable().subscribe(getObserver());
    }

    private void callFromArray() {
        CreationalOperators.optFromArray().subscribe(getObserver());
    }

    private void callCallable() {
        if (mLoadingDisposable != null && !mLoadingDisposable.isDisposed()) {
            mLoadingDisposable.dispose();
        }

        Observable.just("*")
                .repeatWhen(completed -> completed.delay(500, TimeUnit.MILLISECONDS))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    int count;

                    @Override
                    public void onSubscribe(Disposable d) {
                        mLoadingDisposable = d;
                    }

                    @Override
                    public void onNext(String s) {
                        mTextViewResult.setText(mTextViewResult.getText() + " " + s);
                        count++;
                        if (count == 4) {
                            count = 0;
                            mTextViewResult.setText("");
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
        CreationalOperators.optFromCallable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getObserver());
    }

    private void createConnectible() {
        ConnectableObservable<Integer> connectible = CreationalOperators.optPublish();
        connectible
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposables.add(d);
                        Log.d(TAG, "onSubscribe: Observer 1");
                    }

                    @Override
                    public void onNext(Integer integer) {
                        Log.d(TAG, "onNext: Observer 1 : " + integer);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError: Observer 1 : ", e);
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete: Observer 1 : ");
                    }
                });       // onCompleted

        connectible
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposables.add(d);
                        Log.d(TAG, "onSubscribe: Observer 2");
                    }

                    @Override
                    public void onNext(Integer integer) {
                        Log.d(TAG, "onNext: Observer 2 : " + integer);
                        if(integer == 5) connectNext(connectible);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError: Observer 2 : ", e);
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete: Observer 2 : ");
                    }
                });      // onCompleted

        connectible.connect();
    }

    private void connectNext(ConnectableObservable<Integer> connectible){

        connectible
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposables.add(d);
                        Log.d(TAG, "onSubscribe: Observer 3");
                    }

                    @Override
                    public void onNext(Integer integer) {
                        Log.d(TAG, "onNext: Observer 3 : " + integer);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError: Observer 3 : ", e);
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete: Observer 3 : ");
                    }
                });
    }


    private Observer getObserver(TextView resultView) {
        return new Observer<Object>() {
            @Override
            public void onSubscribe(Disposable d) {
                disposables.add(d);
                resultView.setText("");
            }

            @Override
            public void onNext(Object object) {
                if (mLoadingDisposable != null) {
                    mLoadingDisposable.dispose();
                    mLoadingDisposable = null;
                    resultView.setText("");
                }
                resultView.setText(resultView.getText() + " " + String.valueOf(object));
            }

            @Override
            public void onError(Throwable e) {
                resultView.setText(resultView.getText() + " error");
            }

            @Override
            public void onComplete() {
                resultView.setText(resultView.getText() + " onComplete");
            }
        };
    }

    private Observer getObserver() {
        return getObserver(mTextViewResult);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (disposables != null) {
            disposables.clear();
        }
    }
}
