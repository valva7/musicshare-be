package org.musicshare.domain.music.model;

public class PositiveIntegerCounter {

    private int count;

    public PositiveIntegerCounter() {
        this.count = 0;
    }

    public PositiveIntegerCounter(int count) {
        this.count = count;
    }

    public int increase() {
        this.count ++;
        return this.count;
    }

    public int decrease() {
        if(count <= 0) {
            return 0;
        }
        return this.count --;
    }

    public int getCount() {
        return count;
    }
}
