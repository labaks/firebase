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

        Drawable drawableWorld = getActivity().getResources().getDrawable(R.drawable.world);
        drawableWorld.setBounds(0, 0, 60, 60);
        Button btnWorld = (Button) v.findViewById(R.id.world);
        btnWorld.setCompoundDrawables(drawableWorld, null, null, null);
        btnWorld.setOnClickListener(this);

        Drawable drawableBelgium = getActivity().getResources().getDrawable(R.drawable.belgium);
        drawableBelgium.setBounds(0, 0, 60, 60);
        Button btnBelgium = (Button) v.findViewById(R.id.belgium);
        btnBelgium.setCompoundDrawables(drawableBelgium, null, null, null);
        btnBelgium.setOnClickListener(this);

        Drawable drawableBulgaria = getActivity().getResources().getDrawable(R.drawable.bulgaria);
        drawableBulgaria.setBounds(0, 0, 60, 60);
        Button btnBulgaria = (Button) v.findViewById(R.id.bulgaria);
        btnBulgaria.setCompoundDrawables(drawableBulgaria, null, null, null);
        btnBulgaria.setOnClickListener(this);

        Drawable drawableCzech = getActivity().getResources().getDrawable(R.drawable.czech);
        drawableCzech.setBounds(0, 0, 60, 60);
        Button btnCzech = (Button) v.findViewById(R.id.czech);
        btnCzech.setCompoundDrawables(drawableCzech, null, null, null);
        btnCzech.setOnClickListener(this);

        Drawable drawableEngland = getActivity().getResources().getDrawable(R.drawable.england);
        drawableEngland.setBounds(0, 0, 60, 60);
        Button btnEngland = (Button) v.findViewById(R.id.england);
        btnEngland.setCompoundDrawables(drawableEngland, null, null, null);
        btnEngland.setOnClickListener(this);

        Drawable drawableGermany = getActivity().getResources().getDrawable(R.drawable.germany);
        drawableGermany.setBounds(0, 0, 60, 60);
        Button btnGermany = (Button) v.findViewById(R.id.germany);
        btnGermany.setCompoundDrawables(drawableGermany, null, null, null);
        btnGermany.setOnClickListener(this);

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
}
