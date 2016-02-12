package com.example.blah.mobilestudio.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.blah.mobilestudio.AndroidMonitor.AndroidMonitorFragment;
import com.example.blah.mobilestudio.FileViewer.FileFragment;
import com.example.blah.mobilestudio.R;
import com.example.blah.mobilestudio.Resizer.ResizerFragment;
import com.example.blah.mobilestudio.Resizer.ResizerRunnable;
import com.example.blah.mobilestudio.breadcrumbview.BreadcrumbFragment;
import com.example.blah.mobilestudio.breadcrumbview.OnItemSelectedListener;
import com.example.blah.mobilestudio.fileTreeView.FolderStructureFragment;

import net.rdrei.android.dirchooser.DirectoryChooserActivity;
import net.rdrei.android.dirchooser.DirectoryChooserConfig;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends SafeIntermediateActivity implements FolderStructureFragment.OnFileSelectedListener, OnItemSelectedListener {

    public static final int PICK_DIRECTORY_REQUEST = 1;
    public static final int WRITE_EXTERNAL_STORAGE_PERMISSION_REQUEST = 2;
    // The smallest/largest amount of pixels that a view can be resized to.
    public static final float REGION_OFFSET = 100f;

    // Fragments
    private FolderStructureFragment folderStructureFragment;
    private FileFragment fileFragment;
    private ResizerFragment horizontalResizerFragment;
    private ResizerFragment veritcalResizerFragment;
    private AndroidMonitorFragment androidMonitorFragment;
    private BreadcrumbFragment breadFragment;

    private boolean isLandscape;

    @Override
    protected void onActivityCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
        setUpUI();
        handlePermissions();
    }

    private void setUpUI() {
        initialiseOrientation();
        initialiseToolbar();
        initFragments();
        if (isLandscape) {
            initFragmentsForLandscapeMode();
        }
    }

    private void initialiseToolbar() {
        // Only the Open Icon, the up icon and the save icon are visible the entire time
        // All of the other icons are visible if there is room.
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);
        setSupportActionBar(toolbar);
    }

    private void initialiseOrientation() {
        // Lock the layout, based on whether the device is a phone or a tablet
        boolean isTablet = getResources().getBoolean(R.bool.isTablet);
        if (isTablet) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        isLandscape = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
    }

    private void initFragments() {
        String filePath = Environment.getExternalStorageDirectory().getAbsolutePath();

        initFolderFragment(filePath);
        initHBreadcrumbFragment(filePath);
        initMonitorFragment();
        initVerticalResizerFragment();
    }

    private void initMonitorFragment() {
        androidMonitorFragment = new AndroidMonitorFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.android_monitor, androidMonitorFragment)
                .commit();
    }

    private void initFragmentsForLandscapeMode() {
        initHorizontalResizerFragment();
        fileFragment = new FileFragment();

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_fragment, fileFragment)
                .commit();
    }

    private void initFolderFragment(String filePath) {
        folderStructureFragment = new FolderStructureFragment();
        Bundle bundle = new Bundle();
        bundle.putString(FolderStructureFragment.FILE_PATH, filePath);
        folderStructureFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.explorer_fragment, folderStructureFragment).commitAllowingStateLoss();
    }

    @Override
    public void onFileSelected(String path) {
        Log.d("file-selected", path);
        File file = new File(path);
        if (!file.isDirectory()) {
            // If in portrait mode, start a new activity, displaying the file
            if (!isLandscape) {
                Intent i = new Intent(this, DisplayFileActivity.class);
                i.putExtra(DisplayFileActivity.FILE_NAME, path);
                startActivity(i);
            } else {
                fileFragment.setDisplayedFile(new File(path));
            }

        }
        // Change the breadcrumb to reflect the new file selected/
        breadFragment = (BreadcrumbFragment) getSupportFragmentManager().findFragmentById(R.id.topBreadFragment);
        if (breadFragment != null) {
            breadFragment.currentPath = path;
            this.highlightBreadcrumbItem(path);
            breadFragment.onResume();
            breadFragment.calculatePathAndSetTheListener(breadFragment.breadCrumb, path);
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
    public void onBreadItemSelected(String rootPath, String path) {
        Log.d("brdcrumb item selected", path);
        this.highlightFIle(rootPath + path);
        this.highlightBreadcrumbItem(path);
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

                startActivityForResult(chooserIntent, PICK_DIRECTORY_REQUEST);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void handlePermissions() {
        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    WRITE_EXTERNAL_STORAGE_PERMISSION_REQUEST);
        }
    }

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case PICK_DIRECTORY_REQUEST:
                if (resultCode == DirectoryChooserActivity.RESULT_CODE_DIR_SELECTED) {
                    String rootFolder = data.getStringExtra(DirectoryChooserActivity.RESULT_SELECTED_DIR);
                    Log.d("root", rootFolder);
                    openFile(rootFolder);
                    createBreadcrumb(rootFolder);
                }
        }
    }

    private void createBreadcrumb(String rootFolder) {
        if (breadFragment != null) {
            breadFragment.currentPath = rootFolder;
            breadFragment.breadCrumb.setRootPath("");
            breadFragment.setOnClickListener(this);
            breadFragment.calculatePathAndSetTheListener(breadFragment.breadCrumb, breadFragment.currentPath);
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
        FolderStructureFragment folderStructureFragment = (FolderStructureFragment)
                getSupportFragmentManager().findFragmentById(R.id.explorer_fragment);

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
            getSupportFragmentManager().beginTransaction().replace(R.id.explorer_fragment,
                    folderStructureFragment).commitAllowingStateLoss();
        }
    }

    private void highlightFIle(String filePath) {
        FolderStructureFragment folderFragment = (FolderStructureFragment)
                getSupportFragmentManager().findFragmentById(R.id.explorer_fragment);
        folderFragment.highlightFile(filePath);
    }

    private void initHBreadcrumbFragment(String filePath) {
        breadFragment = (BreadcrumbFragment) getSupportFragmentManager().findFragmentById(R.id.topBreadFragment);
        breadFragment.currentPath = filePath;

        breadFragment.setOnClickListener(this);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.topBreadFragment, breadFragment).commitAllowingStateLoss();

    }

    private void highlightBreadcrumbItem(String path) {
        ArrayList<String> items = new ArrayList(Arrays.asList(path.substring(1).split("\\s*/\\s*")));
        breadFragment = (BreadcrumbFragment) getSupportFragmentManager().findFragmentById(R.id.topBreadFragment);
        breadFragment.highlightSelectedItem(items.get(items.size() - 1), items.size() - 1);
    }

    private void initVerticalResizerFragment() {
        veritcalResizerFragment = new ResizerFragment();
        Bundle veritcalResizerBundle = new Bundle();
        veritcalResizerBundle.putInt(ResizerFragment.FIRST_VIEW_BUNDLE_KEY, R.id.top_layout);
        veritcalResizerBundle.putInt(ResizerFragment.THIRD_VIEW_BUNDLE_KEY, R.id.android_monitor_outer_layout);
        veritcalResizerBundle.putBoolean(ResizerFragment.IS_HORIZONTAL, false);
        veritcalResizerFragment.setArguments(veritcalResizerBundle);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.top_monitor_resizer_vertical_fragment, veritcalResizerFragment)
                .commit();
    }

    private void initHorizontalResizerFragment() {
        horizontalResizerFragment = new ResizerFragment();
        Bundle horizontalResizerBundle = new Bundle();
        horizontalResizerBundle.putInt(ResizerFragment.FIRST_VIEW_BUNDLE_KEY, R.id.explorer_fragment);
        horizontalResizerBundle.putInt(ResizerFragment.THIRD_VIEW_BUNDLE_KEY, R.id.content_fragment);
        horizontalResizerBundle.putBoolean(ResizerFragment.IS_HORIZONTAL, true);
        horizontalResizerFragment.setArguments(horizontalResizerBundle);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.explorer_content_resizer_horizontal_fragment, horizontalResizerFragment)
                .commit();
    }
}
