package com.na.utils.rx;

import io.reactivex.Scheduler;

/**
 * Created by oneal23 on 2018/6/26.
 */

public interface SchedulerProvider {

    Scheduler ui();

    Scheduler computation();

    Scheduler io();

}
