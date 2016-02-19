package com.example.blah.mobilestudio.FileViewer;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.GestureDetectorCompat;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;

import com.example.blah.mobilestudio.R;
import com.example.blah.mobilestudio.activities.DisplayFileActivity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Calendar;

/**
 * Created by Stephen on 27/01/2016.
 * The file fragment is only concerned with displaying the file handed to it.
 * No other logic is performed in this class.
 */
public class FileFragment extends Fragment {

    // The file to be displayed
    private File displayedFile;
    private static String DEFAULT_TEXT = "No files are open" +
            System.getProperty("line.separator")
            + "Select a file to open from the explorer";
    private static String ERROR_TEXT = "Unable to display file: ";
    public static String FILE_CONTENTS = "File contents";
    private static String HTML_OPENING = "<html><body><p>";
    private static String HTML_CLOSING = "</p></html></body>";
    private static String[] IMAGE_FORMATS = {"jpeg", "png", "bmp", "webp", "jpg"};

    private WebView webView;
    private ImageView imageView;
    private GestureDetectorCompat gestureDetector;
    private boolean isFullscreen;

    public FileFragment() {
        isFullscreen = false;
    }

    @SuppressLint("ValidFragment")
    public FileFragment(File file) {
        displayedFile = file;
        isFullscreen = false;
    }

    @SuppressLint("ValidFragment")
    public FileFragment(File file, boolean isFullscreen){
        displayedFile = file;
        this.isFullscreen = isFullscreen;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.content_layout, container, false);
        webView = (WebView) rootView.findViewById(R.id.web_layout);
        imageView = (ImageView) rootView.findViewById(R.id.image_layout);
        gestureDetector = new GestureDetectorCompat(getContext(), new DoubleTapListener());
        View.OnTouchListener listener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
            }
        };
        // The webview and image view should listen for double taps
        webView.setOnTouchListener(listener);
        imageView.setOnTouchListener(listener);
        displayFileText();

        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    class DoubleTapListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onDoubleTap(MotionEvent event){
            if(getResources().getBoolean(R.bool.isTablet)){
                if(isFullscreen){
                    getActivity().getSupportFragmentManager().popBackStack();
                } else {
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .add(R.id.screen_layout, (new FileFragment(getDisplayedFile(), true)))
                            .addToBackStack(FILE_CONTENTS)
                            .commit();
                }
                return true;
            }
            return false;
        }

        @Override
        public boolean onDown(MotionEvent e){
            Log.d("d", "on down");
            return true;
        }
    }

    /**
     * Changes the contents in the text view to the contents of the file.
     */
    private void displayFileText() {
        if (displayedFile == null) {
            loadWebView(DEFAULT_TEXT);
            return;
        }

        // Check if the file is an image by checking if the extension is jpeg or png
        String[] fileNameComponents = displayedFile.getAbsolutePath().split("\\.");
        // Extension will be the final component after the "."
        if (fileNameComponents.length > 0) {
            String extension = fileNameComponents[fileNameComponents.length - 1];
            for (int i = 0; i < IMAGE_FORMATS.length; i++) {
                if (extension.equals(IMAGE_FORMATS[i])) {
                    loadImageFile();
                    return;
                }
            }
        }

        new FileOpenerTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    class FileOpenerTask extends AsyncTask<Void, String, String> {
        long startTime;
        long endTime;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            startTime = Calendar.getInstance().getTimeInMillis();
        }

        @Override
        protected String doInBackground(Void... params) {


            // Open the file and read each lines
            try {
                BufferedReader br = new BufferedReader(new FileReader(displayedFile));
                StringBuilder stringBuilder = new StringBuilder();
                String line = null;

                while ((line = br.readLine()) != null) {
                    line = line + System.getProperty("line.separator");
                    stringBuilder.append(line);

                }
                br.close();
                String result = stringBuilder.toString();
                JavaTextFormatter formatter = new JavaTextFormatter();
                result = formatter.highlightSourceCode(result);

                return result;
            } catch (IOException e) {
                Log.d("error", e.getMessage());
            }
            return null;
        }


        @Override
        protected void onPostExecute(String result) {
            if (result == null) {
                loadWebView(ERROR_TEXT);
            } else {
                loadWebView(result);
            }

            endTime = Calendar.getInstance().getTimeInMillis();
            Log.d("time taken", String.valueOf(endTime - startTime));
        }

    }

    // Loads an image file into the view
    // Assumes displayed file is an image
    private void loadImageFile() {
        // clear the webview if there is one
        webView.setVisibility(View.GONE);
        imageView.setVisibility(View.VISIBLE);
        // set the image file bitmap
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        Bitmap imageBitmap = BitmapFactory.decodeFile(displayedFile.getAbsolutePath(), options);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            options.inSampleSize = calculateInSampleSize(options, 200, 200);
        }

        options.inJustDecodeBounds = false;
        imageBitmap = BitmapFactory.decodeFile(displayedFile.getAbsolutePath(), options);

        imageView.setImageBitmap(imageBitmap);
    }

    // Calculates the number of times the image's height will have to be halved before it is
    // below the required height and width.
    // The options will use this to calculate the new bitmap.
    private int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of the image
        int height = options.outHeight;
        int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            int halfHeight = height / 2;
            int halfWidth = width / 2;

            // Calculate the largest inSampleSize that has a power of 2 and keeps both and and
            // width larger than the requested height and width
            while ((halfHeight / inSampleSize) > reqHeight && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }

        }
        return inSampleSize;

    }

    // Loads a webview from an input string
    private void loadWebView(String input) {
        // clear the image view if there is one
        imageView.setVisibility(View.GONE);
        webView.setVisibility(View.VISIBLE);
        // Set the webview to display the input string
        webView.loadData(HTML_OPENING + input + HTML_CLOSING, "text/html; charset=utf-8", null);
    }

    public File getDisplayedFile() {
        return displayedFile;
    }

    /**
     * @param displayedFile - the file to be displayed to the user. The file will immediately be
     *                      opened in the FileFragment.
     */
    public void setDisplayedFile(File displayedFile) {
        this.displayedFile = displayedFile;
        displayFileText();
    }
}
