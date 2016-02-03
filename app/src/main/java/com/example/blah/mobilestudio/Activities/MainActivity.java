package com.example.blah.mobilestudio.Activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.blah.mobilestudio.AndroidMonitor.AndroidMonitorFragment;
import com.example.blah.mobilestudio.FileViewer.FileFragment;
import com.example.blah.mobilestudio.FolderStructureFragment;
import com.example.blah.mobilestudio.R;
import com.example.blah.mobilestudio.Resizer.HorizontalResizerFragment;
import com.example.blah.mobilestudio.breadcrumbview.BreadcrumbFragment;
import com.example.blah.mobilestudio.breadcrumbview.OnItemSelectedListener;

import net.rdrei.android.dirchooser.DirectoryChooserActivity;
import net.rdrei.android.dirchooser.DirectoryChooserConfig;

import java.io.File;

public class MainActivity extends AppCompatActivity implements FolderStructureFragment.OnFileSelectedListener, OnItemSelectedListener {

    public static final String TAG = MainActivity.class.getSimpleName();

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
    Fragment androidMonitor;
    private HorizontalResizerFragment horizontalResizerFragment;
    private AndroidMonitorFragment androidMonitorFragment;
    private BreadcrumbFragment breadFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set up the explorer fragment
        String filePath = Environment.getExternalStorageDirectory().getAbsolutePath();
        folderStructureFragment = new FolderStructureFragment();
        Bundle bundle = new Bundle();
        bundle.putString(FolderStructureFragment.FILE_PATH, filePath);
        folderStructureFragment.setOnClickListener(this);
        folderStructureFragment.setArguments(bundle);

