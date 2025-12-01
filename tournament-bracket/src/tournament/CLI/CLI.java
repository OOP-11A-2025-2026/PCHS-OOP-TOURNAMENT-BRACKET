package tournament.CLI;

import tournament.backend.Match;
import tournament.backend.Tournament;

import java.util.*;

public class CLI {
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("Single Elimination Tournament");

        int n = readPlayerCount();
        List<String> players = readPlayers(n);

        // Initialize the tournament backend
        Tournament tournament = new Tournament(players);
        tournament.buildBracket();

        // Play the tournament with user input
        tournament.playTournament(new Tournament.ResultProvider() {
            @Override
            public String getWinner(String team1, String team2) {
                return promptWinner(team1, team2);
            }
        });

        System.out.println("\nTournament Complete!");
        System.out.println("Winner: " + tournament.getWinner());

        // Optional: print all match results
        System.out.println("\nMatch History:");
        for (Match match : tournament.getAllMatches()) {
            System.out.println(match);
        }
    }

    private static int readPlayerCount() {
        while (true) {
            System.out.print("Enter number of participants (min 2): ");
            try {
                int n = Integer.parseInt(scanner.nextLine());
                if (n >= 2) return n;
                System.out.println("\nError: Number must be at least 2");
            } catch (NumberFormatException e) {
                System.out.println("\nError: Invalid number");
            }
        }
    }

    private static List<String> readPlayers(int n) {
        List<String> players = new ArrayList<>();
        Set<String> uniqueCheck = new HashSet<>();
        for (int i = 0; i < n; i++) {
            while (true) {
                System.out.print("Name of participant N:" + (i + 1) + ": ");
                String name = scanner.nextLine().trim();
                if (name.isEmpty()) {
                    System.out.println("Name can't be empty");
                    continue;
                }
                if (uniqueCheck.contains(name.toLowerCase())) {
                    System.out.println("This name already exists");
                    continue;
                }
                uniqueCheck.add(name.toLowerCase());
                players.add(name);
                break;
            }
        }
        return players;
    }

    private static String promptWinner(String p1, String p2) {
        while (true) {
            System.out.print("\nWho is the winner? (" + p1 + "/" + p2 + "): ");
            String input = scanner.nextLine().trim();
            if (input.equalsIgnoreCase(p1)) return p1;
            if (input.equalsIgnoreCase(p2)) return p2;
            System.out.println("Invalid name, please enter a valid winner.");
        }
    }
}
