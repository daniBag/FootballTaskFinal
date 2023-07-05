package org.example;

import java.util.Objects;

public class Goal implements Comparable{
    private final int ID;
    private final Integer MINUTE;
    private final Player SCORER;
    private final boolean HOME;
    public Goal(int id, int minute, Player scorer, boolean home){
        this.ID = id;
        this.MINUTE = minute;
        this.SCORER = scorer;
        this.HOME = home;
    }

    public Player getSCORER() {
        return this.SCORER;
    }

    public String getMatchText() {
        return this.SCORER.getName() + " (" + this.MINUTE + ") ";
    }

    public boolean getHOME(){
        return this.HOME;
    }

    @Override
    public boolean equals(Object o) {
        boolean equal = false;
        if (o == null || getClass() != o.getClass()){
            Goal goal = (Goal) o;
            equal = this.ID == goal.ID;
        }
        return equal;
    }

    @Override
    public int hashCode() {
        return Objects.hash(ID);
    }

    @Override
    public int compareTo(Object o) {
        int result = 0;
        if (!(o == null || getClass() != o.getClass())){
            Goal goal = (Goal) o;
            result = this.MINUTE.compareTo(goal.MINUTE);
        }
        return result;
    }
    public String toString(){
        return this.ID + "# Scored By: " + this.SCORER.getName() + (this.HOME ? " -Home-" : " -Away-") + " (" + this.MINUTE + ")\n";
    }

    public boolean isScorer(Player player) {
        return this.SCORER.equals(player);
    }
}