        // Set up the toolbar icons
        // Only the Open Icon, the up icon and the save icon are visible the entire time
        // All of the other icons are visible if there is room.
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);
        setSupportActionBar(toolbar);

        // Set up the file fragment
        fileFragment = new FileFragment();
        horizontalResizerFragment = new HorizontalResizerFragment();
        androidMonitorFragment = new AndroidMonitorFragment();
        //fileFragment.setArguments(getIntent().getExtras());
        getSupportFragmentManager().beginTransaction()
                .add(R.id.explorer_content_resizer_fragment, horizontalResizerFragment)
                .add(R.id.content_fragment, fileFragment)
                .add(R.id.android_monitor, androidMonitorFragment)
                .commit();

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

            getSupportFragmentManager().beginTransaction().add(R.id.explorer_fragment, folderStructureFragment).commit();
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

        getSupportFragmentManager().beginTransaction().replace(R.id.explorer_fragment, folderStructureFragment).commit();
    }

    private void highlightFIle(String filePath) {
        FolderStructureFragment folderFragment = (FolderStructureFragment) getSupportFragmentManager().findFragmentById(R.id.explorer_fragment);
        folderFragment.highlightFile(filePath);
    }

    private void resetBreadcrumb(String filePath) {
        //Horizontal Breadcrumb reset
        breadFragment = (BreadcrumbFragment) getFragmentManager().findFragmentById(R.id.topBreadFragment);
        breadFragment.currentPath = filePath;
        breadFragment.setOnClickListener(this);
        getFragmentManager().beginTransaction().replace(R.id.topBreadFragment, breadFragment).commit();

    }


    public FileFragment getFileFragment() {
        return fileFragment;
    }

    public FolderStructureFragment getFolderStructureFragment() {
        return folderStructureFragment;
    }

    @Override
    protected void onResume() {
        super.onResume();

        View outerLayout = findViewById(R.id.screen_layout).getRootView();
        outerLayout.post(new Runnable() {

            LinearLayout outerLayout;

            View heirarchyView;
            View resizerView;
            View contentView;

            LinearLayout androidMonitorLayout;
            RelativeLayout topLayout;
            
            @Override
            public void run() {

                outerLayout = (LinearLayout) findViewById(R.id.screen_layout);

                heirarchyView = folderStructureFragment.getView();
                resizerView = horizontalResizerFragment.getView();
                contentView = fileFragment.getView();

                topLayout = (RelativeLayout) findViewById(R.id.top_layout);
                androidMonitorLayout = (LinearLayout) findViewById(R.id.android_monitor_outer_layout);

                widthChanges();
                heightChanges();

                //layout views again
                outerLayout.requestLayout();
            }

            void widthChanges() {

                //known information - the outerlayout is the width of the screen.
                float thirdWidth = ((float) outerLayout.getWidth()) / 3f;
//                Log.d(TAG, "widthChanges: outerLayout width " + outerLayout.getWidth());
//                Log.d(TAG, "widthChanges: thirdWidth " + thirdWidth);

                //known information - the resizerView is the width of the resizer view.
                float halfOfResizerView = ((float) resizerView.getWidth()) / 2f;
//                Log.d(TAG, "widthChanges: resizer view width " + resizerView.getWidth());
//                Log.d(TAG, "widthChanges: half of resizer view " + halfOfResizerView);

                //change the width of the hierarchy view to be before the resizer view
                // and change the x position of the resizerview to be after the width of the hierarchy view
                int newBeginningOfResizer = Math.round(thirdWidth - halfOfResizerView);
//                Log.d(TAG, "widthChanges: new beginning of resizer " + newBeginningOfResizer);

                heirarchyView.setX(0f);
//                Log.d(TAG, "widthChanges: heirarchyView.getX() " + heirarchyView.getX());

                heirarchyView.getLayoutParams().width = newBeginningOfResizer;
//                Log.d(TAG, "widthChanges: heirarchyView.getLayoutParams().width " + heirarchyView.getLayoutParams().width);

                resizerView.setX(newBeginningOfResizer);
//                Log.d(TAG, "widthChanges: resizerView.getX() " + resizerView.getX());

                //leave the resizer views width.
                //set the content view to be after the resizer view.
                //set the width of the content view to be the space that's left.
                contentView.setX(newBeginningOfResizer + halfOfResizerView);
//                Log.d(TAG, "widthChanges: contentview x " + contentView.getX());

                contentView.getLayoutParams().width = Math.round((thirdWidth * 2) - halfOfResizerView);
//                Log.d(TAG, "widthChanges: contentView.getLayoutParams().width " + contentView.getLayoutParams().width);
            }

            void heightChanges() {
                //known information - the outerlayout is the width of the screen.
                float thirdHeight = ((float) outerLayout.getHeight()) / 3f;
                Log.d(TAG, "heightChanges: outerLayout.getHeight() " + outerLayout.getHeight());
                Log.d(TAG, "heightChanges: third height " + thirdHeight);

                //change the height and y position of the top_layout to be 2/3rds of the screen.
                int newBeginningOfAndroidMonitor = Math.round(thirdHeight);
                Log.d(TAG, "heightChanges: newBeginningOfAndroidMonitor " + newBeginningOfAndroidMonitor);

                topLayout.setY(0f);
                Log.d(TAG, "heightChanges: toplayout y " + topLayout.getY());
                topLayout.getLayoutParams().height = newBeginningOfAndroidMonitor * 2;
                Log.d(TAG, "heightChanges: topLayout.getLayoutParams().height " + topLayout.getLayoutParams().height);

                //change the android monitor layout to have a y position below the top layout
                // and change it to have a height of 1/3rd of the screen.s
                androidMonitorLayout.setY(newBeginningOfAndroidMonitor * 2);
                Log.d(TAG, "heightChanges: androidMonitorLayout.getY " + androidMonitorLayout.getY());
                androidMonitorLayout.getLayoutParams().height = newBeginningOfAndroidMonitor;
                Log.d(TAG, "heightChanges: androidMonitorLayout.getLayoutParams().height " + androidMonitorLayout.getLayoutParams().height);

            }
        });
    }
}
