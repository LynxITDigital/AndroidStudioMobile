package com.example.blah.mobilestudio.FileViewer;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.blah.mobilestudio.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

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
        fileContents = savedInstanceState.getString(FILE_CONTENTS);
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

        // Open the file and read the lines
        try{
            BufferedReader br = new BufferedReader(new FileReader(displayedFile));
            String line;
            StringBuilder stringBuilder = new StringBuilder();

            while ((line = br.readLine()) != null){
                stringBuilder.append(line);
            }

            br.close();
            fileContents = stringBuilder.toString();
        } catch(IOException e){
            fileText.setText(ERROR_TEXT + e.getMessage());
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
        this.displayedFile = displayedFile;
        fileContents = null;
        displayFileText();
    }
}
