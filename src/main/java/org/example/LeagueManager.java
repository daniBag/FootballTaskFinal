package org.example;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import static org.example.UserActions.*;

public class LeagueManager {
    private Map<Integer, List<Match>> leagueRounds;
    private final Map<Integer, Team> TEAMS;
    private Integer currentLeagueRound;
    public LeagueManager(){
        this.currentLeagueRound = 0;
        this.TEAMS = new HashMap<>();
        this.leagueRounds = new HashMap<>();
        readTeams();
        leagueScheduleGenerate();
        printLeagueTable();
    }
    public void leagueSimulator(){
        IntStream.range(0, Constants.ROUNDS_AMOUNT).forEach(round->{
            this.roundPlay(round);
            this.currentLeagueRound++;
            this.printLeagueTable();
            this.userActions(round);
        });
    }
    private void userActions(int round) {
        Scanner scanner = new Scanner(System.in);
        System.out.println(Constants.USER_ACTIONS);
        String input = scanner.nextLine().toUpperCase();
        UserActions choice;
        try{
            choice = Enum.valueOf(UserActions.class, input);
        }catch (IllegalArgumentException e){
            System.out.println("not a valid action!");
            choice = RETRY;
        }
        if (MT.equals(choice)) {
            System.out.println("Enter team ID: (0-9)");
            input = scanner.nextLine();
            try {
                int teamId = Integer.parseInt(input);
                if (teamId >= 0 && teamId < Constants.TEAMS_AMOUNT) {
                    System.out.println(this.findMatchesByTeam(teamId));
                } else {
                    System.out.println("INVALID TEAM ID! back to main menu");
                }
            } catch (IllegalArgumentException e) {
                System.out.println("INVALID TEAM ID! back to main menu");
            }
        } else if (TT.equals(choice)) {
            System.out.println("Enter desired number of top scoring teams: (1-10)");
            input = scanner.nextLine();
            try {
                int teamsNumber = Integer.parseInt(input);
                if (teamsNumber < Constants.TEAMS_AMOUNT && teamsNumber > 0) {
                    System.out.println(this.findTopScoringTeams(teamsNumber));
                } else {
                    System.out.println("INVALID TEAMS AMOUNT! back to main menu");
                }
            } catch (IllegalArgumentException e) {
                System.out.println("INVALID TEAMS AMOUNT! back to main menu");
            }
        } else if (PG.equals(choice)) {
            System.out.println("Enter desired minimum goals amount: ");
            input = scanner.nextLine();
            try {
                int goalsAmount = Integer.parseInt(input);
                if (goalsAmount > 0) {
                    System.out.println(this.findPlayersWithAtLeastNGoals(goalsAmount));
                } else {
                    System.out.println("INVALID GOALS AMOUNT! back to main menu");
                }
            } catch (IllegalArgumentException e) {
                System.out.println("INVALID GOALS AMOUNT! back to main menu");
            }
        } else if (TP.equals(choice)) {
            System.out.println("Enter desired league position: ");
            input = scanner.nextLine();
            try {
                int leaguePos = Integer.parseInt(input);
                if (leaguePos > 0 && leaguePos <= Constants.TEAMS_AMOUNT) {
                    System.out.println(this.getTeamByPosition(leaguePos));
                } else {
                    System.out.println("INVALID LEAGUE POSITION! back to main menu");
                }
            } catch (IllegalArgumentException e) {
                System.out.println("INVALID LEAGUE POSITION! back to main menu");
            }
        } else if (TS.equals(choice)) {
            System.out.println("Enter desired number of top scorers: ");
            input = scanner.nextLine();
            try {
                int playersAmount = Integer.parseInt(input);
                if (playersAmount > 0) {
                    System.out.println(this.getTopScorers(playersAmount));
                } else {
                    System.out.println("INVALID PLAYERS AMOUNT! back to main menu");
                }
            } catch (IllegalArgumentException e) {
                System.out.println("INVALID PLAYERS AMOUNT! back to main menu");
            }
        } else if (C.equals(choice)) {
            if(round < Constants.ROUNDS_AMOUNT - 1){
                System.out.println("Moving on to next round!");
            }else {
                System.out.println("League finished. see you next season!");
            }
        }
        if (!C.equals(choice)){
            choice = RETRY;
        }
        if (RETRY.equals(choice)){
            userActions(round);
        }
    }
    private void roundPlay(int round) {
        this.leagueRounds.get(round).forEach(match -> {
            System.out.println(match.teamNames());
            IntStream.range(0, Constants.COUNTDOWN).forEach(number->{
                try {
                    Thread.sleep(Constants.SECOND);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println(Constants.COUNTDOWN - number);
            });
            match.result().forEach(System.out::println);
        });
    }
    private void printLeagueTable() {
        System.out.println(Constants.SEPARATOR);
        System.out.println("Position:\t Team Name:\t\t Points:\t Goal Difference:");
        AtomicInteger pos = new AtomicInteger(1);
        this.getTableSort().forEach(team -> {
            System.out.println("\t" + pos.get() + team.leagueTablePrint() + team.getPoints(this.findMatchesByTeam(team.getID())) + "\t\t\t\t" + team.getGoalsDiff(this.findMatchesByTeam(team.getID())));
            pos.getAndIncrement();
        });
        System.out.println(Constants.SEPARATOR);
    }
    private void leagueScheduleGenerate() {
        this.leagueRounds = new HashMap<>();
        List<Pair> teamsInRound = new ArrayList<>();
        AtomicReference<Integer> matchIdMod = new AtomicReference<>(1);
        AtomicReference<Boolean> regenerate = new AtomicReference<>(false);
        IntStream.range(0, Constants.ROUNDS_AMOUNT).forEach(number->{
            List<Match> round = new ArrayList<>();
            this.leagueRounds.put(number, round);
            List<Pair> teams = teamIdsShuffle(teamsInRound);
            if (teams.isEmpty()){
                regenerate.set(true);
            }
            if (!regenerate.get()){
                teams.forEach(pair -> {
                    round.add(new Match(matchIdMod.get(), this.TEAMS.get(pair.getFIRST()), this.TEAMS.get(pair.getSECOND())));
                    matchIdMod.getAndSet(matchIdMod.get() + 1);
                });
            }
        });
        if (regenerate.get()){
            leagueScheduleGenerate();
        }
    }
    private List<Pair> teamIdsShuffle(List<Pair> teamsInRound){
        List<Integer> shuffled = new ArrayList<>(IntStream.range(0, this.TEAMS.size()).boxed().toList());
        List<Pair> result = new ArrayList<>();
        Collections.shuffle(shuffled);
        List<Pair> finalResult = new ArrayList<>();
        IntStream.range(1, (this.TEAMS.size() / 2) + 1).forEach(number-> {
            int pairElement = (number * 2) - 1;
            finalResult.add(new Pair(shuffled.get(pairElement), shuffled.get(pairElement - 1)));
        });
        if (teamsInRound.isEmpty()){
            teamsInRound.addAll(finalResult);
            result = finalResult;
        }else {
           if (finalResult.stream().anyMatch(pair-> teamsInRound.stream().anyMatch(pair1 -> pair1.equals(pair)))){
               try{
                   result = teamIdsShuffle(teamsInRound);
               }catch (StackOverflowError e){
                   e.getCause();
               }
           }else {
               result = finalResult;
               teamsInRound.addAll(result);
           }
        }
        return result;
    }
    private void readTeams(){
        File file = new File(Constants.PATH_TO_FILE);
        if (file.exists()) {
            try {
                BufferedReader br = new BufferedReader(new FileReader(file));
                AtomicReference<String> line = new AtomicReference<>();
                IntStream.range(0, Constants.TEAMS_AMOUNT).forEach(integer -> {
                    try {
                        line.set(br.readLine());
                        if (line.get() != null) {
                            this.TEAMS.put(integer, new Team(integer, line.get()));
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    private List<Match> findMatchesByTeam(int teamId){
        List<Match> result = new ArrayList<>();
        if (this.currentLeagueRound > 0){
            IntStream.range(0, this.currentLeagueRound).forEach(round-> this.leagueRounds.get(round).stream().filter(match ->
                    match.isTeamInMatch(this.TEAMS.get(teamId))).findFirst().ifPresent(result::add));
        }
        return result;
    }
    private List<Team> findTopScoringTeams(int n){
        List<Team> result = this.TEAMS.keySet().stream().sorted(Comparator.comparingInt(this::countGoalsForTeam)).map(this.TEAMS::get).toList();
        result = result.subList(0, n);
        return result;
    }
    private int countGoalsForTeam(int teamId){
        AtomicInteger goals = new AtomicInteger();
        this.findMatchesByTeam(teamId).forEach(match -> goals.set(goals.get() + match.countGoalsOfTeam(this.TEAMS.get(teamId))));
        return goals.get();
    }
    private List<Player> findPlayersWithAtLeastNGoals(int n){
        List<Player> result = new ArrayList<>();
        this.TEAMS.values().forEach(team -> {
            List<Player> players = team.getPlayerScoredNGoals(n, this.findMatchesByTeam(team.getID()));
            if (!players.isEmpty()){
                result.addAll(players);
            }
        });
        return result;
    }
    private Team getTeamByPosition(int position){
        return this.getTableSort().get(position - 1);
    }
    private Map<Integer, Integer> getTopScorers(int n){
        Map<Integer, Integer> teamsTopScorers = new LinkedHashMap<>();
        this.TEAMS.values().forEach(team -> teamsTopScorers.putAll(team.getTopScorers(n, this.findMatchesByTeam(team.getID()))));
        return teamsTopScorers.entrySet().
                stream().
                sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())).
                limit(n).
                collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (oldValue, newValue) -> oldValue, LinkedHashMap::new));
    }
    private List<Team> getTableSort() {
        return new ArrayList<>(this.TEAMS.values().stream().sorted(Comparator.comparing((Team team) ->
                team.getPoints(this.findMatchesByTeam(team.getID()))).thenComparing(team -> team.getGoalsDiff(this.findMatchesByTeam(team.getID()))).reversed().thenComparing(Team::getNAME)).toList());
    }
}
