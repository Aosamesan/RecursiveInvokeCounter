package com.aosamesan;

@FunctionalInterface
public interface InvokerParameter<Invoker extends InvokeCounter, T, R> {
    R invoke(T params, Invoker invoker);
}
