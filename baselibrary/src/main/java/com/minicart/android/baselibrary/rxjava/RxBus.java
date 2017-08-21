package com.minicart.android.baselibrary.rxjava;


import android.support.annotation.NonNull;

import java.util.HashMap;
import java.util.Map;

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

    public static synchronized RxBus getBus(Class clazz) {
        RxBus rxBus = RxBusInstance.CLASS_MAP.get(clazz);
        if (rxBus == null) {
            rxBus = new RxBus();
            RxBusInstance.CLASS_MAP.put(clazz, rxBus);
        }
        return rxBus;
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
        private static final Map<Class, RxBus> CLASS_MAP = new HashMap<>();
    }

}
