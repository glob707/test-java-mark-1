package com.test.mark1.elements;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

import java.util.concurrent.atomic.AtomicInteger;

public class RetryAnalyzer implements IRetryAnalyzer {
    private final AtomicInteger retryCount = new AtomicInteger(0);
    private static final int MAX_RETRY = 2;

    @Override
    public boolean retry(ITestResult result) {
        if (!result.isSuccess() && retryCount.get() < MAX_RETRY) {
            retryCount.incrementAndGet();
            return true;
        }
        return false;
    }
}
