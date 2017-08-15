package com.minicart.android.baselibrary.rxjava;


import android.support.annotation.NonNull;

import io.reactivex.Flowable;
import io.reactivex.processors.FlowableProcessor;
import io.reactivex.processors.PublishProcessor;

/**
 * @类名：RxBus
 * @描述：
 * @创建人：54506
 * @创建时间：2017/1/5 13:33
 * @版本：
 */

public class RxBus {
    private final FlowableProcessor<Object> bus;

    private RxBus() {
        bus = PublishProcessor.create().toSerialized();
    }

    public static synchronized RxBus getDefault() {
        return RxBusInstance.INSTANCE;
    }

    public synchronized <T extends IRxBusEvent> Flowable<T> getClassSubject(Class<T> eventType) {
        return bus.ofType(eventType);
    }

    public <T extends IRxBusEvent> void post(@NonNull T t) {
        synchronized (this) {
            bus.onNext(t);
        }
    }

    private static class RxBusInstance {
        private static final RxBus INSTANCE = new RxBus();
    }

}
