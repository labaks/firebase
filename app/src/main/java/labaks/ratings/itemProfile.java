package labaks.ratings;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

public class itemProfile extends AppCompatActivity {

    Toolbar toolbar;
    Item item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_profile);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        item = (Item) getIntent().getExtras().getSerializable("item");
        toolbar.setTitle(item.getName());
    }
}
