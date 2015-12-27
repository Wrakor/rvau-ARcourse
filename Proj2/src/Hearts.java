import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
/*import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;*/


public class Hearts {

    private static ArrayList<Player> players = new ArrayList<>();
    private static ArrayList<Card> cards = new ArrayList<>();
    private static int nRounds = 1, winningPlayer = 0;

    public static HashMap<String, Integer> cardValue = new HashMap<>();

    public Hearts() {
        initializePlayers();
        initializeCardsValue();
    }

    private static void initializeCardsValue() {
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

    public static void addCard(String value, String suit) {
        /*cards.add(new Card("2", "espadas"));
        cards.add(new Card("3", "espadas"));
        cards.add(new Card("5", "copas"));
        cards.add(new Card("rei", "paus"));*/

        cards.add(new Card(value, suit));
    }

    public void addCards(ArrayList<Card> cards) {
        if (cards.size() == 4)
            this.cards = cards;
    }

    public static int getRoundWinner() {
        String firstCardSuit = cards.get(winningPlayer).getSuit();
        String firstCardValue = cards.get(winningPlayer).getValue();
        int nHearts = 0;
        nRounds++;

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

        if (nRounds == 13)
            getWinner();

        return winningPlayer;
    }

    private static boolean valueBiggerThan(String value1, String value2) {
            if (cardValue.get(value1) > cardValue.get(value2))
                return true;
            else
                return false;
    }

    public static int getWinner() {
        Collections.sort(players);

        System.out.print("Vencedor: ");
        for (int i = 0; i < players.size(); i++) {
            System.out.println("Jogador " + players.get(i).getId() + " com " + players.get(i).getNumberOfHearts() + " copas");
        }

        return players.get(0).getId();
    }
}
