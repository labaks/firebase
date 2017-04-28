package labaks.ratings;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static java.lang.String.format;

public class MainActivity extends AppCompatActivity {

    private static int SIGN_IN_REQUEST_CODE = 1;
    private FirebaseCustomAdapter itemAdapter;
    RelativeLayout activity_main;
    DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("items");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        activity_main = (RelativeLayout) findViewById(R.id.activity_main);

        Typeface iconFont = FontManager.getTypeface(getApplicationContext(), FontManager.FONTAWESOME);


        TextView bg, cz, gm;
        bg = (TextView) findViewById(R.id.Bulgaria);
        cz = (TextView) findViewById(R.id.Czech);
        gm = (TextView) findViewById(R.id.Germany);
        bg.setTypeface(iconFont);
        cz.setTypeface(iconFont);
        gm.setTypeface(iconFont);

        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            startActivityForResult(AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .build(), SIGN_IN_REQUEST_CODE);
        } else {
            displayItems(ref);
        }
    }

    private void displayItems(DatabaseReference ref) {

        final ListView listItem = (ListView) findViewById(R.id.listView);
        itemAdapter = new FirebaseCustomAdapter(this, ref) {
            @Override
            protected void populateView(View v, final Item model, final int position) {
                super.populateView(v, model, position);
                final String itemId = format("item%d", position);

                v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent myIntent = new Intent(MainActivity.this, itemProfile.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("item", model);
                        bundle.putString("itemId", itemId);
                        myIntent.putExtras(bundle);
                        startActivity(myIntent);
                    }
                });
            }
        };
        listItem.setAdapter(itemAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SIGN_IN_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Snackbar.make(activity_main, "Вход выполнен", Snackbar.LENGTH_SHORT).show();
                displayItems(ref);
            } else {
                Snackbar.make(activity_main, "Вход не выполнен", Snackbar.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_signout) {
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
        }
        return true;
    }

    public void filterByCountry(View view) {
        Log.e("PRESSED", view.getResources().getResourceEntryName(view.getId()));
        ref.orderByChild("country").equalTo(view.getResources().getResourceEntryName(view.getId()))
                .addChildEventListener(new ChildEventListener() {
                                           @Override
                                           public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                               Log.e("ref------------------", dataSnapshot.getKey());
                                               DatabaseReference databaseReference;
                                               databaseReference.
//                                               displayItems(dataSnapshot.getRef());

                                           }

                                           @Override
                                           public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                                           }

                                           @Override
                                           public void onChildRemoved(DataSnapshot dataSnapshot) {

                                           }

                                           @Override
                                           public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                                           }

                                           @Override
                                           public void onCancelled(DatabaseError databaseError) {

                                           }
                                       }

                );
    }
}
