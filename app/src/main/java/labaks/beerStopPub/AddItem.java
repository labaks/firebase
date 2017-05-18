package labaks.beerStopPub;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class AddItem extends AppCompatActivity {

    private EditText et_Name, et_Alcohol, et_Price1, et_Price2, et_Volume1, et_Volume2, et_Description;
    private Spinner sp_Country;
    private Button btn_add;
    private String[] countries = {"belgium", "bulgaria", "czech", "england", "germany"};
    private boolean is_et_Name, is_et_Alcohol, is_et_Price1, is_et_Price2, is_et_Volume1, is_et_Volume2, is_et_Description, is_sp_Country;
    private String name, alcoholStr, price1Str, price2Str, volume1Str, volume2Str, description, country;
    private int volume1, volume2;
    private double price1, price2, alcohol;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_item);
        is_et_Name = is_et_Alcohol = is_et_Price1 = is_et_Price2 = is_et_Volume1 = is_et_Volume2 = is_et_Description = is_sp_Country = false;

        et_Name = (EditText) findViewById(R.id.et_name);
        et_Alcohol = (EditText) findViewById(R.id.et_alcohol);
        et_Price1 = (EditText) findViewById(R.id.et_price_1);
        et_Price2 = (EditText) findViewById(R.id.et_price_2);
        et_Volume1 = (EditText) findViewById(R.id.et_volume_1);
        et_Volume2 = (EditText) findViewById(R.id.et_volume_2);
        et_Description = (EditText) findViewById(R.id.et_description);
        sp_Country = (Spinner) findViewById(R.id.spinner_country);
        btn_add = (Button) findViewById(R.id.btn_add);

        ArrayAdapter<String> countriesAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, countries);
        countriesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_Country.setAdapter(countriesAdapter);
        sp_Country.setPrompt("Country");
    }


    public void addItem(View view) {
        name = et_Name.getText().toString();
        alcoholStr = et_Alcohol.getText().toString();
        price1Str = et_Price1.getText().toString();
        price2Str = et_Price2.getText().toString();
        volume1Str = et_Volume1.getText().toString();
        volume2Str = et_Volume2.getText().toString();
        description = et_Description.getText().toString();
        country = sp_Country.getSelectedItem().toString();

        is_et_Name = !(TextUtils.isEmpty(name));
        is_et_Alcohol = !(TextUtils.isEmpty(alcoholStr));
        is_et_Price1 = !(TextUtils.isEmpty(price1Str));
        is_et_Price2 = !(TextUtils.isEmpty(price2Str));
        is_et_Volume1 = !(TextUtils.isEmpty(volume1Str));
        is_et_Volume2 = !(TextUtils.isEmpty(volume2Str));
        is_et_Description = !(TextUtils.isEmpty(description));
        is_sp_Country = !(TextUtils.isEmpty(country));

        if (is_et_Name) {

        } else {
            et_Name.setError("Required");
        }

        if (is_et_Alcohol) {

        } else {
            et_Alcohol.setError("Required");
        }

        if (is_et_Price1) {
            price1 = Double.parseDouble(price1Str);
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
        } else if (!is_et_Price2 && is_et_Volume2) {
            et_Price2.setError("Required if volume 2 is not empty");
        } else if (is_et_Price2) {
            if (is_et_Volume2) {
                price2 = Double.parseDouble(price2Str);
                volume2 = Integer.parseInt(volume2Str);
            }
        }

        if (is_et_Name && is_et_Alcohol && is_et_Price1 && is_et_Volume1) {
            Toast.makeText(this, "Added successfully", Toast.LENGTH_LONG).show();
        }
    }
}
