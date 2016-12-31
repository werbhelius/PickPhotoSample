package com.werb.pickphotoview.util;

import com.werb.pickphotoview.util.event.RxEvent;

import rx.Observable;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;

/**
 * Created by wanbo on 2016/12/31.
 */

public class RxBus {

    public static RxBus mInstance;

    private final Subject<Object, Object> mBus = new SerializedSubject<>(PublishSubject.create());

    public static RxBus getInstance() {
        if (mInstance == null) {
            mInstance = new RxBus();
        }
        return mInstance;
    }

    public void send(RxEvent event) {
        mBus.onNext(event);
    }

    public <T> Observable<T> toObservable (Class<T> eventType) {
        return mBus.ofType(eventType);
    }

}
