package org.example;

import java.util.List;

public class Constants {
    public static final List<String> FIRST_NAMES = List.of("Tom", "John", "George", "Sam", "James", "Pedro", "Denis", "Luis", "Diego", "Jesus",
            "Frederic", "David", "Jose", "Oskar", "Dor", "Oliver", "Harry", "Paul", "Steve", "Andy", "Mark", "Chris", "Kevin", "Michael", "Lee", "Alan", "Daniel",
            "Frank", "Gary", "Jack", "Joe", "Stephen");
    public static final List<String> LAST_NAMES = List.of("Doe", "Smith", "White", "Teves", "Rodriguez", "Lopez", "Mora", "Suarez", "Costa", "Rueda",
            "Maradona", "Luis", "Kyle", "Pont", "Cane", "Shell", "Kane", "Traore", "Diarra", "Gabriel", "Gaddis", "Gado", "Del Piero", "Shevchenko", "Schweinsteiger",
            "Bergkamp", "Beckham", "Drogba", "Torres", "Silva", "Anelka", "Gomes");
    public static final String PATH_TO_FILE = "src/main/resources/teams.csv";
    public static final int ROUNDS_AMOUNT = 9;
    public static final int TEAMS_AMOUNT = 10;
    public static final int PLAYERS_AMOUNT = 15;
    public static final int WIN = 3;
    public static final int DRAW = 1;
    public static final int COUNTDOWN = 10;
    public static final long SECOND = 1000;
    public static final int HOME_GOAL_TEXT_LENGTH = 35;
    public static final String WHITE_SPACE = " ";
    public static final String SEPARATOR = "*************************************************************\n";
    public static final int MAX_GOALS_PER_MATCH = 12;
    public static final int MINUTE_MAX = 91;
    public static final int MINUTE_MIN = 1;
    public static final int INVALID = -99;
    public static final int LOSS = 0;
    public static final String USER_ACTIONS = """
            User actions!
            please enter your desired action by the letter in () next to it.
            Find matches by team (MT)
            Find top scoring n teams (TT)
            Find players that scored at least n goals (PG)
            Get team by position in league table (TP)
            Get the n top scorers (TS)
            Continue to next round(C)""";
}
