package com.test.mark1.elements;

import org.testng.IAnnotationTransformer;
import org.testng.IRetryAnalyzer;
import org.testng.annotations.ITestAnnotation;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

@SuppressWarnings({"rawtypes", "unchecked"})
public class RetryTransformer implements IAnnotationTransformer {

    @Override
    public void transform(ITestAnnotation annotation, Class testClass,
                          Constructor testConstructor, Method testMethod) {
        annotation.setRetryAnalyzer((Class<? extends IRetryAnalyzer>) (Class<?>) RetryAnalyzer.class);
    }
}
