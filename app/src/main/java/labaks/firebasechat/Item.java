package labaks.firebasechat;

public class Item {

    private String name;
    private float totalRate;
    private float userRate;

    public Item(String name, float totalRate, float userRate) {
        this.name = name;
        this.totalRate = totalRate;
        this.userRate = userRate;
    }

    public Item() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getTotalRate() {
        return totalRate;
    }

    public void setTotalRate(float totalRate) {
        this.totalRate = totalRate;
    }

    public float getUserRate() {
        return userRate;
    }

    public void setUserRate(float userRate) {
        this.userRate = userRate;
    }
}
