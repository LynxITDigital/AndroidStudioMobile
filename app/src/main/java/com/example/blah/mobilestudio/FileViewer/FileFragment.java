package com.example.blah.mobilestudio.FileViewer;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.blah.mobilestudio.R;

import java.io.File;

/**
 * Created by Stephen on 27/01/2016.
 */
public class FileFragment extends Fragment {

    // The file to be displayed
    private File displayedFile;
    private TextView fileText;

    private static String  DEFAULT_TEXT = "No files are open"  + "\n" + "Select a file from the explorer";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fileText = (TextView) container.findViewById(R.id.content_text);
        return inflater.inflate(R.layout.content_layout, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

        displayFileText();
    }
    /**
     * Changes the contents in the text view to the contents of the file.
     */
    private void displayFileText(){
        if(fileText == null){
            fileText.setText(DEFAULT_TEXT);
        }
    }

    public File getDisplayedFile() {
        return displayedFile;
    }

    public void setDisplayedFile(File displayedFile) {
        this.displayedFile = displayedFile;
    }
}
