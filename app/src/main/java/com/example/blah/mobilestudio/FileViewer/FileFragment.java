package com.example.blah.mobilestudio.FileViewer;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.blah.mobilestudio.R;

import java.io.File;
import java.io.FileReader;

/**
 * Created by Stephen on 27/01/2016.
 * The file fragment is only concerned with displaying the file handed to it.
 * No other logic is performed in this class.
 */
public class FileFragment extends Fragment {

    // The file to be displayed
    private File displayedFile;
    private TextView fileText;

    private static String  DEFAULT_TEXT = "No files are open"  + "\n" + "Select a file from the explorer";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.content_layout, container, false);
        fileText = (TextView) rootView.findViewById(R.id.content_text);
        displayFileText();
        return rootView;
    }

    /**
     * Changes the contents in the text view to the contents of the file.
     */
    private void displayFileText(){
        if(displayedFile == null){
            fileText.setText(DEFAULT_TEXT);
            return;
        }
    }

    public File getDisplayedFile() {
        return displayedFile;
    }

    public void setDisplayedFile(File displayedFile) {
        this.displayedFile = displayedFile;
    }
}
