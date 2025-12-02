package tournament.CLI;

import tournament.backend.Match;
import tournament.backend.Tournament;
import java.util.*;

public class CLI {
    private static final Scanner scanner = new Scanner(System.in);
    private static final Random random = new Random();

    // ANSI color codes for prettier output
    private static final String RESET = "\u001B[0m";
    private static final String BOLD = "\u001B[1m";
    private static final String CYAN = "\u001B[36m";
    private static final String GREEN = "\u001B[32m";
    private static final String YELLOW = "\u001B[33m";
    private static final String RED = "\u001B[31m";
    private static final String BLUE = "\u001B[34m";
    private static final String MAGENTA = "\u001B[35m";

    public static void main(String[] args) {
        printBanner();

        int n = readPlayerCount();
        List<String> players = readPlayers(n);

        // Initialize the tournament backend
        Tournament tournament = new Tournament(players);
        tournament.buildBracket();

        printSeparator();
        System.out.println(BOLD + CYAN + "🏆 TOURNAMENT BEGINS! 🏆" + RESET);
        printSeparator();

        // Play the tournament with user input
        tournament.playTournament(new Tournament.ResultProvider() {
            @Override
            public String getWinner(String team1, String team2) {
                return promptWinner(team1, team2);
            }
        });

        printTournamentComplete(tournament);
    }

    private static void printBanner() {
        System.out.println();
        System.out.println(BOLD + CYAN + "╔════════════════════════════════════════╗" + RESET);
        System.out.println(BOLD + CYAN + "║                                        ║" + RESET);

        // Centered text
        String title = "⚔️ TOURNAMENT";
        int totalWidth = 40; // width inside the borders
        int padding = (totalWidth - title.length()) / 2;
        String line = "║" + " ".repeat(padding) + YELLOW + title + CYAN + " ".repeat(totalWidth - title.length() - padding) + "║";
        System.out.println(BOLD + CYAN + line + RESET);

        System.out.println(BOLD + CYAN + "║                                        ║" + RESET);
        System.out.println(BOLD + CYAN + "╚════════════════════════════════════════╝" + RESET);
        System.out.println();
    }

    private static void printSeparator() {
        System.out.println(CYAN + "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━" + RESET);
    }

    private static void printTournamentComplete(Tournament tournament) {
        System.out.println();
        printSeparator();
        System.out.println(BOLD + GREEN + "🎉 TOURNAMENT COMPLETE! 🎉" + RESET);
        printSeparator();
        System.out.println();
        System.out.println(BOLD + YELLOW + "👑 CHAMPION: " + MAGENTA + tournament.getWinner() + " 👑" + RESET);
        System.out.println();
        printSeparator();

        // Optional: print all match results
        System.out.println(BOLD + BLUE + "\n📋 MATCH HISTORY:" + RESET);
        System.out.println();
        for (Match match : tournament.getAllMatches()) {
            System.out.println("  " + GREEN + "▸ " + RESET + match);
        }
        System.out.println();
        printSeparator();
    }

    private static int readPlayerCount() {
        while (true) {
            System.out.print(BOLD + "Enter number of participants " + CYAN + "(min 2)" + RESET + ": ");
            try {
                int n = Integer.parseInt(scanner.nextLine());
                if (n >= 2) {
                    System.out.println(GREEN + "✓ " + n + " participants registered" + RESET);
                    return n;
                }
                System.out.println(RED + "✗ Error: Number must be at least 2" + RESET);
            } catch (NumberFormatException e) {
                System.out.println(RED + "✗ Error: Invalid number" + RESET);
            }
        }
    }

    private static List<String> readPlayers(int n) {
        List<String> players = new ArrayList<>();
        Set<String> uniqueCheck = new HashSet<>();

        System.out.println();
        printSeparator();
        System.out.println(BOLD + YELLOW + "⚡ PARTICIPANT REGISTRATION" + RESET);
        printSeparator();

        for (int i = 0; i < n; i++) {
            while (true) {
                System.out.print(BOLD + "Participant #" + (i + 1) + RESET + ": ");
                String name = scanner.nextLine().trim();

                if (name.isEmpty()) {
                    System.out.println(RED + "  ✗ Name can't be empty" + RESET);
                    continue;
                }

                if (uniqueCheck.contains(name.toLowerCase())) {
                    System.out.println(RED + "  ✗ This name already exists" + RESET);
                    continue;
                }

                uniqueCheck.add(name.toLowerCase());
                players.add(name);
                System.out.println(GREEN + "  ✓ " + name + " registered successfully" + RESET);
                break;
            }
        }

        return players;
    }

    private static String promptWinner(String p1, String p2) {
        System.out.println();
        System.out.println(BOLD + YELLOW + "⚔️  MATCH TIME!" + RESET);
        System.out.println(BLUE + "  " + p1 + RESET + " " + BOLD + "vs" + RESET + " " + MAGENTA + p2 + RESET);

        while (true) {
            System.out.print(BOLD + "Enter winner " + CYAN + "(or '/' to simulate)" + RESET + ": ");
            String input = scanner.nextLine().trim();

            // Simulate match if '/' is entered
            if (input.equals("/")) {
                String winner = random.nextBoolean() ? p1 : p2;
                System.out.println(YELLOW + "🎲 Simulated result: " + GREEN + winner + " wins!" + RESET);
                return winner;
            }

            if (input.equalsIgnoreCase(p1)) {
                System.out.println(GREEN + "✓ " + p1 + " advances!" + RESET);
                return p1;
            }

            if (input.equalsIgnoreCase(p2)) {
                System.out.println(GREEN + "✓ " + p2 + " advances!" + RESET);
                return p2;
            }

            System.out.println(RED + "✗ Invalid name, please enter '" + p1 + "' or '" + p2 + "' (or '/' to simulate)" + RESET);
        }
    }
}
