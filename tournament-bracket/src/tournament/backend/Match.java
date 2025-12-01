package tournament.backend;

public class Match {
    private final int matchId;
    private final int roundNumber;
    private String team1;
    private String team2;
    private String winner;
    private boolean isBye;
    private boolean isCompleted;

    public Match(int matchId, int roundNumber) {
        this.matchId = matchId;
        this.roundNumber = roundNumber;
        this.isCompleted = false;
        this.isBye = false;
    }

    public void setTeams(String t1, String t2) {
        this.team1 = t1;
        this.team2 = t2;

        if (t2 == null || t1 == null) {
            this.isBye = true;
            this.winner = t1;
            this.isCompleted = true;
        }
    }

    public void setWinner(String winner) {
        if (winner == null || winner.trim().isEmpty()) {
            throw new IllegalArgumentException("Winner cannot be null or empty");
        }
        this.winner = winner;
        this.isCompleted = true;
    }

    public int getMatchId() {
        return matchId;
    }

    public int getRoundNumber() {
        return roundNumber;
    }

    public String getTeam1() {
        return team1;
    }

    public String getTeam2() {
        return team2;
    }

    public String getWinner() {
        return winner;
    }

    public boolean isBye() {
        return isBye;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    @Override
    public String toString() {
        if (isBye) {
            return String.format("Round %d, Match %d: %s (BYE)",
                    roundNumber, matchId, team1);
        }
        if (!isCompleted) {
            return String.format("Round %d, Match %d: %s vs %s (Pending)",
                    roundNumber, matchId, team1, team2);
        }
        return String.format("Round %d, Match %d: %s vs %s → Winner: %s",
                roundNumber, matchId, team1, team2, winner);
    }
}