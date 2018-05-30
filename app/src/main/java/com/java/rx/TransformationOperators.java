package com.java.rx;


import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class TransformationOperators {



    public static Observable<String> optMap() {

        return Observable.fromArray(1, 2, 3)
                .map(new Function<Integer, String>() {
                    @Override
                    public String apply(Integer integer) throws Exception {
                        return String.valueOf(integer) + " as string \n";
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static Observable<String> optFlatMap() {

        return Observable.fromArray(1, 2, 3, 4, 5)
                .flatMap(new Function<Integer, ObservableSource<String>>() {
                             @Override
                             public ObservableSource<String> apply(Integer integer) throws Exception {
                                 return Observable.just(integer + "x");
                             }
                         }
                )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * Ignores the previous item when emitted new one
     * @return
     */
    public static Observable<String> optSwitchMap() {

        return Observable.fromArray(1, 2, 3, 4, 5)
                .switchMap(new Function<Integer, ObservableSource<String>>() {
                               @Override
                               public ObservableSource<String> apply(Integer integer) throws Exception {
                                   return Observable.just(integer + "x");
                               }
                           }
                )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static Observable<String> optConcatMap() {

        return Observable.fromArray(1, 2, 3, 4, 5)
                .concatMap(new Function<Integer, ObservableSource<String>>() {
                               @Override
                               public ObservableSource<String> apply(Integer integer) throws Exception {
                                   return Observable.just(integer + "x");
                               }
                           }
                )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static Observable optBuffer() {

        return Observable.fromArray(1, 2, 3, 4, 5,6)
                .buffer(3)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }


}
