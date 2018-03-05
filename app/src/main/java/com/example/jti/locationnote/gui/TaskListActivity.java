package com.example.jti.locationnote.gui;

import android.app.ListActivity;
import android.content.Intent;


import android.os.Bundle;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;


import com.example.jti.locationnote.R;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

//Älä extendaa listactivityää
public class TaskListActivity extends ListActivity {

    // private  String FILENAME = "muistilista";
    private ArrayList<String> myStringArray = new ArrayList<>();
    //private static final Object SELECTION = "JUU";
    // This is the Adapter being used to display the list's data
    //private boolean delete=false;
    private View view2;
    private String item;
    private ArrayAdapter<String> adapter =null;
    private static String FILENAME = "muistilista";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getListFromFile();
        setContentView(R.layout.activity_task_list);
        final ListView listView = getListView();
        adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, myStringArray);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {



            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    final int position, long id) {
                item = (String) parent.getItemAtPosition(position);
                view2 =view;
                DeleteDialog dl = new DeleteDialog();
                dl.show(getFragmentManager(),"dialog");

            }

        });


    }

    private void getListFromFile() {
        FileInputStream is = null;

        try {
            is = openFileInput(FILENAME);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        BufferedReader br = null;
        if(is !=null) {
            InputStreamReader isr = new InputStreamReader(is);
            br = new BufferedReader(isr);
        }
        try {

            String word;
            assert br != null;
            while ((word = br.readLine()) != null) {
                myStringArray.add(word);

            }

        } catch (Exception e) {

            e.printStackTrace();
        }
    }


    public void deleteItem() {
        // delete = true;
        myStringArray.remove(item);

        adapter.notifyDataSetChanged();
        view2.setAlpha(1);


        Intent intent = new Intent(TaskListActivity.this,MapsActivity.class);
        // if(myStringArray.contains(item))
        intent.putStringArrayListExtra("Item deleted", myStringArray);

        setResult(RESULT_OK, intent);


    }
}
