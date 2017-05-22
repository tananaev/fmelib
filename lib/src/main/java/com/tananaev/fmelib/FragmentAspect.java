package com.tananaev.fmelib;

import android.app.Fragment;
import android.util.Log;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class FragmentAspect {
    public static final String LIB_TAG = "FMElib: ";

    @Pointcut("execution(@com.tananaev.fmelib.RunWhenResumed void *(..))")
    public void method() {
    }

    @Around("method()")
    public void runWhenResumed(final ProceedingJoinPoint joinPoint) throws Throwable {
        Object classInstance = joinPoint.getThis();
        final String TAG = LIB_TAG + classInstance.getClass().getSimpleName();
        if (classInstance instanceof Resumer &&
                (classInstance instanceof Fragment
                        || classInstance instanceof android.support.v4.app.Fragment)) {
            Log.d(TAG, "is a Fragment and extending an Easy Fragment");
            Resumer resumer = (Resumer) classInstance;
            resumer.runWhenResumed(new Task() {
                @Override
                public void run(boolean fragmentDestroyed) {
                    try {
                        if (!fragmentDestroyed) {
                            Log.d(TAG, "Fragment not destroyed and resume; Thus running method logic");
                            joinPoint.proceed();
                        } else {
                            Log.d(TAG, "Fragment destroyed");
                        }
                    } catch (Throwable throwable) {
                        //TODO: Should we do something here?
                    }
                }
            });
        } else {
            Log.e(TAG, "Class should extend an Easy Fragment");
            joinPoint.proceed();
        }
    }
}



