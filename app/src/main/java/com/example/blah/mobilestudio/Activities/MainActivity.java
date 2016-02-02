package com.example.blah.mobilestudio.Activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.blah.mobilestudio.FileViewer.FileFragment;
import com.example.blah.mobilestudio.FolderStructureFragment;
import com.example.blah.mobilestudio.R;
import com.example.blah.mobilestudio.breadcrumbview.BreadcrumbFragment;
import com.example.blah.mobilestudio.breadcrumbview.OnItemSelectedListener;

import net.rdrei.android.dirchooser.DirectoryChooserActivity;
import net.rdrei.android.dirchooser.DirectoryChooserConfig;

import java.io.File;

public class MainActivity extends AppCompatActivity implements FolderStructureFragment.OnFileSelectedListener, OnItemSelectedListener {

    // Value for the fragments to call when they need the root folder string
    public static final String ROOT_FOLDER = "root folder";
    // Value for fragments to call when they need the current folder string
    public static final String CURRENT_DIRECTORY = "current directory";

    public static final int PICK_DIRECTORY_REQUEST = 1;
    public static final int WRITE_EXTERNAL_STORAGE_PERMISSION_REQUEST = 2;

    private String currentFolder;
    private String rootFolder;

    // The various UI elements available
    Toolbar toolbar;
    FolderStructureFragment folderStructureFragment;
    FileFragment fileFragment;
    private BreadcrumbFragment breadFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Set up the toolbar icons
        // Only the Open Icon, the up icon and the save icon are visible the entire time
        // All of the other icons are visible if there is room.
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);
        setSupportActionBar(toolbar);


        // Initialise the root and current folder values
        if (rootFolder == null) {
            rootFolder = "";
        }

        if (currentFolder == null) {
            currentFolder = "";
        }

        // Set up the necessary permissions for the app
        handlePermissions();

        if (findViewById(R.id.explorer_fragment) != null) {
            if (savedInstanceState != null) {
                return;
            }
            // Set up the explorer fragment
            String filePath = Environment.getExternalStorageDirectory().getAbsolutePath();
            folderStructureFragment = new FolderStructureFragment();
            Bundle bundle = new Bundle();
            bundle.putString(FolderStructureFragment.FILE_PATH, filePath);
            folderStructureFragment.setArguments(bundle);

            getFragmentManager().beginTransaction().replace(R.id.explorer_fragment, folderStructureFragment).commit();
            resetBreadcrumb(filePath);
        }
    }

    @Override
    public void onFileSelected(String path) {
        Log.d("file-selected", path);
        File file = new File(path);
        if (!file.isDirectory()) {
            fileFragment = new FileFragment();
            Bundle args = new Bundle();
            args.putString(FileFragment.FILE_CONTENTS, path);
            fileFragment.setArguments(args);

            if (findViewById(R.id.content_fragment) != null) {

                getFragmentManager().beginTransaction()
                        .replace(R.id.content_fragment, fileFragment)
                        .addToBackStack(null)
                        .commit();
            } else {
                // Todo: should navigate to a new screen.
//                getFragmentManager().beginTransaction()
//                        .replace(R.id.explorer_fragment, fileFragment)
//                        .addToBackStack(null)
//                        .commit();
            }
        }

        if (breadFragment != null) {
            breadFragment.currentPath = path;
            breadFragment.onResume();
        }
    }

    @Override
    public void onBreadItemSelected(String path) {
        Log.d("brdcrumb item selected", path);
        this.highlightFIle(path);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
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
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
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
    public void rootFolderChanged() {

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
            }

        }
    }

    // How the app responds after the directory chooser activity has been launched and returned.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case PICK_DIRECTORY_REQUEST:
                if (resultCode == DirectoryChooserActivity.RESULT_CODE_DIR_SELECTED) {
                    rootFolder = data.getStringExtra(DirectoryChooserActivity.RESULT_SELECTED_DIR);
                    Log.d("root", rootFolder);
                    openFile(rootFolder);
                    resetBreadcrumb(rootFolder);
                }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void openFile(String filePath) {
        FolderStructureFragment folderStructureFragment = new FolderStructureFragment();
        Bundle b = new Bundle();
        b.putString(FolderStructureFragment.FILE_PATH, filePath);
        folderStructureFragment.setOnClickListener(this);
        folderStructureFragment.setArguments(b);

        getFragmentManager().beginTransaction().replace(R.id.explorer_fragment, folderStructureFragment).commit();
    }

    private void highlightFIle(String filePath) {
        FolderStructureFragment folderFragment = (FolderStructureFragment) getFragmentManager().findFragmentById(R.id.explorer_fragment);
        folderFragment.highlightFile(filePath);
    }

    private void resetBreadcrumb(String filePath) {
        //Horizontal Breadcrumb reset
        breadFragment = (BreadcrumbFragment) getFragmentManager().findFragmentById(R.id.topBreadFragment);
        breadFragment.currentPath = filePath;
        breadFragment.setOnClickListener(this);
        getFragmentManager().beginTransaction().replace(R.id.topBreadFragment, breadFragment).commit();

    }

}
