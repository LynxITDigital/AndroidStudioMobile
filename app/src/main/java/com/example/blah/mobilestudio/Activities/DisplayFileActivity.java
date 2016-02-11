package com.example.blah.mobilestudio.activities;

import android.os.Bundle;

import com.example.blah.mobilestudio.FileViewer.FileFragment;
import com.example.blah.mobilestudio.R;

import java.io.File;

/**
 * Created by Stephen on 8/02/2016.
 */
public class DisplayFileActivity extends SafeActivity {
    public static final String FILE_NAME = "file";

    @Override
    protected void onActivityCreate(Bundle savedInstanceState) {
        setContentView(R.layout.display_file);

        FileFragment fileFragment = new FileFragment(new File(getIntent().getStringExtra(FILE_NAME)));
        getSupportFragmentManager().beginTransaction().add(R.id.display_file_placeholder, fileFragment).commit();

    }
}
