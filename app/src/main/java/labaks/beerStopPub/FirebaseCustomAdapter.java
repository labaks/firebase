package labaks.beerStopPub;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

abstract class FirebaseCustomAdapter extends FirebaseListAdapter<Item> {

    private final Typeface iconFont = FontManager.getTypeface(mActivity, FontManager.FONTAWESOME);
    private Context context;
    private DatabaseReference root = FirebaseDatabase.getInstance().getReference().child("items");

    FirebaseCustomAdapter(Context context, Activity activity, Query ref) {
        super(activity, Item.class, R.layout.item, ref);
        this.context = context;
    }

    @Override
    protected void populateView(View v, final Item model, int position) {
        final TextView tv_name, tv_alcohol, tv_volume, tv_price, tv_totalRating, tv_numberOfRaters;
        final ImageView iv_itemImage, iv_flag;
        final String itemId = String.valueOf(this.getRef(position))
                .replace(String.valueOf(root) + "/", "");

        setIconToTextView(v, R.id.tvAlcoholIcon);
        setIconToTextView(v, R.id.tvVolumeIcon);
        setIconToTextView(v, R.id.tvPriceIcon);
        setIconToTextView(v, R.id.tvTotalRatingIcon);
        setIconToTextView(v, R.id.tvNumberOfRatersIcon);

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
                Picasso.with(mActivity)
                        .load(uri)
                        .fit()
                        .placeholder(R.drawable.mug)
                        .error(R.drawable.mug)
                        .into(iv_itemImage);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
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
        float totalRate = model.getTotalRate();
        tv_totalRating.setText(String.format("%.1f", totalRate));
        root.child(itemId).child("totalRate").setValue(totalRate);
        root.child(itemId).child("usersRate").child("temporaryrating").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Object tempSTr = dataSnapshot.getValue();
                if (tempSTr != null) {
                    tv_numberOfRaters.setText(Integer.toString(model.getUsersRate().size() - 1));
                } else {
                    tv_numberOfRaters.setText(Integer.toString(model.getUsersRate().size()));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        tv_totalRating.setText(String.format("%.1f", model.getTotalRate()));

        iv_flag = (ImageView) v.findViewById(R.id.iv_flag);
        int flagId = context.getResources().getIdentifier(model.getCountry(), "drawable", context.getPackageName());
        if (flagId != 0) {
            iv_flag.setImageResource(flagId);
        }

    }

    private void setIconToTextView(View v, int textViewId) {
        TextView tvWithIcon;
        tvWithIcon = (TextView) v.findViewById(textViewId);
        tvWithIcon.setTypeface(iconFont);
    }
}
