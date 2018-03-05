package com.example.jti.locationnote.datamanagement;

import android.location.Location;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by jti on 3/2/18.
 */

public class Player {


    private MediaRecorder mediaRecorder;
    private boolean rec;
    private File           //file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_ALARMS);
            pathToRecord= Environment.getExternalStorageDirectory();
    private File records = new File(pathToRecord,recordFolder);
    private  static String recordFolder ="aanitteet";
    private static final String LOG_TAG = "TexInput";



    public Player(){

        File file;
        file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_ALARMS);
        if(file ==null) {
            file = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_ALARMS), recordFolder);
            if (!file.mkdirs()) {
                Log.e(LOG_TAG, "Directory not created");
            }

        }


    }



    public void stopRecording() {
        try {
            mediaRecorder.stop();
            mediaRecorder.reset();
            mediaRecorder.release();
            mediaRecorder = null;
            rec = true;
        } catch (IllegalStateException is) {

            Log.e(LOG_TAG, "Stop is not enable");
        }


    }

    //Record to wanted note
    public void onRecord(boolean rec,Location location) {
        if(rec)
            startRecording(location);
        else
            stopRecording();

    }

    public void startRecording(Location location) {
        //TimeStamp for right metafile name
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM:dd:HH:mm");
        String currentTimeStamp = dateFormat.format(new Date());
        String outputfile;
        String latitude = String.valueOf(location.getLatitude());
        String longitude = String.valueOf(location.getLongitude());
                ;
        outputfile= pathToRecord.getAbsolutePath()+"/" +latitude+"-"+longitude+".3gp";
        //Conf recorder
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setOutputFile(outputfile);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try{
            mediaRecorder.prepare();
        }catch (IOException e){
            e.printStackTrace();
            Log.e(LOG_TAG, "prepare() failed ");
        }
        mediaRecorder.start();
        rec=false;
    }


    public boolean getOnRecord(){
        return rec;
    }






}
