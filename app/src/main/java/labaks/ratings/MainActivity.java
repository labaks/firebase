package labaks.ratings;

import android.content.Intent;
import android.graphics.Typeface;
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
        final Typeface iconFont = FontManager.getTypeface(getApplicationContext(), FontManager.FONTAWESOME);

        final ListView listItem = (ListView) findViewById(R.id.listView);
        itemAdapter = new FirebaseListAdapter<Item>(this,
                Item.class,
                R.layout.item,
                FirebaseDatabase.getInstance().getReference().child("items")) {
            @Override
            protected void populateView(View v, final Item model, final int position) {
                TextView tv_alcoholIcon, tv_volumeIcon, tv_priceIcon, tv_totalRatingIcon, tv_numberOfRatersIcon;
                TextView tv_name, tv_alcohol, tv_volume, tv_price, tv_totalRating, tv_numberOfRaters;
                final ImageView iv_itemImage;
                final String itemId = format("item%d", position);
                tv_alcoholIcon = (TextView) v.findViewById(R.id.tvAlcoholIcon);
                tv_volumeIcon = (TextView) v.findViewById(R.id.tvVolumeIcon);
                tv_priceIcon = (TextView) v.findViewById(R.id.tvPriceIcon);
                tv_totalRatingIcon = (TextView) v.findViewById(R.id.tvTotalRatingIcon);
                tv_numberOfRatersIcon = (TextView) v.findViewById(R.id.tvNumberOfRatersIcon);

                tv_alcoholIcon.setTypeface(iconFont);
                tv_volumeIcon.setTypeface(iconFont);
                tv_priceIcon.setTypeface(iconFont);
                tv_totalRatingIcon.setTypeface(iconFont);
                tv_numberOfRatersIcon.setTypeface(iconFont);

                tv_name = (TextView) v.findViewById(R.id.tvItemName);
                tv_alcohol = (TextView) v.findViewById(R.id.tvAlcohol);
                tv_volume = (TextView) v.findViewById(R.id.tvVolume);
                tv_price = (TextView) v.findViewById(R.id.tvPrice);
                tv_totalRating = (TextView) v.findViewById(R.id.tvTotalRating);
                tv_numberOfRaters = (TextView) v.findViewById(R.id.tvNumberOfRaters);

                iv_itemImage = (ImageView) v.findViewById(R.id.iv_itemLogo);

                StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("itemsImages/" + itemId + ".png");
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
                tv_alcohol.setText(String.format("%.1f", model.getAlcohol()));
                if (model.getVolume2() != 0) {
                    tv_volume.setText(String.valueOf(model.getVolume()) + "/" + String.valueOf(model.getVolume2()) + " ml");
                    tv_price.setText(String.format("%.2f", model.getPrice()) + "/" + String.format("%.2f", model.getPrice2()) + " lv");
                } else {
                    tv_volume.setText(String.valueOf(model.getVolume()) + " ml");
                    tv_price.setText(String.format("%.2f", model.getPrice()) + " lv");
                }
                tv_totalRating.setText(String.format("%.1f", model.getTotalRate()));
                tv_numberOfRaters.setText(Integer.toString(model.getUsersRate().size()));
                tv_totalRating.setText(String.format("%.1f", model.getTotalRate()));

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
                            startActivityForResult(AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .build(), SIGN_IN_REQUEST_CODE);
                        }
                    });
        }
        return true;
    }
}
