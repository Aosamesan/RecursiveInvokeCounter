package com.aosamesan;

import java.awt.*;
import java.lang.reflect.TypeVariable;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {

    public static void main(String[] args) {
        System.out.println("==== Merge Sort");
        int size = 16;
        InvokeCounter<Void, Function<Object[], Void>> mergeSortCounter = InvokeCounter.createInvokeCounter(Main::mergeSort);
        List<Integer> list = Stream.generate(new RandomIntegerSupplier()).limit(size).collect(Collectors.toList());
        mergeSortCounter.invoke(list, 0, list.size());
        System.out.println("IsSorted : " + isSorted(list) + " , Invoke Count : " + mergeSortCounter.getCount());

        System.out.println("==== Fibonacci");
        InvokeCounter<Long, Function<Object[], Long>> fibonacciCounter = InvokeCounter.createInvokeCounter(Main::fibonacci);
        Long result = fibonacciCounter.invoke(5L);
        System.out.println("Result : " + result + " , Count : " + fibonacciCounter.getCount());
    }

    private static <T extends Comparable<T>> boolean isSorted(List<T> list) {
        if (list.size() < 2) {
            return true;
        }
        T previous = list.get(0);
        for (T item : list) {
            if (previous.compareTo(item) > 0) {
                return false;
            }
            previous = item;
        }
        return true;
    }

    private static Long fibonacci(Object[] params, InvokeCounter<Long, Function<Object[], Long>> invokeCounter) {
        if (params.length != 1) {
            throw new IllegalArgumentException();
        }

        long n = (long)params[0];
        if (n < 2) {
            return 1L;
        }
        return invokeCounter.invoke(n - 1) + invokeCounter.invoke(n - 2);
    }

    private static class RandomIntegerSupplier implements Supplier<Integer>{
        private Random r = new Random();
        @Override
        public Integer get() {
            return r.nextInt(100);
        }
    }

    @SuppressWarnings("unchecked")
    private static Void mergeSort(Object[] params, InvokeCounter<Void, Function<Object[], Void>> invokeCounter) {
        if (params.length != 3) {
            throw new IllegalArgumentException();
        }
        if (!(params[0] instanceof List)) {
            throw new IllegalArgumentException();
        }


        List<Integer> list = (List<Integer>)params[0];
        int start = (int)params[1];
        int end = (int)params[2];
        int length = end - start; // exclude end

        if (length < 2) {
            return null;
        }

        int mid = (start + end) / 2;

        // divide
        invokeCounter.invoke(list, start, mid);
        invokeCounter.invoke(list, mid, end);

        int left = start;
        int right = mid;
        Queue<Integer> queue = new LinkedList<>();

        while (left < mid && right < end) {
            queue.add(list.get(left) < list.get(right) ? list.get(left++) : list.get(right++));
        }

        while (left < mid) {
            queue.add(list.get(left++));
        }

        while (right < end) {
            queue.add(list.get(right++));
        }

        int index = start;
        while (!queue.isEmpty()) {
            list.set(index++, queue.poll());
        }
        return null;
    }
}
