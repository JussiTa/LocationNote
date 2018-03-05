package com.example.jti.locationnote.gui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

import com.example.jti.locationnote.R;
import com.example.jti.locationnote.datamanagement.Player;



public class TextInput extends DialogFragment implements TextWatcher {
    private TextView textview;
    private EditText editText2;
    private EditText editText3;
    private Geocoder geocoder;
    private boolean rec=true;
    public RecordButton rb =null;
    private ImageButton button3= null;
    private Player player = new Player();
    private Location location;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View promptsView = inflater.inflate(R.layout.activity_text_input, null);
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout

        builder.setView(promptsView);
        textview = (TextView) promptsView
                .findViewById(R.id.note);

        editText2 = (EditText) promptsView
                .findViewById(R.id.note2);

        editText3 =(EditText) promptsView.findViewById(R.id.note3);
        editText3.addTextChangedListener(this);

        button3= (ImageButton) promptsView.findViewById(R.id.button_record);

        button3.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                if(player.getOnRecord())
                    textview.setText("Start recording");
                else
                    textview.setText("Stop recording");
                player.onRecord(rec,location);
            }
        });


        builder.setPositiveButton(R.string.add, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {

                try {
                    ((MapsActivity)getActivity()).positiveButton(TextInput.this);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        TextInput.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }

    public void setgeoCoder(Geocoder geo){
        this.geocoder = geo;

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        String text =s.toString();
        List<Address> addresses = null;
        try {
            addresses =  geocoder.getFromLocationName(text,1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String lat ="";
        String longi ="";
        if(addresses.size()>0) {
            try {
                double latitude = addresses.get(0).getLatitude();
                double longitude = addresses.get(0).getLongitude();
                lat = Double.toString(latitude);
                longi = Double.toString(longitude);
            }catch(NullPointerException e){
                e.printStackTrace();
            }
        }
        textview.setText(lat+ " , "+ longi);

    }

    public EditText getEditText() {
        return editText2;
    }

    public TextView getTextView(){
        return textview;
    }

    public void setlocation(Location location) {
        this.location = location;
    }


    class RecordButton extends android.support.v7.widget.AppCompatButton {
        boolean mStartRecording = true;

        OnClickListener clicker = new OnClickListener() {
            public void onClick(View v) {
                player.startRecording(location);
                if (mStartRecording) {
                    setText("Stop recording");
                } else {
                    setText("Start recording");
                }
                mStartRecording = !mStartRecording;
            }
        };

        public RecordButton(Context ctx){
            super(ctx);

            setText("Start recording");
            setOnClickListener(clicker);

        }


    }


}
