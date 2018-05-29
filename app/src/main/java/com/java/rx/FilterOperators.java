package com.java.rx;


import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;
import rx.Subscriber;

public class FilterOperators {

    public static Observable<Integer> optFilter() {
        return Observable
                .just(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
                .filter(new Predicate<Integer>() {
                    @Override
                    public boolean test(Integer integer) throws InterruptedException {
                        Thread.sleep(1000);
                        return integer % 2 == 0;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static Observable<Integer> optTake(int takeCount) {
        return Observable.fromArray(1, 2, 3, 4, 5, 6, 7, 8, 9, 0).take(takeCount).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }

    public static Observable<Integer> optTakeLast(int takeLastCount) {
        return Observable.fromArray(new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 0})
                .takeLast(takeLastCount).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }


    public static Observable<Integer> optFirst() {
        return Observable.fromArray(1, 2, 3, 4)
                .first(2).toObservable().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static Observable<Integer> optElementAt() {
        return Observable.fromArray(1, 2, 3, 4, 5, 6, 7, 8, 9, 0).elementAt(4).toObservable().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static Observable<Integer> optSkip(int skipCount) {
        return Observable.fromArray(new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 0})
                .skip(skipCount).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

}
