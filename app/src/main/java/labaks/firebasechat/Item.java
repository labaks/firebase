package labaks.firebasechat;

public class Item {

    private String name;
    private float rate;

    public Item(String name, float rate) {
        this.name = name;
        this.rate = rate;
    }

    public Item() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getRate() {
        return rate;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }
}
