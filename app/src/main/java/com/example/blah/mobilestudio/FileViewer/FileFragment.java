package com.example.blah.mobilestudio.FileViewer;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

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
    private static String  DEFAULT_TEXT = "No files are open"  +
                                        System.getProperty("line.separator")
                                        + "Select a file to open from the explorer";
    private static String ERROR_TEXT = "Unable to display file: ";
    private static String FILE_CONTENTS = "File contents";
    private static String HTML_OPENING = "<html><body>";
    private static String HTML_CLOSING = "</html></body>";
    WebView webView;


    // TODO save file Contents in on save state
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.content_layout, container, false);
        webView = (WebView) rootView.findViewById(R.id.web_layout);
        displayFileText();
        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState){

        super.onSaveInstanceState(outState);
    }

    /**
     * Changes the contents in the text view to the contents of the file.
     */
    private void displayFileText() {
        // If no file has been selected display the defualt text instructing the user to find a file
        if(displayedFile == null){

            return;
        }

        // If we already have file text, no need to reopen the file

        new FileOpenerTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    class FileOpenerTask extends AsyncTask<Void, String, String> {
        long startTime;
        long endTime;

        @Override
        protected void onPreExecute(){
            super.onPreExecute();

            startTime = Calendar.getInstance().getTimeInMillis();
        }

        @Override
        protected String doInBackground(Void... params) {


            // Open the file and read each lines
            try{

                BufferedReader br = new BufferedReader(new FileReader(displayedFile));
                StringBuilder stringBuilder = new StringBuilder();
                String line = null;

                while ((line = br.readLine()) != null){
                    line = line + "<br \\>";
                    stringBuilder.append(line);
                }

                br.close();

                return stringBuilder.toString();
            } catch(IOException e){
                Log.d("error",e.getMessage());
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(String... progress){

        }

        @Override
        protected void onPostExecute(String result){
            webView.loadData(HTML_OPENING + result + HTML_CLOSING, "text/html; charset=utf-8", null);
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

        displayFileText();
        this.displayedFile = displayedFile;
    }
}
