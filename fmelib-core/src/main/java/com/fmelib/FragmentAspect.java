package com.fmelib;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class FragmentAspect {

    @Pointcut("execution(@com.fmelib.RunWhenResumed void *(..))")
    public void method() {
    }

    @Around("method()")
    public void runWhenResumed(final ProceedingJoinPoint joinPoint) throws Throwable {
        Log.d("FragmentAspect", "runWhenResumed method");
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                Log.d("FragmentAspect", "runWhenResumed runnable");
                try {
                    joinPoint.proceed();
                } catch (Throwable e) {
                }
            }
        });
    }

}
