package com.example.blah.mobilestudio.Activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.blah.mobilestudio.AndroidMonitor.AndroidMonitorFragment;
import com.example.blah.mobilestudio.FileViewer.FileFragment;
import com.example.blah.mobilestudio.R;
import com.example.blah.mobilestudio.Resizer.ResizerFragment;
import com.example.blah.mobilestudio.Resizer.SizableRegion;
import com.example.blah.mobilestudio.breadcrumbview.BreadcrumbFragment;
import com.example.blah.mobilestudio.breadcrumbview.OnItemSelectedListener;
import com.example.blah.mobilestudio.fileTreeView.FolderStructureFragment;

import net.rdrei.android.dirchooser.DirectoryChooserActivity;
import net.rdrei.android.dirchooser.DirectoryChooserConfig;

import java.io.File;

public class MainActivity extends AppCompatActivity implements FolderStructureFragment.OnFileSelectedListener, OnItemSelectedListener {

    public static final String TAG = MainActivity.class.getSimpleName();

    public static final int PICK_DIRECTORY_REQUEST = 1;
    public static final int WRITE_EXTERNAL_STORAGE_PERMISSION_REQUEST = 2;
    // The smallest/largest amount of pixels that a view can be resized to.
    private static final float REGION_OFFSET = 100f;

    private String currentFolder;
    private String rootFolder;

    // The various UI elements available
    Toolbar toolbar;

    FolderStructureFragment folderStructureFragment;
    FileFragment fileFragment;
    private ResizerFragment horizontalResizerFragment;;
    private ResizerFragment veritcalResizerFragment;
    private AndroidMonitorFragment androidMonitorFragment;
    private BreadcrumbFragment breadFragment;

    // The orientation of the screen
    private boolean isLandscape;

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

        // Set up the file fragment
        fileFragment = new FileFragment();

        androidMonitorFragment = new AndroidMonitorFragment();

        folderStructureFragment = new FolderStructureFragment();
        Bundle bundle = new Bundle();
        String filePath = Environment.getExternalStorageDirectory().getAbsolutePath();
        bundle.putString(FolderStructureFragment.FILE_PATH, filePath);
        folderStructureFragment.setArguments(bundle);

        resetBreadcrumb(filePath);

