package labaks.ratings;

import java.io.Serializable;
import java.util.Map;

public class Item implements Serializable {

    private String name;
    private float alcohol;
    private String country;
    private String description;
    private float price, price2;
    private int volume, volume2;
    private float totalRate;
    private Map<String, Float> usersRate;

    public Item() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    float getTotalRate() {
        return calculateTotalRate();
    }

    private float calculateTotalRate() {
        double total = 0.0;
        for (Map.Entry<String, Float> entry : usersRate.entrySet()) {
            total += entry.getValue();
        }
        return (float) (total / usersRate.size());
    }

    public void setTotalRate(float totalRate) {
        // need it
    }

    Map<String, Float> getUsersRate() {
        return usersRate;
    }

    public void setUsersRate(Map<String, Float> usersRate) {
        this.usersRate = usersRate;
        totalRate = calculateTotalRate();
    }

    float getAlcohol() {
        return alcohol;
    }

    public void setAlcohol(float alcohol) {
        this.alcohol = alcohol;
    }

    String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    float getPrice2() {
        return price2;
    }

    public void setPrice2(float price2) {
        this.price2 = price2;
    }

    int getVolume2() {
        return volume2;
    }

    public void setVolume2(int volume2) {
        this.volume2 = volume2;
    }
}
