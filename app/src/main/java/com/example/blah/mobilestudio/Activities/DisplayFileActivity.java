package com.example.blah.mobilestudio.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;

import com.example.blah.mobilestudio.FileViewer.FileFragment;
import com.example.blah.mobilestudio.R;

import java.io.File;

/**
 * Created by Stephen on 8/02/2016.
 */
public class DisplayFileActivity extends FragmentActivity {
    public static final String FILE_NAME = "file";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_file);

        FileFragment fileFragment = new FileFragment(new File(getIntent().getStringExtra(FILE_NAME)));
        getSupportFragmentManager().beginTransaction().add(R.id.display_file_placeholder, fileFragment).commit();

    }
}
