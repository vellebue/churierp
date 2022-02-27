package org.bastanchu.churierp.churierpweb.util;

public class Pair<F,S> {

    private F firtst;
    private S second;

    public Pair(F first, S second) {
        this.firtst = first;
        this.second = second;
    }

    public F getFirtst() {
        return firtst;
    }

    public S getSecond() {
        return second;
    }
}
