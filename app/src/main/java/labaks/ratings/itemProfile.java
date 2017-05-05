package labaks.ratings;

import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class itemProfile extends AppCompatActivity {

    private Toolbar toolbar;
    private Item item;
    private TextView tv_alcoholIcon, tv_volumeIcon, tv_priceIcon, tv_totalRatingIcon, tv_numberOfRatersIcon;
    private TextView tv_alcohol, tv_volume, tv_price, tv_totalRating, tv_numberOfRaters, tv_description;
    private RatingBar rb_userRatingBar;
    private String itemId;
    ImageView iv_itemImage, iv_flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_profile);

        final String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        item = (Item) getIntent().getExtras().getSerializable("item");
        itemId = getIntent().getExtras().getString("itemId");
        Typeface iconFont = FontManager.getTypeface(getApplicationContext(), FontManager.FONTAWESOME);

        iv_itemImage = (ImageView) findViewById(R.id.iv_itemLogo);

        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("itemsImages/" + itemId + ".png");
        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Log.e("Image URL", String.valueOf(uri));
                Picasso.with(getApplicationContext())
                        .load(uri)
                        .placeholder(R.drawable.mug)
                        .error(R.drawable.mug)
                        .into(iv_itemImage);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("Image URL", "FAILURE");
                Picasso.with(getApplicationContext()).load(R.drawable.mug).into(iv_itemImage);
            }
        });

        tv_alcoholIcon = (TextView) findViewById(R.id.tvAlcoholIcon);
        tv_volumeIcon = (TextView) findViewById(R.id.tvVolumeIcon);
        tv_priceIcon = (TextView) findViewById(R.id.tvPriceIcon);
        tv_totalRatingIcon = (TextView) findViewById(R.id.tvTotalRatingIcon);
        tv_numberOfRatersIcon = (TextView) findViewById(R.id.tvNumberOfRatersIcon);

        tv_alcoholIcon.setTypeface(iconFont);
        tv_volumeIcon.setTypeface(iconFont);
        tv_priceIcon.setTypeface(iconFont);
        tv_totalRatingIcon.setTypeface(iconFont);
        tv_numberOfRatersIcon.setTypeface(iconFont);

        tv_alcohol = (TextView) findViewById(R.id.tvAlcohol);
        tv_volume = (TextView) findViewById(R.id.tvVolume);
        tv_price = (TextView) findViewById(R.id.tvPrice);
        tv_totalRating = (TextView) findViewById(R.id.tvTotalRating);
        tv_numberOfRaters = (TextView) findViewById(R.id.tvNumberOfRaters);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(item.getName());

        tv_alcohol.setText(String.format("%.1f", item.getAlcohol()));
        if (item.getVolume2() != 0) {
            tv_volume.setText(String.valueOf(item.getVolume()) + "/" + String.valueOf(item.getVolume2()) + " ml");
            tv_price.setText(String.format("%.2f", item.getPrice()) + "/" + String.format("%.2f", item.getPrice2()) + " lv");
        } else {
            tv_volume.setText(String.valueOf(item.getVolume()) + " ml");
            tv_price.setText(String.format("%.2f", item.getPrice()) + " lv");
        }
        tv_totalRating.setText(String.format("%.1f", item.getTotalRate()));
        tv_numberOfRaters.setText(Integer.toString(item.getUsersRate().size()));

        iv_flag = (ImageView) findViewById(R.id.iv_flag);
        int flagId = getApplicationContext().getResources().getIdentifier(item.getCountry(), "drawable", getPackageName());
        if (flagId != 0) {
            iv_flag.setImageResource(flagId);
        }

        rb_userRatingBar = (RatingBar) findViewById(R.id.rbItemRate);
        if (item.getUsersRate().get(userId) != null) {
            rb_userRatingBar.setRating(item.getUsersRate().get(userId));
        } else {
            rb_userRatingBar.setRating(0);
        }
        rb_userRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                FirebaseDatabase.getInstance().getReference().child("items")
                        .child(itemId)
                        .child("usersRate")
                        .child(userId)
                        .setValue(v);
                FirebaseDatabase.getInstance().getReference().child("items")
                        .child(itemId)
                        .child("totalRate")
                        .setValue(item.getTotalRate());
                tv_totalRating.setText(String.format("%.1f", item.getTotalRate()));
            }
        });

        tv_description = (TextView) findViewById(R.id.tvDescription);
        tv_description.setText(item.getDescription());
    }
}
