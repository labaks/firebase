package labaks.ratings;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class itemProfile extends AppCompatActivity {

    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_profile);
        textView = (TextView) findViewById(R.id.textView);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            textView.setText(bundle.getString("itemId"));
        }
    }
}
