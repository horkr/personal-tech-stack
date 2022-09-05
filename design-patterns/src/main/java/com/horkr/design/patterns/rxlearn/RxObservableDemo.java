package com.horkr.design.patterns.rxlearn;


import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author 卢亮宏
 */
public class RxObservableDemo {
    private static final Logger log = LoggerFactory.getLogger(RxObservableDemo.class);

    // 基础用法 注：整体方法调用顺序：观察者.onSubscribe（）> 被观察者.subscribe（）> 观察者.onNext（）>观察者.onComplete()
    private static void baseExample(){
        // 创建观察者
        ObservableOnSubscribe<Integer> observableOnSubscribe = new ObservableOnSubscribe<Integer>() {
            // 1. 创建被观察者 & 生产事件
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                emitter.onNext(1);
                emitter.onNext(2);
                emitter.onNext(3);
                emitter.onComplete();
            }
        };
        //创建观察者 & 定义响应事件的行为
        Observer<Integer> observer = new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {
                log.info("开始采用subscribe连接");
            }
            // 默认最先调用复写的 onSubscribe（）

            @Override
            public void onNext(Integer value) {
                log.info("对Next事件" + value + "作出响应");
            }

            @Override
            public void onError(Throwable e) {
                log.info("对Error事件作出响应");
            }

            @Override
            public void onComplete() {
                log.info("对Complete事件作出响应");
            }
        };

//        通过通过订阅（subscribe）连接观察者和被观察者
        Observable.create(observableOnSubscribe).subscribe(observer);

    }

    private static void consumeExample(){
        Observable.just("hello").subscribe(new Consumer<String>() {
            // 每次接收到Observable的事件都会调用Consumer.accept（）
            @Override
            public void accept(String s) throws Exception {
                System.out.println(s);
            }
        });
    }

    public static void main(String[] args) {
        consumeExample();
    }
}