        isLandscape = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
        setResizers();
        //fileFragment.setArguments(getIntent().getExtras());
        // Fragments common to both kinds of layouts
        getSupportFragmentManager().beginTransaction()
                .add(R.id.top_monitor_resizer_vertical_fragment, veritcalResizerFragment)
                .add(R.id.explorer_fragment, folderStructureFragment)
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
    }

    @Override
    public void onFileSelected(String path) {
        Log.d("file-selected", path);
        File file = new File(path);
        if (!file.isDirectory()) {
            // If in portrait mode, start a new activity, displaying the file
            if(!isLandscape){
                Intent i = new Intent(this, DisplayFileActivity.class);
                i.putExtra(DisplayFileActivity.FILE_NAME, path);
                startActivity(i);
            } else {
                fileFragment.setDisplayedFile(new File(path));
            }

        }

        if (breadFragment != null) {
            breadFragment.currentPath = path;
            breadFragment.onResume();
        }
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
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

    /**
     * Call commitAllowingStateLoss() instead of commit().
     * Please refer to http://stackoverflow.com/questions/7575921/illegalstateexception
     * -can-not-perform-this-action-after-onsaveinstancestate-wit
     */
    private void openFile(String filePath) {
        FolderStructureFragment folderStructureFragment = (FolderStructureFragment) getSupportFragmentManager().findFragmentById(R.id.explorer_fragment);

        if (folderStructureFragment != null) {
            Bundle b = folderStructureFragment.getArguments();
            b.putString(FolderStructureFragment.FILE_PATH, filePath);
            folderStructureFragment.setOnClickListener(this);
            getSupportFragmentManager().beginTransaction()
                    .detach(folderStructureFragment)
                    .attach(folderStructureFragment)
                    .commitAllowingStateLoss();
        } else {
            folderStructureFragment = new FolderStructureFragment();
            Bundle b = new Bundle();
            b.putString(FolderStructureFragment.FILE_PATH, filePath);
            folderStructureFragment.setOnClickListener(this);
            folderStructureFragment.setArguments(b);
            getSupportFragmentManager().beginTransaction().replace(R.id.explorer_fragment, folderStructureFragment).commitAllowingStateLoss();;
        }
    }

    private void highlightFIle(String filePath) {
        FolderStructureFragment folderFragment = (FolderStructureFragment) getSupportFragmentManager().findFragmentById(R.id.explorer_fragment);
        folderFragment.highlightFile(filePath);
    }

    private void resetBreadcrumb(String filePath) {
        //Horizontal Breadcrumb reset
        breadFragment = (BreadcrumbFragment) getSupportFragmentManager().findFragmentById(R.id.topBreadFragment);
        breadFragment.currentPath = filePath;

        breadFragment.setOnClickListener(this);
        getSupportFragmentManager()
                .beginTransaction().
                replace(R.id.topBreadFragment, breadFragment).commit();

    }

    // Sets up the layout for the main activity
    private void setResizers(){
        View outerLayout = findViewById(R.id.screen_layout).getRootView();

        // Set up the resizer fragments
        // need the horizontal fragment in landscape

        if(isLandscape){
            // Do not create the horizontal resizer fragment if landscape
            horizontalResizerFragment = new ResizerFragment();
            Bundle horizontalResizerBundle = new Bundle();
            horizontalResizerBundle.putInt(ResizerFragment.FIRST_VIEW_BUNDLE_KEY, R.id.explorer_fragment);
            horizontalResizerBundle.putInt(ResizerFragment.THIRD_VIEW_BUNDLE_KEY, R.id.content_fragment);
            horizontalResizerBundle.putBoolean(ResizerFragment.IS_HORIZONTAL, true);
            horizontalResizerFragment.setArguments(horizontalResizerBundle);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.content_fragment, fileFragment)
                    .add(R.id.explorer_content_resizer_horizontal_fragment, horizontalResizerFragment)
                    .commit();
        }

        // Need vertical in both portrait and landscape
        veritcalResizerFragment = new ResizerFragment();
        Bundle veritcalResizerBundle = new Bundle();
        veritcalResizerBundle.putInt(ResizerFragment.FIRST_VIEW_BUNDLE_KEY, R.id.top_layout);
        veritcalResizerBundle.putInt(ResizerFragment.THIRD_VIEW_BUNDLE_KEY, R.id.android_monitor_outer_layout);
        veritcalResizerBundle.putBoolean(ResizerFragment.IS_HORIZONTAL, false);
        veritcalResizerFragment.setArguments(veritcalResizerBundle);
        getSupportFragmentManager().beginTransaction()

                .commit();



        outerLayout.post(new Runnable() {

            View outerLayout;

            View heirarchyFrameLayout;
            View horizontalResizer;
            View contentFrameLayout;

            View topLayout;
            View verticalResizer;
            View androidMonitorLayout;

            View toolbarView;

            View verticalBreadCrumbView;

            @Override
            public void run() {

                outerLayout = findViewById(R.id.screen_layout);

                heirarchyFrameLayout = findViewById(R.id.explorer_fragment);


                topLayout = findViewById(R.id.top_layout);
                verticalResizer = findViewById(R.id.top_monitor_resizer_vertical_fragment);
                androidMonitorLayout = findViewById(R.id.android_monitor_outer_layout);

                toolbarView = findViewById(R.id.toolbar);

                verticalBreadCrumbView = findViewById(R.id.verticalTabFragment);
                horizontalResizer = findViewById(R.id.explorer_content_resizer_horizontal_fragment);

                if (isLandscape) {

                    contentFrameLayout = findViewById(R.id.content_fragment);
                    widthChanges();
                    configureHorizontalResizerRegion();
                }

                heightChanges();
                configureVerticalResizerRegion();


                //layout views again
                outerLayout.requestLayout();
            }

            void widthChanges() {
                //known information
                float thirdWidth = ((float) outerLayout.getWidth()) / 3f;
                float halfOfResizerView = ((float) horizontalResizer.getWidth()) / 2f;

                //change the width of the hierarchy view to be before the resizer view
                heirarchyFrameLayout.getLayoutParams().width = Math.round(thirdWidth - halfOfResizerView);

                //leave the resizer views width.
                //set the width of the content view to be the space that's left.
                contentFrameLayout.getLayoutParams().width = Math.round((thirdWidth * 2) - halfOfResizerView);
            }

            void heightChanges() {
                //known information
                float thirdHeight = ((float) outerLayout.getHeight()) / 3f;
                float halfOfResizerView = ((float) verticalResizer.getHeight()) / 2f;

                //change the height and y position of the top_layout to be 2/3rds of the screen.
                topLayout.getLayoutParams().height = Math.round((thirdHeight * 2) - halfOfResizerView);

                //change the android monitor layout to have a y position below the top layout
                // and change it to have a height of 1/3rd of the screen.s
                androidMonitorLayout.getLayoutParams().height = Math.round(thirdHeight - halfOfResizerView);
            }

            void configureVerticalResizerRegion() {

                veritcalResizerFragment.configure(new SizableRegion(
                        toolbarView.getHeight() + REGION_OFFSET,
                        outerLayout.getHeight() - (REGION_OFFSET + horizontalResizer.getWidth())
                ));
            }

            void configureHorizontalResizerRegion() {
                horizontalResizerFragment.configure(new SizableRegion(
                        verticalBreadCrumbView.getWidth() + REGION_OFFSET,
                        outerLayout.getWidth() - (REGION_OFFSET + verticalResizer.getHeight())
                ));
            }
        });
    }


    public void setOrientation() {
        if((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
        }
    }
}
