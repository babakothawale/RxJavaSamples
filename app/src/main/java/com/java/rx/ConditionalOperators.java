package com.java.rx;

import android.util.Log;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

public class ConditionalOperators {

    private static final String TAG = ConditionalOptActivity.class.getSimpleName();

    protected static <T> ObservableTransformer<T, T> applySchedulers() {
        return upstream -> upstream.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    protected static <T> ObservableTransformer<T, T> showDebugMessages(final String operatorName) {

        return (observable) -> observable
                .doOnSubscribe((disposable) -> Log.v(TAG, operatorName + ".doOnSubscribe"))
                .doOnDispose(() -> Log.v(TAG, operatorName + ".doOnDispose"))
                .doOnNext((doOnNext) -> Log.v(TAG, operatorName + ".doOnNext. Data: " + doOnNext))
                .doOnComplete(() -> Log.v(TAG, operatorName + ".doOnCompleted"))
                .doOnTerminate(() -> Log.v(TAG, operatorName + ".doOnTerminate"))
                .doOnError((throwable) -> Log.v(TAG, operatorName + ".doOnError: " + throwable.getMessage()));
    }

    public static Observable<Integer> optRepeatWhen() {
        return Observable.range(1, 10)
                .repeatWhen((observable) ->
                        observable.delay(2, TimeUnit.SECONDS))
                .compose(applySchedulers())
                .compose(showDebugMessages("repeatWhen"));
    }


    public static Observable<Integer> optTakeUntil() {
        return Observable.range(1, 10)
                .takeUntil(new Predicate<Integer>() {
                    @Override
                    public boolean test(Integer integer) throws Exception {
                        return integer == 8;
                    }
                })
                .compose(applySchedulers())
                .compose(showDebugMessages("takeUntil"));
    }


    public static Observable<Integer> optTakeWhile() {
        return Observable.just(2,4,6,8,10,11,12)
                .takeWhile(new Predicate<Integer>() {
                    @Override
                    public boolean test(Integer integer) throws Exception {
                        return integer % 2 == 0;
                    }
                })
                .compose(applySchedulers())
                .compose(showDebugMessages("takeWhile"));
    }
}
