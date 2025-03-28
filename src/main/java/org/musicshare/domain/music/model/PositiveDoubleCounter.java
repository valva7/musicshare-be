package org.musicshare.domain.music.model;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class PositiveDoubleCounter {

    private double count;

    public PositiveDoubleCounter() {
        this.count = 0;
    }

    public PositiveDoubleCounter(double rating) {
        this.count = rating;
    }

    public double increase(int count) {
        this.count += count;
        return this.count;
    }

    public double decrease() {
        this.count -= count;
        return this.count;
    }

    public double average(double addCount, int averageCount) {
        BigDecimal before = BigDecimal.valueOf(this.count).add(BigDecimal.valueOf(averageCount - 1)).setScale(2, RoundingMode.DOWN);
        before = before.add(BigDecimal.valueOf(addCount)).divide(BigDecimal.valueOf(averageCount)).setScale(2, RoundingMode.DOWN);

        this.count = before.doubleValue();

        return this.count;
    }

    public double getCount() {
        return count;
    }

}
