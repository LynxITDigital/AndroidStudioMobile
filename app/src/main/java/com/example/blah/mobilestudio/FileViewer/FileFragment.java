package com.example.blah.mobilestudio.FileViewer;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.blah.mobilestudio.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

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

    private static final int LINES_PER_CELL = 50;
    private ListView fileList;
    private ArrayList<String> fileContents;
    private ArrayAdapter<String> listAdapter;

    // TODO save file Contents in on save state
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.content_layout, container, false);
        fileList = (ListView) rootView.findViewById(R.id.file_list);
        fileContents = new ArrayList<String>();
        listAdapter = new ArrayAdapter<String>(getContext(), R.layout.file_text_cell ,fileContents);
        fileList.setAdapter(listAdapter);
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
            fileContents.add(DEFAULT_TEXT);
            listAdapter.notifyDataSetChanged();
            return;
        }

        // If we already have file text, no need to reopen the file

        new FileOpenerTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    class FileOpenerTask extends AsyncTask<Void, String, Void> {
        long startTime;
        long endTime;

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            fileContents.clear();
            listAdapter.notifyDataSetChanged();
            startTime = Calendar.getInstance().getTimeInMillis();
        }

        @Override
        protected Void doInBackground(Void[] params) {


            // Open the file and read each lines
            try{

                BufferedReader br = new BufferedReader(new FileReader(displayedFile));
                StringBuilder stringBuilder = new StringBuilder();
                StringBuilder nextCellData = new StringBuilder();
                int lineNumber = 0;
                String line = null;
                // Load fifty lines at a time, then add them to the list
                while ((line = br.readLine()) != null){
                    line = line + System.getProperty("line.separator");
                    stringBuilder.append(line);
                    nextCellData.append(line);
                    // Add the lines to a new cell in the list
                    if((lineNumber % LINES_PER_CELL) == 0){
                        publishProgress(new String(nextCellData.toString()));
                        nextCellData = new StringBuilder();
                    }

                    lineNumber++;
                }
                publishProgress(nextCellData.toString());
                br.close();


            } catch(IOException e){
                Log.d("error",e.getMessage());
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(String... progress){
            fileContents.add(progress[0]);
            listAdapter.notifyDataSetChanged();

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
        fileContents.clear();
        listAdapter.notifyDataSetChanged();
        displayFileText();
        this.displayedFile = displayedFile;
    }
}
