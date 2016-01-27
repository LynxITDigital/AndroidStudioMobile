package com.example.blah.mobilestudio.Activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.blah.mobilestudio.FolderStructureFragment;
import com.example.blah.mobilestudio.R;

import net.rdrei.android.dirchooser.DirectoryChooserActivity;
import net.rdrei.android.dirchooser.DirectoryChooserConfig;

public class MainActivity extends AppCompatActivity implements FolderStructureFragment.OnFileSelectedListener {

    // Value for the fragments to call when they need the root folder string
    public static final String ROOT_FOLDER = "root folder";
    // Value for fragments to call when they need the current folder string
    public static final String CURRENT_DIRECTORY = "current directory";

    public static final int PICK_DIRECTORY_REQUEST = 1;
    public static final int WRITE_EXTERNAL_STORAGE_PERMISSION_REQUEST = 2;

    private String currentFolder;
    private String rootFolder;

    Toolbar toolbar;

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

        // Set up the toolbar icons
        // Only the Open Icon, the up icon and the save icon are visible the entire time
        // All of the other icons are visible if there is room.
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);
        setSupportActionBar(toolbar);

        // Initialise the root and current folder values
        if(rootFolder == null){
            rootFolder = "";
        }

        if(currentFolder == null){
            currentFolder = "";
        }

        // Set up the necessary permissions for the app
        handlePermissions();
    }

    @Override
    public void onFileSelected(String path) {
        Log.d("file-selected", path);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case R.id.action_open_folder:
                Intent chooserIntent = new Intent(this, DirectoryChooserActivity.class);

                DirectoryChooserConfig config = DirectoryChooserConfig.builder()
                        .initialDirectory("/")
                        .newDirectoryName("DirChooserSample")
                        .allowReadOnlyDirectory(true)
                        .allowNewDirectoryNameModification(true)
                        .build();

                chooserIntent.putExtra(DirectoryChooserActivity.EXTRA_CONFIG, config);

                // REQUEST_DIRECTORY is a constant integer to identify the request, e.g. 0
                startActivityForResult(chooserIntent, PICK_DIRECTORY_REQUEST);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // Check if the permission is not currently granted. If not, request it
    private void handlePermissions() {
        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    WRITE_EXTERNAL_STORAGE_PERMISSION_REQUEST);
        }
    }

    // TODO Finish this method
    /**
     * Called whenever the path value has changed. Notifies all fragments that need to
     * know about the change.
     */
    public void rootFolderChanged(){

    }

    // How the activity responds after permissions have been granted or denied.
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case WRITE_EXTERNAL_STORAGE_PERMISSION_REQUEST: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission granted

                } else {
                    // Permission denied
                    // Cannot select empty directories
                }
                return;
            }

        }
    }

    // How the app responds after the directory chooser activity has been launched and returned.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode){
            case PICK_DIRECTORY_REQUEST:
                if(resultCode == DirectoryChooserActivity.RESULT_CODE_DIR_SELECTED) {
                    rootFolder = data.getStringExtra(DirectoryChooserActivity.RESULT_SELECTED_DIR);
                    Log.d("root", rootFolder);
                }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar, menu);

        return super.onCreateOptionsMenu(menu);
    }
}