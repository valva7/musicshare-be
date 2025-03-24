package org.musicshare.domain.music.model;

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
        double before = this.count * averageCount - 1;
        this.count = (before + addCount) / averageCount;

        // 소수 둘째 자리부터 버리기
        this.count = Math.floor(this.count * 100) / 100.0;

        return this.count;
    }

    public double getCount() {
        return count;
    }

}
