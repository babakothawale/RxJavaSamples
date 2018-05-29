package com.baba.rx.sample;


import com.java.rx.TransformationOperators;

import org.junit.Test;


import io.reactivex.ObservableTransformer;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    private static final String TAG = "ExampleUnitTest";

    protected <T> ObservableTransformer<T, T> logTransformer(final String operatorName) {

        return (observable) -> observable
                .doOnSubscribe(disposable -> Log.d(TAG, operatorName + ".doOnSubscribe"))
                .doOnComplete(() -> {
                    Log.d(TAG, operatorName + ".doOnComplete");
                })
                .doOnDispose(() -> {
                    Log.d(TAG, operatorName + ".doOnDispose");
                })
                //.doOnUnsubscribe(() -> Log.d(TAG, operatorName + ".doOnUnsubscribe"))
                .doOnNext((doOnNext) -> Log.d(TAG, operatorName + ".doOnNext. Data: " + doOnNext))
                //.doOnCompleted(() -> Log.d(TAG, operatorName + ".doOnCompleted"))
                .doOnTerminate(() -> Log.d(TAG, operatorName + ".doOnTerminate"))
                .doOnError((throwable) -> Log.d(TAG, operatorName + ".doOnError: " + throwable.getMessage()));
    }
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void optFlatMap(){
        TransformationOperators.optFlatMap()
                //.compose(logTransformer("FlatMap"))
        .subscribe();
    }
}