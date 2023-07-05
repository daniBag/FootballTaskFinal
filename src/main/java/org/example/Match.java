package org.example;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
public class Match {
    private final int ID;
    private final Team HOME_TEAM;
    private final Team AWAY_TEAM;
    private final List<Goal> GOALS;
    public Match(int id, Team homeTeam, Team awayTeam){
        this.ID = id;
        this.HOME_TEAM = homeTeam;
        this.AWAY_TEAM = awayTeam;
        this.GOALS = new ArrayList<>();
    }
    public boolean isTeamInMatch(Team team){
        return this.AWAY_TEAM.equals(team) || this.HOME_TEAM.equals(team);
    }
    public String toString() {
        return "Match ID:" + this.ID +
                " Home: " + HOME_TEAM.getNAME() +
                " Away: " + AWAY_TEAM.getNAME() +
                "\n";
    }
    public String teamNames() {
        return "\nMATCH DAY: " + this.ID + " \n" +
                "Home: " + this.HOME_TEAM.getNAME() + " :VS: Away: " + this.AWAY_TEAM.getNAME() + "\n";
    }

    public List<String> result() {
        generateResult();
        return this.getMatchResultText();
    }

    private List<String> getMatchResultText() {
        List<String> matchData = new ArrayList<>();
        matchData.add(Constants.SEPARATOR + fixHomeGoalText(this.HOME_TEAM.getNAME() + ": " + countGoalsOfTeam(this.HOME_TEAM) + ": ") + " : " + this.AWAY_TEAM.getNAME() + ": " + countGoalsOfTeam(this.AWAY_TEAM) + "\n" + Constants.SEPARATOR);
        getGoalsData(matchData);
        return matchData;
    }
    public int getTeamPointsFromMatch(Team team){
        int points = Constants.INVALID;
        if (this.HOME_TEAM.equals(team) || this.AWAY_TEAM.equals(team)){
            int teamGoals = countGoalsOfTeam(team);
            int goalDiff = teamGoals - (this.GOALS.size() - teamGoals);
            if (goalDiff > 0){
                points = Constants.WIN;
            } else if (goalDiff == 0) {
                points = Constants.DRAW;
            }else {
                points = Constants.LOSS;
            }
        }
        return points;
    }
    public int getTeamGoalsDiff(Team team){
        int goals = this.countGoalsOfTeam(team);
        return goals - (this.GOALS.size() - goals);
    }

    public int countGoalsOfTeam(Team team) {
        return Math.toIntExact(this.GOALS.stream().filter(goal -> team.isPlayerInTeam(goal.getSCORER())).count());
    }
    public int countGoalForPlayer(Player player){
        return Math.toIntExact(this.GOALS.stream().filter(goal -> goal.isScorer(player)).count());
    }
    private void getGoalsData(List<String> matchData) {
        List<Goal> homeGoals = this.GOALS.stream().filter(Goal::getHOME).toList();
        List<Goal> awayGoals = this.GOALS.stream().filter(goal -> !goal.getHOME()).toList();
        IntStream.range(0, Math.max(homeGoals.size(), awayGoals.size())).forEach(number-> matchData.add(getGoalText(homeGoals, number, true) + getGoalText(awayGoals, number, false)));
    }

    private String getGoalText(List<Goal> goals, int number, boolean home) {
        String output;
        Goal goal = (goals.size() > number? goals.get(number) : null);
        String empty = "";
        output = (goal == null? empty : goal.getMatchText());
        if (home){
            output = fixHomeGoalText(output);
        }
        return output;
    }

    private String fixHomeGoalText(String homeText) {
        AtomicReference<String> output = new AtomicReference<>(homeText);
        int whiteSpace = Constants.HOME_GOAL_TEXT_LENGTH - output.get().length();
        IntStream.range(0, whiteSpace).forEach(integer-> output.set(output.get() + Constants.WHITE_SPACE));
        return output.get();
    }

    private void generateResult(){
        Random random = new Random();
        List<Integer> minutesRandomized = new ArrayList<>();
        int goalsAmount = random.nextInt(Constants.MAX_GOALS_PER_MATCH);
        int homeGoalsAmount = (goalsAmount > 0? random.nextInt(goalsAmount) : 0);
        int awayGoalsAmount = goalsAmount - homeGoalsAmount;
        AtomicInteger homeCounter = new AtomicInteger();
        AtomicInteger awayCounter = new AtomicInteger();
        IntStream.range(0, goalsAmount).
                forEach(number-> minutesRandomized.add(random.nextInt(Constants.MINUTE_MIN, Constants.MINUTE_MAX)));
        Collections.sort(minutesRandomized);
        Map<Integer, Integer> minutesMapped = IntStream.range(0, minutesRandomized.size()).boxed().collect(Collectors.toMap(index-> index, minutesRandomized::get));
        List<Integer> toShuffle = new ArrayList<>(minutesMapped.keySet().stream().toList());
        Collections.shuffle(toShuffle);
        toShuffle.forEach(shuffled->{
            boolean home = false;
            if (homeCounter.get() < homeGoalsAmount){
                if (awayCounter.get() < awayGoalsAmount){
                    if(random.nextInt(0, 2) % 2 == 0){
                        home = true;
                        homeCounter.getAndIncrement();
                    }else {
                        awayCounter.getAndIncrement();
                    }
                }else {
                    home = true;
                    homeCounter.getAndIncrement();
                }
            }else {
                awayCounter.getAndIncrement();
            }
            Goal goal = new Goal(shuffled,
                    minutesMapped.get(shuffled),
                    (home? (this.HOME_TEAM.getPlayerById(random.nextInt(0, Constants.PLAYERS_AMOUNT))) : (this.AWAY_TEAM.getPlayerById(random.nextInt(0, Constants.PLAYERS_AMOUNT)))),
                    home);
            this.GOALS.add(goal);
        });
        this.GOALS.sort(Goal::compareTo);
    }
}
