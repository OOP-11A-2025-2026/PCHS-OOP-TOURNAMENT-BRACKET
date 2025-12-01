package tournament.backend;

import java.util.*;

public class Tournament {
    private final List<String> participants;
    private BracketNode root;
    private final List<Match> allMatches = new ArrayList<>();
    private int matchCounter = 1;

    public Tournament(List<String> participants) {
        if (participants.size() < 2) {
            throw new IllegalArgumentException("At least 2 participants required");
        }
        this.participants = new ArrayList<>(participants);
    }

    public void buildBracket() {
        List<BracketNode> leaves = createFirstRoundLeaves(participants);
        root = buildNextRounds(leaves, 2);
    }

    private List<BracketNode> createFirstRoundLeaves(List<String> participants) {
        List<String> shuffled = new ArrayList<>(participants);
        Collections.shuffle(shuffled);
        List<BracketNode> leaves = new ArrayList<>();

        for (int i = 0; i < shuffled.size(); i += 2) {
            Match match = new Match(matchCounter++, 1);
            if (i + 1 < shuffled.size()) {
                match.setTeams(shuffled.get(i), shuffled.get(i + 1));
            } else {
                match.setTeams(shuffled.get(i), null);
            }
            BracketNode node = new BracketNode(match);
            leaves.add(node);
            allMatches.add(match);
        }
        return leaves;
    }

    private BracketNode buildNextRounds(List<BracketNode> nodes, int round) {
        if (nodes.size() == 1) return nodes.get(0); // root reached

        List<BracketNode> nextRound = new ArrayList<>();

        for (int i = 0; i < nodes.size(); i += 2) {
            Match match = new Match(matchCounter++, round);
            BracketNode parent = new BracketNode(match);
            parent.setLeft(nodes.get(i));

            if (i + 1 < nodes.size()) {
                parent.setRight(nodes.get(i + 1));
            }
            nextRound.add(parent);
            allMatches.add(match);
        }

        return buildNextRounds(nextRound, round + 1);
    }

    public void playTournament(ResultProvider inputProvider) {
        resolveMatch(root, inputProvider);
    }

    private void resolveMatch(BracketNode node, ResultProvider inputProvider) {
        if (node == null) return;

        resolveMatch(node.getLeft(), inputProvider);
        resolveMatch(node.getRight(), inputProvider);

        Match match = node.getMatch();
        if (match.isCompleted()) return;

        String t1 = match.getTeam1();
        String t2 = match.getTeam2();

        if (t1 == null && node.getLeft() != null) t1 = node.getLeft().getMatch().getWinner();
        if (t2 == null && node.getRight() != null) t2 = node.getRight().getMatch().getWinner();

        match.setTeams(t1, t2);

        if (!match.isBye() && inputProvider != null) {
            String winner = inputProvider.getWinner(t1, t2);
            match.setWinner(winner);
        }
    }

    public String getWinner() {
        return root.getMatch().getWinner();
    }

    public List<Match> getAllMatches() {
        return allMatches;
    }

    public interface ResultProvider {
        String getWinner(String team1, String team2);
    }
}
