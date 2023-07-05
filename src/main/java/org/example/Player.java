package org.example;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

public class Player {
    private final int ID;
    private final String FIRST_NAME;
    private final String LAST_NAME;

    public Player(int id, String firstName, String lastName){
        this.ID = id;
        this.FIRST_NAME = firstName;
        this.LAST_NAME = lastName;
    }
    @Override
    public boolean equals(Object o) {
        boolean equal = false;
        if (!(o == null || getClass() != o.getClass())) {
            Player player = (Player) o;
            equal =  this.ID == player.ID;
        }
        return equal;
    }
    @Override
    public int hashCode() {
        return Objects.hash(ID);
    }
    public int countPlayerGoals(List<Match> matchesPlayed){
        AtomicReference<Integer> goals = new AtomicReference<>(0);
        matchesPlayed.forEach(match -> goals.updateAndGet(v -> v + match.countGoalForPlayer(this)));
        return goals.get();
    }
    @Override
    public String toString() {
        return "Player " +
                "id#" + ID +
                ": Name=" + this.getName() + '\'';
    }
    public int getID() {
        return this.ID;
    }
    public String getName() {
        return this.FIRST_NAME + " " + this.LAST_NAME;
    }
}
