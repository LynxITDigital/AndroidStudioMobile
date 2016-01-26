package com.example.blah.mobilestudio;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity implements FolderStructureFragment.OnFileSelectedListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String filePath = Environment.getExternalStorageDirectory().getAbsolutePath();
        FolderStructureFragment f = new FolderStructureFragment();
        Bundle b = new Bundle();
        b.putString(FolderStructureFragment.FILE_PATH, filePath);
        f.setOnClickListener(this);
        f.setArguments(b);

        getFragmentManager().beginTransaction().replace(R.id.fragment, f).commit();
    }

    @Override
    public void onFileSelected(String path) {
        Log.d("file-selected", path);
    }
}
