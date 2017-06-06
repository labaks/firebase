package labaks.beerStopPub;

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

    public Item(String name, float alcohol, String country, String description, float price, int volume) {
        this.name = name;
        this.alcohol = alcohol;
        this.country = country;
        this.description = description;
        this.price = price;
        this.volume = volume;
        this.totalRate = calculateTotalRate();
    }

    public Item(String name, float alcohol, String country, String description, float price, int volume, float price2, int volume2) {
        this.name = name;
        this.alcohol = alcohol;
        this.country = country;
        this.description = description;
        this.price = price;
        this.volume = volume;
        this.price2 = price2;
        this.volume2 = volume2;
        this.totalRate = calculateTotalRate();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getTotalRate() {
        this.totalRate = calculateTotalRate();
        return totalRate;
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
        this.totalRate = calculateTotalRate();
    }

    public Map<String, Float> getUsersRate() {
        return usersRate;
    }

    public void setUsersRate(Map<String, Float> usersRate) {
        this.usersRate = usersRate;
    }

    public void setUsersRateItem(String s, Float f) {
        this.usersRate.put(s, f);
    }

    public float getAlcohol() {
        return alcohol;
    }

    public void setAlcohol(float alcohol) {
        this.alcohol = alcohol;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public float getPrice2() {
        return price2;
    }

    public void setPrice2(float price2) {
        this.price2 = price2;
    }

    public int getVolume2() {
        return volume2;
    }

    public void setVolume2(int volume2) {
        this.volume2 = volume2;
    }
}
