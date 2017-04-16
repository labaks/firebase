package labaks.firebasechat;

import java.util.Map;

public class Item {

    private String name;
    private float totalRate;
    private Map<String, Float> usersRate;

    public Item(String name, float totalRate, Map<String, Float> usersRate) {
        this.name = name;
        this.totalRate = totalRate;
        this.usersRate = usersRate;
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

    public Map<String, Float> getUsersRate() {
        return usersRate;
    }

    public void setUsersRate(Map<String, Float> usersRate) {
        this.usersRate = usersRate;
    }
}
