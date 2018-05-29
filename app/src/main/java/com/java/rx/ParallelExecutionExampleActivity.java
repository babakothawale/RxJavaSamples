package com.java.rx;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class ParallelExecutionExampleActivity extends AppCompatActivity {

    private static final String TAG = "ParallelExecution";
    private static final Integer MAX_CONCURRENT = 20;
    private CompositeDisposable disposables = new CompositeDisposable();
    private TextView mTextViewResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parallel_execution_example);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mTextViewResult = findViewById(R.id.textview_result);


        RxView.clicks(findViewById(R.id.button_new_thread))
                .debounce(600, TimeUnit.MILLISECONDS)
                .observeOn(rx.android.schedulers.AndroidSchedulers.mainThread())
                .doOnError((error) -> {
                })
                .subscribe(aVoid -> {
                    newThreadExample();

                });

        RxView.clicks(findViewById(R.id.button_multiple_thread))
                .debounce(600, TimeUnit.MILLISECONDS)
                .observeOn(rx.android.schedulers.AndroidSchedulers.mainThread())
                .doOnError((error) -> {
                })
                .subscribe(aVoid -> {

                    multipleThreadsExample();

                });


        RxView.clicks(findViewById(R.id.button_using_computation))
                .debounce(600, TimeUnit.MILLISECONDS)
                .observeOn(rx.android.schedulers.AndroidSchedulers.mainThread())
                .doOnError((error) -> {
                })
                .subscribe(aVoid -> {

                    parallelExecutionUsingComputation();

                });

        RxView.clicks(findViewById(R.id.button_max_thread))
                .debounce(600, TimeUnit.MILLISECONDS)
                .observeOn(rx.android.schedulers.AndroidSchedulers.mainThread())
                .doOnError((error) -> {
                })
                .subscribe(aVoid -> {

                     maxParallelThreads();

                });

        RxView.clicks(findViewById(R.id.button_max_thread_io))
                .debounce(600, TimeUnit.MILLISECONDS)
                .observeOn(rx.android.schedulers.AndroidSchedulers.mainThread())
                .doOnError((error) -> {
                })
                .subscribe(aVoid -> {

                    maxParallelThreadWithIO();

                });


        RxView.clicks(findViewById(R.id.button_max_thread_with_avl_thread))
                .debounce(600, TimeUnit.MILLISECONDS)
                .observeOn(rx.android.schedulers.AndroidSchedulers.mainThread())
                .doOnError((error) -> {
                })
                .subscribe(aVoid -> {

                    maxParallelWithAvailableThread();
                });


    }

    protected <T> ObservableTransformer<T, T> showDebugMessages(final String operatorName) {

        return (observable) -> observable
                .doOnSubscribe((disposable) -> Log.v(TAG, operatorName + ".doOnSubscribe"))
                .doOnDispose(() -> Log.v(TAG, operatorName + ".doOnDispose"))
                .doOnNext((doOnNext) -> Log.v(TAG, operatorName + ".doOnNext. Data: " + doOnNext))
                .doOnComplete(() -> Log.v(TAG, operatorName + ".doOnCompleted"))
                .doOnTerminate(() -> Log.v(TAG, operatorName + ".doOnTerminate"))
                .doOnError((throwable) -> Log.v(TAG, operatorName + ".doOnError: " + throwable.getMessage()));
    }


    private void newThreadExample() {

        Observable.range(0, 10)
                .flatMap(number -> Observable
                        .just(number)
                        .subscribeOn(Schedulers.newThread())
                        .map(n -> heavyDataProcessing(n, 1000, 5000)
                        ))
                .compose(this.showDebugMessages("flatMap"))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getObserver());
    }

    private void multipleThreadsExample() {

        Observable.range(0, 100)
                .flatMap(number -> Observable
                        .just(number)
                        .subscribeOn(Schedulers.newThread())
                        .map(n -> heavyDataProcessing(n, 1000, 2000))
                )
                .compose(this.showDebugMessages("flatMap"))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getObserver());
    }

    private void parallelExecutionUsingComputation() {

        Observable.range(0, 10)
                .flatMap(number -> Observable
                        .just(number)
                        // Schedulers.computation() will limit the number of simultaneously threads based on the number
                        // of available processors.
                        .subscribeOn(Schedulers.computation())
                        .map(n -> heavyDataProcessing(n, 2000, 5000))
                )
                .compose(this.showDebugMessages("flatMap"))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getObserver());
    }

    private void maxParallelThreads() {

        Observable.range(0, 60)
                .flatMap(number -> Observable
                                .just(number)
                                .subscribeOn(Schedulers.newThread())
                                .map(n -> heavyDataProcessing(n, 4000, 6000)),
                        // Even when requesting tasks to be executed using a new thread, system will limit the number of simultaneously
                        // treads up to 20, due this flatmap maxConcurrent parameter.
                        MAX_CONCURRENT
                )
                .compose(this.showDebugMessages("flatMap"))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getObserver());
    }

    private void maxParallelThreadWithIO() {

        Observable.range(0, 60)
                .flatMap(number -> Observable
                                .just(number)
                                .subscribeOn(Schedulers.io())
                                .map(n -> heavyDataProcessing(n, 3000, 4000)),
                        MAX_CONCURRENT
                )
                .compose(this.showDebugMessages("flatMap"))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getObserver());
    }

    private void maxParallelWithAvailableThread() {

        // Get the number of available processors on the device and create a scheduler based on it.
        int numberOfThreads = Runtime.getRuntime().availableProcessors() + 1;
        final ExecutorService executor = Executors.newFixedThreadPool(numberOfThreads);
        final Scheduler scheduler = Schedulers.from(executor);

        Observable.range(0, 40)
                .flatMap(number -> Observable
                        .just(number)
                        // Since we are using a scheduler from an executor, we can control how many threads
                        // will really run in parallel.
                        .subscribeOn(scheduler)
                        .map(n -> heavyDataProcessing(n, 3000, 5000))
                )
                .doFinally(() -> executor.shutdown())
                .compose(this.showDebugMessages("flatMap"))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getObserver());
    }


    private void parallelUsingFlowable(){
       Flowable<Integer> flowable  =
               Flowable.range(0, 40)
                .parallel(10)
                .runOn(Schedulers.io())
                .map(this::heavyLoading)
                .sequential()
                       .observeOn(AndroidSchedulers.mainThread());

        Disposable disposable = flowable.subscribe(integer -> mTextViewResult.setText(mTextViewResult.getText() + "\n" + integer),
                throwable -> mTextViewResult.setText(mTextViewResult.getText() + " error - " + throwable.getMessage()));
        disposables.add(disposable);
    }

    private Integer heavyLoading(Integer id) {
        return heavyDataProcessing(id, 2000, 3000);
    }

    /**
     * This is a helper method which just sleeps for a while in order to simulate a heavy data processing.
     *
     * @param number
     * @param minSleepTime
     * @param maxSleepTime
     * @return
     */
    private Integer heavyDataProcessing(Integer number, Integer minSleepTime, Integer maxSleepTime) {
        try {

            Integer timeToSleep = new Random().nextInt(maxSleepTime - minSleepTime) + minSleepTime;
            final String msg = "Processing data " + number + " on thread: " + Thread.currentThread().getName() + " - Sleeping for " + timeToSleep + "ms...";
            Log.d(TAG, msg);

//            final Scheduler.Worker w = AndroidSchedulers.mainThread().createWorker();
//            w.schedule(() -> {
//                Log.d(TAG, "heavyDataProcessing: " + msg);
//            });

            Thread.sleep(timeToSleep);

            return number;

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
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
                mTextViewResult.setText(mTextViewResult.getText() + "\n" + String.valueOf(object));
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
        if(disposables != null ){
            disposables.clear();
        }
    }
}
