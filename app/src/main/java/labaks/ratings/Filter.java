package labaks.ratings;

import android.app.DialogFragment;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

public class Filter extends DialogFragment implements OnClickListener {

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().setTitle("Select a country");
        View v = inflater.inflate(R.layout.filter, null);

//        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(activity, android.R.layout.simple_spinner_item, data);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//
//        Spinner spinner = (Spinner)v.findViewById(R.id.spinner_countries);
//        spinner.setAdapter(adapter);
//        spinner.setPrompt("Countries");
//        spinner.setSelection(0);
//        spinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
//                country = adapter.getItem(position);
//            }
//        });

        generateBtn(v, R.drawable.world, R.id.world);
        generateBtn(v, R.drawable.belgium, R.id.belgium);
        generateBtn(v, R.drawable.bulgaria, R.id.bulgaria);
        generateBtn(v, R.drawable.czech, R.id.czech);
        generateBtn(v, R.drawable.england, R.id.england);
        generateBtn(v, R.drawable.germany, R.id.germany);

        return v;
    }

    public void onClick(View v) {
        String country = v.getResources().getResourceEntryName(v.getId());
        MainActivity activity = (MainActivity) getActivity();
        activity.displayItems(country);

        dismiss();
    }


    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
    }

    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
    }

    private void generateBtn(View v, int drawableId, int btnId) {
        Drawable drawable = getActivity().getResources().getDrawable(drawableId);
        drawable.setBounds(0, 0, 60, 60);
        Button button = (Button) v.findViewById(btnId);
        button.setCompoundDrawables(drawable, null, null, null);
        button.setOnClickListener(this);
    }
}
