package com.example.blah.mobilestudio.FileViewer;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.blah.mobilestudio.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Calendar;

/**
 * Created by Stephen on 27/01/2016.
 * The file fragment is only concerned with displaying the file handed to it.
 * No other logic is performed in this class.
 */
public class FileFragment extends Fragment {

    // The file to be displayed
    private File displayedFile;
    private TextView fileText;
    private String fileContents;
    private static String  DEFAULT_TEXT = "No files are open"  +
                                        System.getProperty("line.separator")
                                        + "Select a file to open from the explorer";
    private static String ERROR_TEXT = "Unable to display file: ";
    private static String FILE_CONTENTS = "File contents";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.content_layout, container, false);
        fileText = (TextView) rootView.findViewById(R.id.content_text);
        if(savedInstanceState != null){
            fileContents = savedInstanceState.getString(FILE_CONTENTS);
        }
        displayFileText();
        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState){
        outState.putString(FILE_CONTENTS, fileContents);
        super.onSaveInstanceState(outState);
    }

    /**
     * Changes the contents in the text view to the contents of the file.
     */
    private void displayFileText() {
        // If no file has been selected display the defualt text instructing the user to find a file
        if(displayedFile == null){
            fileText.setText(DEFAULT_TEXT);
            return;
        }

        // If we already have file text, no need to reopen the file
        if(fileContents != null){
            fileText.setText(fileContents);
            return;
        }
        new FileOpenerTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    class FileOpenerTask extends AsyncTask<Void, String, Void> {
        long startTime;
        long endTime;

        @Override
        protected void onPreExecute(){
            super.onPreExecute();

            startTime = Calendar.getInstance().getTimeInMillis();
            fileText.setText("");
        }

        @Override
        protected Void doInBackground(Void[] params) {


            // Open the file and read each lines
            try{
                Log.d("this", "does this happen");
                BufferedReader br = new BufferedReader(new FileReader(displayedFile));
                StringBuilder stringBuilder = new StringBuilder();
                String line = null;

                while ((line = br.readLine()) != null){
                    line = line + System.getProperty("line.separator");
                    stringBuilder.append(line);
                    publishProgress(new String(line));
                }

                br.close();
                fileContents = stringBuilder.toString();
                Log.d("contents", fileContents);

            } catch(IOException e){
                Log.d("error",e.getMessage());
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(String... progress){
            fileText.append(progress[0]);
        }

        @Override
        protected void onPostExecute(Void result){

            endTime = Calendar.getInstance().getTimeInMillis();
            Log.d("time taken", String.valueOf(endTime - startTime));
        }

    }

    public File getDisplayedFile() {
        return displayedFile;
    }

    /**
     *
     * @param displayedFile - the file to be displayed to the user. The file will immediately be
     *                      opened in the FileFragment.
     */
    public void setDisplayedFile(File displayedFile) {

        fileContents = null;
        displayFileText();
        this.displayedFile = displayedFile;
    }
}
