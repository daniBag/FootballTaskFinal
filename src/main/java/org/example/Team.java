package org.example;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Team implements Comparable{
    private final int ID;
    private final String NAME;
    private final List<Player> PLAYERS;

    public Team(int id, String name){
        this.ID = id;
        this.NAME = name;
        this.PLAYERS = new ArrayList<>();
        this.generatePlayers();
    }

    private void generatePlayers() {
        Random random = new Random();
        IntStream.range(0, Constants.PLAYERS_AMOUNT).
                forEach(integer-> this.PLAYERS.
                        add(new Player(((this.ID * Constants.PLAYERS_AMOUNT) + integer),
                                        Constants.FIRST_NAMES.get(random.nextInt(0, Constants.FIRST_NAMES.size())),
                                        Constants.LAST_NAMES.get(random.nextInt(0, Constants.LAST_NAMES.size())))));
    }

    public boolean equals(Team o) {
        return this.ID == o.ID;
    }
    public int hashCode() {
        return Objects.hash(ID);
    }
    @Override
    public String toString() {
        return "Name: " + this.NAME + " ID: " + this.ID;
    }
    public String getNAME() {
        return this.NAME;
    }
    @Override
    public int compareTo(Object o) {
        int result = 0;
        if (!(o == null || this.getClass() != o.getClass())) {
            Team other = (Team) o;
            result = other.NAME.compareTo(this.NAME);
        }
        return result;
    }
    public List<Player> getPlayerScoredNGoals(int n, List<Match> matchesPlayed) {
        return this.PLAYERS.stream().
                filter(player -> player.countPlayerGoals(matchesPlayed) >= n).
                toList();
    }
    public Map<Integer, Integer> getTopScorers(int n, List<Match> matchesPlayed) {
        Map<Integer, Integer> playersByGoals = new HashMap<>();
        this.PLAYERS.forEach(player -> playersByGoals.put(player.getID(), player.countPlayerGoals(matchesPlayed)));
        return playersByGoals.
                entrySet().
                stream().
                sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())).
                limit(n)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (oldValue, newValue) -> oldValue, LinkedHashMap::new));
    }
    public int getPoints(List<Match> matchesPlayed){
        AtomicInteger points = new AtomicInteger(0);
        matchesPlayed.forEach(match -> points.set(points.get() + match.getTeamPointsFromMatch(this)));
        return points.get();
    }
    public int getGoalsDiff(List<Match> matchesPlayed){
        AtomicInteger points = new AtomicInteger(0);
        matchesPlayed.forEach(match -> points.set(points.get() + match.getTeamGoalsDiff(this)));
        return points.get();
    }
    public String leagueTablePrint() {
        return "\t\t" + this.NAME + "      \t";
    }
    public Player getPlayerById(int playerId) {
        return this.PLAYERS.get(playerId);
    }
    public boolean isPlayerInTeam(Player player) {
        return this.PLAYERS.stream().
                anyMatch(player1 -> player1.equals(player));
    }
    public int getID() {
        return this.ID;
    }
}
