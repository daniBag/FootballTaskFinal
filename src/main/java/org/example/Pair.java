package org.example;

import java.util.Objects;

public class Pair implements Comparable {
    private final Integer FIRST;
    private final Integer SECOND;

    public Pair(int first, int second){
        this.FIRST = first;
        this.SECOND = second;
    }

    @Override
    public boolean equals(Object o) {
        boolean equal = false;
        if (!(o == null || getClass() != o.getClass())) {
            Pair pair = (Pair) o;
            equal =  (this.FIRST.equals(pair.FIRST) && this.SECOND.equals(pair.SECOND)) || (this.FIRST.equals(pair.SECOND) && this.SECOND.equals(pair.FIRST));
        }
        return  equal;
    }

    @Override
    public int hashCode() {
        return Objects.hash(FIRST, SECOND);
    }

    public Integer getFIRST() {
        return this.FIRST;
    }
    public Integer getSECOND() {
        return this.SECOND;
    }
    public String toString(){
        return this.FIRST + "-" + this.SECOND + "\n";
    }

    @Override
    public int compareTo(Object o) {
        int result = 0;
        if (!(o == null || getClass() != o.getClass())) {
            Pair pair = (Pair) o;
            result = this.FIRST.compareTo(pair.FIRST);
            if (result == 0){
                result = this.SECOND.compareTo(pair.SECOND);
            }
        }
        return result;
    }

}
