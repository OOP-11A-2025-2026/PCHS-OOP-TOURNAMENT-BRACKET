package tournament.CLI;

import tournament.backend.Match;
import tournament.backend.Tournament;
import java.util.*;

public class CLI {
    private static final Scanner scanner = new Scanner(System.in);
    private static final Random random = new Random();

    private static final boolean ANSI = supportsANSI();

    private static final String RESET   = ANSI ? "\u001B[0m"  : "";
    private static final String BOLD    = ANSI ? "\u001B[1m"  : "";
    private static final String CYAN    = ANSI ? "\u001B[36m" : "";
    private static final String GREEN   = ANSI ? "\u001B[32m" : "";
    private static final String YELLOW  = ANSI ? "\u001B[33m" : "";
    private static final String RED     = ANSI ? "\u001B[31m" : "";
    private static final String BLUE    = ANSI ? "\u001B[34m" : "";
    private static final String MAGENTA = ANSI ? "\u001B[35m" : "";

    private static final boolean UNICODE_OK = supportsUnicode();

    private static boolean supportsANSI() {
        String os = System.getProperty("os.name").toLowerCase();
        boolean isUnix = os.contains("nix") || os.contains("nux") || os.contains("mac") || os.contains("darwin");
        boolean hasTERM = System.getenv("TERM") != null;
        return isUnix || hasTERM;
    }

    private static boolean supportsUnicode() {
        String os = System.getProperty("os.name").toLowerCase();
        return !os.contains("windows");
    }

    public static void main(String[] args) {
        printBanner();

        int n = readPlayerCount();
        List<String> players = readPlayers(n);

        Tournament tournament = new Tournament(players);
        tournament.buildBracket();

        printSeparator();
        System.out.println(BOLD + CYAN + (UNICODE_OK ? "🏆 TOURNAMENT BEGINS! 🏆" : "TOURNAMENT BEGINS!") + RESET);
        printSeparator();

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

        if (UNICODE_OK) {
            System.out.println(BOLD + CYAN + "╔════════════════════════════════════════╗" + RESET);
            System.out.println(BOLD + CYAN + "║                                        ║" + RESET);
        } else {
            System.out.println("+----------------------------------------+");
            System.out.println("|                                        |");
        }

        String title = UNICODE_OK ? "⚔️ TOURNAMENT" : "TOURNAMENT";
        int width = 40;
        int padding = (width - title.length()) / 2;
        String inside;

        if (UNICODE_OK)
            inside = "║" + " ".repeat(padding) + YELLOW + title + CYAN + " ".repeat(width - title.length() - padding) + "║";
        else
            inside = "|" + " ".repeat(padding) + title + " ".repeat(width - title.length() - padding) + "|";

        System.out.println(BOLD + CYAN + inside + RESET);

        if (UNICODE_OK) {
            System.out.println(BOLD + CYAN + "║                                        ║" + RESET);
            System.out.println(BOLD + CYAN + "╚════════════════════════════════════════╝" + RESET);
        } else {
            System.out.println("|                                        |");
            System.out.println("+----------------------------------------+");
        }

        System.out.println();
    }

    private static void printSeparator() {
        if (UNICODE_OK)
            System.out.println(CYAN + "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━" + RESET);
        else
            System.out.println("----------------------------------------");
    }

    private static void printTournamentComplete(Tournament tournament) {
        System.out.println();
        printSeparator();
        System.out.println(BOLD + GREEN + (UNICODE_OK ? "🎉 TOURNAMENT COMPLETE! 🎉" : "TOURNAMENT COMPLETE!") + RESET);
        printSeparator();
        System.out.println();
        System.out.println(BOLD + YELLOW + (UNICODE_OK ? "👑 CHAMPION: " : "CHAMPION: ") + MAGENTA + tournament.getWinner() + RESET);
        System.out.println();
        printSeparator();

        System.out.println(BOLD + BLUE + "\nMATCH HISTORY:" + RESET);
        System.out.println();
        for (Match match : tournament.getAllMatches()) {
            System.out.println("  " + GREEN + "> " + RESET + match);
        }
        System.out.println();
        printSeparator();
    }

    private static int readPlayerCount() {
        while (true) {
            System.out.print(BOLD + "Enter number of participants (min 2): " + RESET);
            try {
                int n = Integer.parseInt(scanner.nextLine());
                if (n >= 2) {
                    System.out.println(GREEN + "OK: " + n + " participants registered" + RESET);
                    return n;
                }
                System.out.println(RED + "Error: Number must be at least 2" + RESET);
            } catch (NumberFormatException e) {
                System.out.println(RED + "Error: Invalid number" + RESET);
            }
        }
    }

    private static List<String> readPlayers(int n) {
        List<String> players = new ArrayList<>();
        Set<String> uniqueCheck = new HashSet<>();

        System.out.println();
        printSeparator();
        System.out.println(BOLD + YELLOW + "PARTICIPANT REGISTRATION" + RESET);
        printSeparator();

        for (int i = 0; i < n; i++) {
            while (true) {
                System.out.print(BOLD + "Participant #" + (i + 1) + ": " + RESET);
                String name = scanner.nextLine().trim();

                if (name.isEmpty()) {
                    System.out.println(RED + "  Error: Name can't be empty" + RESET);
                    continue;
                }

                if (uniqueCheck.contains(name.toLowerCase())) {
                    System.out.println(RED + "  Error: Name already exists" + RESET);
                    continue;
                }

                uniqueCheck.add(name.toLowerCase());
                players.add(name);
                System.out.println(GREEN + "  OK: " + name + " registered" + RESET);
                break;
            }
        }

        return players;
    }

    private static String promptWinner(String p1, String p2) {
        System.out.println();
        System.out.println(BOLD + YELLOW + (UNICODE_OK ? "⚔️  MATCH TIME!" : "MATCH TIME!") + RESET);
        System.out.println(BLUE + "  " + p1 + RESET + " " + BOLD + "vs" + RESET + " " + MAGENTA + p2 + RESET);

        while (true) {
            System.out.print(BOLD + "Enter winner (or '/' to simulate): " + RESET);
            String input = scanner.nextLine().trim();

            if (input.equals("/")) {
                String winner = random.nextBoolean() ? p1 : p2;
                System.out.println(YELLOW + "Simulated result: " + GREEN + winner + " wins!" + RESET);
                return winner;
            }

            if (input.equalsIgnoreCase(p1)) {
                System.out.println(GREEN + p1 + " advances!" + RESET);
                return p1;
            }

            if (input.equalsIgnoreCase(p2)) {
                System.out.println(GREEN + p2 + " advances!" + RESET);
                return p2;
            }

            System.out.println(RED + "Invalid name. Enter '" + p1 + "' or '" + p2 + "'" + RESET);
        }
    }
}
