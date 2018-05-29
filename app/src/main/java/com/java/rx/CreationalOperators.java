package com.java.rx;

import android.util.Log;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observables.ConnectableObservable;
import io.reactivex.schedulers.Schedulers;

public class CreationalOperators {

    public static Observable<Integer> optJust() {
        return Observable
                .just(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static Observable<Integer> optCreate() {
        return Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {

                emitter.onNext(1);
                emitter.onNext(2);
                emitter.onNext(3);
                emitter.onNext(4);

                emitter.onComplete();
            }
        });
    }

    public static Observable<Integer> optFromArray() {
        return Observable.fromArray(1, 2, 3, 4, 5, 6, 7, 8, 9, 0);
    }

    public static Observable<Integer> optFromIterable() {
        return Observable.fromIterable(Arrays.asList(1,2,3,4,5));
    }

    public static Observable<List<Integer>> optFromCallable(){
      return Observable.fromCallable(new Callable<List<Integer>>() {
          @Override
          public List<Integer> call() throws Exception {
              return Database.readDatabaseValues();
          }
      })
              .subscribeOn(Schedulers.io());
    }

    //HOT observables
    public static ConnectableObservable<Integer> optPublish(){
        return Observable.range( 1, 10 )
                .concatMap(i-> Observable.just(i).delay(500, TimeUnit.MILLISECONDS))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .publish();
    }

    private static Observable<Integer> mCacheObservable;
    //HOT observable
    static Observable<Integer> optCache() {
        if(mCacheObservable == null) {
            mCacheObservable = Observable
                    .range(1,10)
                    .concatMap(i-> Observable.just(i).delay(750, TimeUnit.MILLISECONDS))
                    .doOnNext((number) -> {
                        Log.d("optCache", "optCache: doOnNext");
                    })
                    .cache()
                    .concatMap(i-> Observable.just(i).delay(750, TimeUnit.MILLISECONDS));
        }
        return mCacheObservable;
    }

    private static Observable<Long> mRefCountObservable;
    static Observable<Long> optRefCount() {
        if (mRefCountObservable == null) {
            mRefCountObservable = Observable
                    .interval(500, TimeUnit.MILLISECONDS)
                    .doOnNext((number) -> {
                        Log.d("RefCount", "optRefCount: doOnNext:: " + number);
                    })

                    // This will convert the Observable to a ConnectableObservable
                    .publish().refCount();
        }
        return mRefCountObservable;
    }

}
