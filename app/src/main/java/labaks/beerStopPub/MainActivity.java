package labaks.beerStopPub;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class MainActivity extends AppCompatActivity {

    private static final int SIGN_IN_REQUEST_CODE = 1;
    private RelativeLayout activity_main;
    private GridView gridItem;
    private Query query;
    private final DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("items");
    private boolean isAdmin = false;

    private final String world = "world";

    DialogFragment filter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        activity_main = (RelativeLayout) findViewById(R.id.activity_main);
        gridItem = (GridView) findViewById(R.id.gridView);

        filter = new Filter();

        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            startActivityForResult(AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .build(), SIGN_IN_REQUEST_CODE);
        } else {
            isAdmin = FirebaseAuth.getInstance().getCurrentUser().getEmail().equals("labaks93@gmail.com");
            invalidateOptionsMenu();
            displayItems(world);
        }
    }

    public void displayItems(String country) {
        if (country.equals(world)) {
            query = ref;
        } else {
            query = ref.orderByChild("country").equalTo(country);
        }
        FirebaseCustomAdapter itemAdapter = new FirebaseCustomAdapter(this, this, query) {
            @Override
            protected void populateView(View v, final Item model, final int position) {
                super.populateView(v, model, position);
                final String itemId = String.valueOf(this.getRef(position))
                        .replace(String.valueOf(FirebaseDatabase.getInstance().getReference().child("items")) + "/", "");

                v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent myIntent = new Intent(MainActivity.this, ItemProfile.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("item", model);
                        bundle.putString("itemId", itemId);
                        myIntent.putExtras(bundle);
                        startActivity(myIntent);
                    }
                });
            }
        };
        gridItem.setAdapter(itemAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SIGN_IN_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Snackbar.make(activity_main, "Вход выполнен", Snackbar.LENGTH_SHORT).show();
                Toast.makeText(this, "User: " + FirebaseAuth.getInstance().getCurrentUser().getEmail(), Toast.LENGTH_LONG).show();
                displayItems(world);
                invalidateOptionsMenu();
            } else {
                Snackbar.make(activity_main, "Вход не выполнен", Snackbar.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        MenuItem add = menu.findItem(R.id.menu_add);
        add.setVisible(isAdmin);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.menu_add:
                Intent myIntent = new Intent(MainActivity.this, AddItem.class);
                startActivity(myIntent);
                return true;
            case R.id.menu_filter:
                filter.show(getFragmentManager(), "filter");
                return true;
            case R.id.menu_signout:
                AuthUI.getInstance().signOut(this)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Snackbar.make(activity_main, "Выход выполнен", Snackbar.LENGTH_SHORT).show();
                                startActivityForResult(AuthUI.getInstance()
                                        .createSignInIntentBuilder()
                                        .build(), SIGN_IN_REQUEST_CODE);
                            }
                        });
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
