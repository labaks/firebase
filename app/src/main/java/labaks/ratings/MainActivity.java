package labaks.ratings;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import static java.lang.String.format;

public class MainActivity extends AppCompatActivity {

    private static int SIGN_IN_REQUEST_CODE = 1;
    private FirebaseListAdapter<Item> itemAdapter;
    RelativeLayout activity_main;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        activity_main = (RelativeLayout) findViewById(R.id.activity_main);

        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            startActivityForResult(AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .build(), SIGN_IN_REQUEST_CODE);
        } else {
            displayItems();
        }
    }

    private void displayItems() {

        ListView listItem = (ListView) findViewById(R.id.listView);
        itemAdapter = new FirebaseListAdapter<Item>(this,
                Item.class,
                R.layout.item,
                FirebaseDatabase.getInstance().getReference().child("items")) {
            @Override
            protected void populateView(View v, final Item model, final int position) {
                final String userId = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
                TextView tv_name, tv_number_of_raters;
                RatingBar rb_userRatingBar;
                final TextView tv_totalRate;
                final ImageView iv_itemImage;
                tv_name = (TextView) v.findViewById(R.id.tvItemName);
                rb_userRatingBar = (RatingBar) v.findViewById(R.id.rbItemRate);
                tv_totalRate = (TextView) v.findViewById(R.id.tvTotalRate);
                tv_number_of_raters = (TextView) v.findViewById(R.id.tvNumberOfRaters);
                iv_itemImage = (ImageView) v.findViewById(R.id.ivItemImage);

                StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("itemsImages/item" + position + ".png");
                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Log.e("Image URL", String.valueOf(uri));
                        Picasso.with(getApplicationContext()).load(uri).into(iv_itemImage);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("Image URL", "FAILURE");
                    }
                });

                tv_name.setText(model.getName());
                if (model.getUsersRate().get(userId) != null) {
                    rb_userRatingBar.setRating(model.getUsersRate().get(userId));
                } else {
                    rb_userRatingBar.setRating(0);
                }
                tv_totalRate.setText(Float.toString(model.getTotalRate()));
                tv_number_of_raters.setText(Integer.toString(model.getUsersRate().size()));
                rb_userRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                    @Override
                    public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                        FirebaseDatabase.getInstance().getReference().child("items")
                                .child(format("Item%d", position))
                                .child("usersRate")
                                .child(userId)
                                .setValue(v);
                        FirebaseDatabase.getInstance().getReference().child("items")
                                .child(format("Item%d", position))
                                .child("totalRate")
                                .setValue(model.getTotalRate());
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
                displayItems();
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
                            finish();
                        }
                    });
        }
        return true;
    }
}
