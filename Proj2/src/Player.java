
public class Player implements Comparable{

    private int id, nHearts = 0;

    public Player(int id) {
        this.id = id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void addHearts(int nHearts) {
        this.nHearts += nHearts;
    }

    public int getId() {
        return id;
    }

    public int getNumberOfHearts() {
        return nHearts;
    }

    @Override
    public int compareTo(Object o) {
        if (this.nHearts > ((Player)o).getNumberOfHearts())
            return 1;
        else
            return -1;
    }
}
