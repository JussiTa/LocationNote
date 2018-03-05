package com.example.jti.locationnote.datamanagement;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

import java.io.BufferedReader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;


/**
 * Created by jussi on 12/11/16.
 */

public class HandleFiles {

    private static String FILENAME = "muistilista";
    private ArrayList<String> myStringArray = new ArrayList<String>();
    private ArrayList<String> myStringArray2 = new ArrayList<String>();
    private  Context context;
    File file;
    File directory;
    InputStream is;

    public HandleFiles(ArrayList<String> lista1,Context context) {
        myStringArray = lista1;
        this.context=context;

    }


    /*Save list to file if removed or changed items in ListActivity and excepted file is exist.

     */
    public void saveModifiedList(ArrayList<String> myStringArray) throws IOException {
        FileOutputStream outputStream = null;
        OutputStreamWriter osw = null;
        try {
            outputStream = context.openFileOutput(FILENAME, Context.MODE_PRIVATE);
            osw = new OutputStreamWriter(outputStream);
            int i = 0;
            for (String data2 : myStringArray)
                if (i == 0)
                    osw.write(data2 + "\n");
                else
                    osw.append(data2 + "\n");
            i++;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        osw.close();

    }


    public ArrayList<String> getList() throws FileNotFoundException {
        if (!myStringArray.isEmpty())
            myStringArray.clear();
        //Alusteaan lukijat
        FileInputStream is = null;

        //Haetaan tiedosto lukua varten
        is = context.openFileInput(FILENAME);

        //If file not found
        if (is !=null) {
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            try {
                String word = "  ";
                while ((word = br.readLine()) != null) {
                    myStringArray.add(word);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else
            throw new FileNotFoundException();

        return myStringArray;
    }


    //Udate current list to being file
    public ArrayList<String> updateoOrCreateFile(String data) throws IOException {
        FileInputStream is = null;
        OutputStreamWriter osw = null;
        //Get the file for reading
        try {
            is = context.openFileInput(FILENAME);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        //If file found
        if (is != null) {
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            try {
                String word = " ";
                while ((word = br.readLine()) != null) {
                    myStringArray2.add(word);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //Checking file for write and after write.
        FileOutputStream outputStream = null;
        outputStream = context.openFileOutput(FILENAME, Context.MODE_PRIVATE);
        osw = new OutputStreamWriter(outputStream);
        if (!myStringArray2.isEmpty()) {
            myStringArray2.add(data);
            int i = 0;
            for (String data2 : myStringArray2) {
                //First line without append
                if (i == 0)
                    osw.write(data2 + "\n");
                else
                    osw.append(data2 + "\n");
                i++;
            }
        }
        //If file is empty.
        else
            osw.write(data);
        osw.close();
        myStringArray.clear();
        myStringArray = myStringArray2;
        return  myStringArray;
    }

    /*public File[] getListOfFiles(){

        String path = Environment.getExternalStorageDirectory().toString()+"/AANITTEET";

        File[] files;

        File file = new File(path);
        files=file.listFiles();

        return files;



    }*/



}


