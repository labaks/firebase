package labaks.ratings;

import android.app.Activity;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

abstract class FirebaseCustomAdapter extends FirebaseListAdapter<Item> {

    final Typeface iconFont = FontManager.getTypeface(mActivity, FontManager.FONTAWESOME);

    public FirebaseCustomAdapter(Activity activity, Query ref) {
        super(activity, Item.class, R.layout.item, ref);
    }

    @Override
    protected void populateView(View v, Item model, int position) {
        TextView tv_alcoholIcon, tv_volumeIcon, tv_priceIcon, tv_totalRatingIcon, tv_numberOfRatersIcon;
        TextView tv_name, tv_alcohol, tv_volume, tv_price, tv_totalRating, tv_numberOfRaters;
        final ImageView iv_itemImage;
        final String itemId = String.valueOf(this.getRef(position))
                .replace(String.valueOf(FirebaseDatabase.getInstance().getReference().child("items")) + "/", "");
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
                Picasso.with(mActivity).load(uri).into(iv_itemImage);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("Image URL", "FAILURE");
                Picasso.with(mActivity).load(R.drawable.mug).into(iv_itemImage);
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
    }
}
