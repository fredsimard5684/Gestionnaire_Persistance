package com.uqtr.ca.logging;

import com.google.inject.AbstractModule;
import com.google.inject.matcher.Matchers;

public class InterceptorModule extends AbstractModule {
    @Override
    protected void configure() {
        bindInterceptor(Matchers.any(),
                Matchers.annotatedWith(RetrieveLog.class),
                new LogRetrieve());
    }
}
