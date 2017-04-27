package labaks.ratings;

import android.util.Log;

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

//    public Item(String name, float totalRate, Map<String, Float> usersRate) {
//        Log.e("CONSTR", "params");
//        this.name = name;
//        this.totalRate = totalRate;
//        this.usersRate = usersRate;
//    }

    public Item() {
        Log.e("CONSTR", "empty");

    }

    public String getName() {
        Log.e("get", "name");
        return name;
    }

    public void setName(String name) {
        Log.e("set", "name");
        this.name = name;
    }

    public float getTotalRate() {
        Log.e("get", "totalRate");
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
        Log.e("set", "totalRate");
        // need it
    }

    public Map<String, Float> getUsersRate() {
        Log.e("get", "usersRate");
        return usersRate;
    }

    public void setUsersRate(Map<String, Float> usersRate) {
        Log.e("set", "usersRate");
        this.usersRate = usersRate;
        totalRate = calculateTotalRate();
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