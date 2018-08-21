package com.aosamesan;

import java.util.function.Function;

public class InvokeCounter<T, InvokerType extends Function<Object[], T>> {
    private InvokerType invoker;
    private int count;

    private InvokeCounter() {
        count = 0;
    }

    private void setInvoker(InvokerType invoker) {
        this.invoker = invoker;
    }

    public T invoke(Object... params) {
        count++;
        return invoker.apply(params);
    }

    public int getCount() {
        return count;
    }

    public void resetCount() {
        count = 0;
    }

    public static <R>InvokeCounter<R, Function<Object[], R>> createInvokeCounter(InvokerParameter<InvokeCounter<R, Function<Object[], R>>, Object[], R> parameter) {
        InvokeCounter<R, Function<Object[], R>> invokeCounter = new InvokeCounter<>();
        invokeCounter.setInvoker((params) -> parameter.invoke(params, invokeCounter));
        return invokeCounter;
    }
}
