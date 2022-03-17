package com.temmy.json_items_test_1.util;

import java.util.NavigableMap;
import java.util.Random;
import java.util.TreeMap;

public class WeightedRandomness<n> {
    private final NavigableMap<Double, n> map = new TreeMap<Double, n>();
    private final Random random;
    private double total = 0d;

    public WeightedRandomness(){
        this(new Random());
    }

    public WeightedRandomness(Random random) {
        this.random = random;
    }

    public void add (double weight, n result){
        if (weight <= 0) return;
        total += weight;
        map.put(total, result);
    }

    public n next(){
        double value = random.nextDouble()*total;
        return map.higherEntry(value).getValue();
    }
}
