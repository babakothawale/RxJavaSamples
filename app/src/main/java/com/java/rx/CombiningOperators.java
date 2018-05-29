package com.java.rx;

import io.reactivex.Observable;
import io.reactivex.functions.BiFunction;

public class CombiningOperators {
    static Observable<Integer> observableInt1 = Observable.fromArray(1, 2, 3, 4, 5);
    static Observable<Integer> observableInt2 = Observable.fromArray(11, 12, 13, 14, 15);//Emits integers
    static Observable<String> observableAlpha = Observable.fromArray("A", "B", "C", "D", "F");  //Emits alphabets

    /**
     * merge can interleave the outputs
     *
     * @return
     */
    public static Observable<Integer> optMerge() {
        return Observable.merge(observableInt1, observableInt2);
    }

    static class ZipObject {
        int number;
        String alphabet;

        @Override
        public String toString() {
            return "[" + number + "::" + alphabet + "]";
        }
    }

    public static Observable<ZipObject> optZip() {

        return Observable.zip(observableInt1, observableAlpha,
                //Function that define how to zip outputs of both the stream into single object.
                new BiFunction<Integer, String, ZipObject>() {

                    @Override
                    public ZipObject apply(Integer integer, String s) throws Exception {
                        ZipObject zipObject = new ZipObject();
                        zipObject.alphabet = s;
                        zipObject.number = integer;
                        return zipObject;
                    }
                });
    }

    static class CombineObject {
        int number;
        String alphabet;

        @Override
        public String toString() {
            return "[" + number + "::" + alphabet + "]";
        }
    }

    public static Observable<CombineObject> optCombine() {

        return Observable.combineLatest(observableInt1, observableAlpha, new BiFunction<Integer, String, CombineObject>() {
            @Override
            public CombineObject apply(Integer integer, String s) throws Exception {
                CombineObject combineObject=new CombineObject();
                combineObject.number=integer;
                combineObject.alphabet=s;
                return combineObject;
            }
        });
    }



}
