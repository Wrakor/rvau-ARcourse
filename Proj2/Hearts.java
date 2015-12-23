import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
/*import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;*/


public class Hearts {

    private static ArrayList<Player> players = new ArrayList<>();
    private static ArrayList<Card> cards = new ArrayList<>();
    private static int nRounds = 10, winningPlayer = 0;

    public static HashMap<String, Integer> cardValue = new HashMap<>();

    public Hearts() {
        String args[] = {};
        main(args);
    }

    public static void main(String args[]) {
        initializePlayers();
        initializeFigureCards();

        for (int i = 0; i < nRounds; i++) {
            //addCards();
            playRound();
        }

        getWinner();
    }

    private static void initializeFigureCards() {
        cardValue.put("1", 1);
        cardValue.put("2", 2);
        cardValue.put("3", 3);
        cardValue.put("4", 4);
        cardValue.put("5", 5);
        cardValue.put("6", 6);
        cardValue.put("7", 7);
        cardValue.put("8", 8);
        cardValue.put("9", 9);
        cardValue.put("10", 10);
        cardValue.put("valete", 11);
        cardValue.put("dama", 12);
        cardValue.put("rei", 13);
        cardValue.put("as", 14);
    }

    private static void initializePlayers() {
        for (int i = 0; i <= 3; i++)
            players.add(new Player(i));
    }

    private static void addCard(String value, String suit) {
        /*cards.add(new Card("2", "espadas"));
        cards.add(new Card("3", "espadas"));
        cards.add(new Card("5", "copas"));
        cards.add(new Card("rei", "paus"));*/

        cards.add(new Card(value, suit));
    }

    private static void playRound() {

        while (cards.size() != 4) { //esperar que as cartas sejam jogadas
            try {
                System.out.println("size:"+cards.size());
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        getRoundWinner();
    }

    private static void getRoundWinner() {
        String firstCardSuit = cards.get(winningPlayer).getSuit();
        String firstCardValue = cards.get(winningPlayer).getValue();
        int nHearts = 0;

        // Encontrar jogador vencedor da ronda, que vai apanhar as cartas e jogar primeiro a seguir
        for (int i = 0; i < 4; i++) {
            if (cards.get(i).getSuit() == firstCardSuit) {
                if (valueBiggerThan(cards.get(i).getValue(), firstCardValue)) {
                    winningPlayer = i;
                }
            }

            //Se a carta for copa ou dama de espadas, contar o numero de copas que vao ser apanhadas
            if (cards.get(i).getSuit().equals("copas"))
                nHearts++;
            else if (cards.get(i).getSuit().equals("espadas") && cards.get(i).getValue().equals("dama"))
                nHearts += 13;
        }

        System.out.println("Vencedor da ronda: Jogador "+ (winningPlayer + 1) + ", apanha " + nHearts + " copa(s)");

        //Adicionar copas ao vencedor da ronda
        players.get(winningPlayer).addHearts(nHearts);
        cards = new ArrayList<>();
    }

    private static boolean valueBiggerThan(String value1, String value2) {
            if (cardValue.get(value1) > cardValue.get(value2))
                return true;
            else
                return false;
    }

    private static void getWinner() {
        Collections.sort(players);

        System.out.print("Vencedor: ");
        for (int i = 0; i < players.size(); i++) {
            System.out.println("Jogador " + players.get(i).getId() + " com " + players.get(i).getNumberOfHearts() + " copas");
        }
    }
}
