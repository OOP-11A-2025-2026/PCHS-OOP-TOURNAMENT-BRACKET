package CLI_and_Reading;
import java.util.*;

public class TournamentCLI{
    private static Scanner scanner = new Scanner(System.in);
    public static void main(String[] args)
    {
        System.out.println("Single Elimination Tournament");
        int n = readPlayerCount();
        List<String> players = readPlayers(n);
        runTournament(players);
    }
    private static int readPlayerCount() {
        while(true)
        {
            System.out.print("Enter number of participants(min 2): ");
            try{
                int n = Integer.parseInt(scanner.nextLine());
                if(n>=2)
                {
                    return n;
                }
                System.out.println("\nError");
            }
            catch(NumberFormatException e)
            {
                System.out.println("\nError");
            }
        }
    }
    private static List<String> readPlayers(int n)
    {
        List<String> players = new ArrayList<>();
        Set<String> uniqueCheck = new HashSet<>();
        for(int i = 0;i<n;i++)
        {
            while(true)
            {
                System.out.print("Name of participant N:"+(i+1)+": ");
                String name = scanner.nextLine().trim();
                if(name.isEmpty())
                {
                    System.out.println("Name can't be empty");
                    continue;
                }
                if(uniqueCheck.contains(name.toLowerCase()))
                {
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

    private static void runTournament(List<String> players)
    {
        int round = 1;
        while(players.size() > 1)
        {
            System.out.println("\nRound"+round);
            Collections.shuffle(players);
            List<String> winners = new ArrayList<>();
            for(int i=0;i<players.size();i+=2)
            {
                if(i+1>=players.size())
                {
                    System.out.println(players.get(i) + " receives bye and continues");
                    winners.add(players.get(i));
                    break;
                }
                String p1 = players.get(i);
                String p2 = players.get(i + 1);
                System.out.println("Match: "+p1+" vs "+p2);
                String winner = getWinnerFromUser(p1, p2);
                winners.add(winner);
            }
            players = winners;
            round++;
        }

        System.out.println("\nWinner: "+players.get(0));
    }

    private static String getWinnerFromUser(String p1, String p2)
    {
        while(true)
        {
            System.out.print("\nWho is the winner?("+p1+"/"+p2+"):");
            String input = scanner.nextLine().trim();
            if(input.equalsIgnoreCase(p1))
            {
                return p1;
            }
            if(input.equalsIgnoreCase(p2))
            {
                return p2;
            }
            System.out.println("\nInvalid name");
        }
    }
}
