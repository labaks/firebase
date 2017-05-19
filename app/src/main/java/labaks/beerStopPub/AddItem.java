package labaks.beerStopPub;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddItem extends AppCompatActivity {

    private EditText et_Name, et_Alcohol, et_Price1, et_Price2, et_Volume1, et_Volume2, et_Description;
    private Spinner sp_Country;
    private String[] countries = {"belgium", "bulgaria", "czech", "england", "germany"};
    private boolean is_et_Name, is_et_Alcohol, is_et_Price1, is_et_Price2, is_et_Volume1, is_et_Volume2;
    private boolean isPriceVolume2 = true;
    private int volume1, volume2;
    private float price1, price2, alcohol;

    private final DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("items");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_item);
        is_et_Name = is_et_Alcohol = is_et_Price1 = is_et_Price2 = is_et_Volume1 = is_et_Volume2 = false;

        et_Name = (EditText) findViewById(R.id.et_name);
        et_Alcohol = (EditText) findViewById(R.id.et_alcohol);
        et_Price1 = (EditText) findViewById(R.id.et_price_1);
        et_Price2 = (EditText) findViewById(R.id.et_price_2);
        et_Volume1 = (EditText) findViewById(R.id.et_volume_1);
        et_Volume2 = (EditText) findViewById(R.id.et_volume_2);
        et_Description = (EditText) findViewById(R.id.et_description);
        sp_Country = (Spinner) findViewById(R.id.spinner_country);

        ArrayAdapter<String> countriesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, countries);
        countriesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_Country.setAdapter(countriesAdapter);
        sp_Country.setPrompt("Country");
    }


    public void addItem(View view) {
        String name = et_Name.getText().toString();
        String alcoholStr = et_Alcohol.getText().toString();
        String price1Str = et_Price1.getText().toString();
        String price2Str = et_Price2.getText().toString();
        String volume1Str = et_Volume1.getText().toString();
        String volume2Str = et_Volume2.getText().toString();
        String description = et_Description.getText().toString();
        String country = sp_Country.getSelectedItem().toString();

        is_et_Name = !(TextUtils.isEmpty(name));
        is_et_Alcohol = !(TextUtils.isEmpty(alcoholStr));
        is_et_Price1 = !(TextUtils.isEmpty(price1Str));
        is_et_Price2 = !(TextUtils.isEmpty(price2Str));
        is_et_Volume1 = !(TextUtils.isEmpty(volume1Str));
        is_et_Volume2 = !(TextUtils.isEmpty(volume2Str));

        if (!is_et_Name) {
            et_Name.setError("Required");
        }

        if (is_et_Alcohol) {
            alcohol = Float.parseFloat(alcoholStr);
        } else {
            et_Alcohol.setError("Required");
        }

        if (is_et_Price1) {
            price1 = Float.parseFloat(price1Str);
        } else {
            et_Price1.setError("Required");
        }

        if (is_et_Volume1) {
            volume1 = Integer.parseInt(volume1Str);
        } else {
            et_Volume1.setError("Required");
        }

        if (is_et_Price2 && !is_et_Volume2) {
            et_Volume2.setError("Required if price 2 is not empty");
            isPriceVolume2 = false;
        } else if (!is_et_Price2 && is_et_Volume2) {
            et_Price2.setError("Required if volume 2 is not empty");
            isPriceVolume2 = false;
        } else if (is_et_Price2) {
            if (is_et_Volume2) {
                price2 = Float.parseFloat(price2Str);
                volume2 = Integer.parseInt(volume2Str);
                isPriceVolume2 = true;
            }
        }

        if (is_et_Name && is_et_Alcohol && is_et_Price1 && is_et_Volume1 && isPriceVolume2) {
            Item item;

            if (price2 != 0) {
                item = new Item(name, alcohol, country, description, price1, volume1, price2, volume2);
            } else {
                item = new Item(name, alcohol, country, description, price1, volume1);
            }

            AddItemHelper addItemHelper = new AddItemHelper(item, ref);
            ref.addListenerForSingleValueEvent(addItemHelper);
            et_Name.getText().clear();
            et_Alcohol.getText().clear();
            et_Description.getText().clear();
            et_Price1.getText().clear();
            et_Price2.getText().clear();
            et_Volume1.getText().clear();
            et_Volume2.getText().clear();

            Toast.makeText(this, item.getName() + " added", Toast.LENGTH_LONG).show();
        }
    }
}
