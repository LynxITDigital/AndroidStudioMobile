package com.example.blah.mobilestudio.breadcrumbview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.blah.mobilestudio.R;

import java.util.ArrayList;

/**
 * Created by alit on 21/01/2016.
 */
public class BreadcrumbView extends LinearLayout {
    private ArrayList<String> listOfElements;

    private Paint paintColorStyle;
    private String rootPath;
    private String currentPath;
    public String highlightedItem;
    public int highlightedIndex;
    private String orientation;
    private Boolean isPathable;
    private Integer breadcrumbTextColor;
    private Integer breadcrumbTextSize;
    private Integer breadcrumbBgColor;
    private static int SPACER_WIDTH = 5;
    private OnItemSelectedListener mListener;

    public BreadcrumbView(Context context, AttributeSet attrs) {
        super(context, attrs);

        // Initialize properties
        rootPath = "";
        currentPath = "";
        highlightedItem = "";
        highlightedItem = "";
        highlightedIndex = 0;
        orientation = "";
        isPathable = true;

        this.listOfElements = new ArrayList<String>();
        paintColorStyle = new Paint();
        TypedArray attributesValuesArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.BreadcrumbView, 0, 0);

        try {
            isPathable = attributesValuesArray.getBoolean(R.styleable.BreadcrumbView_isPathable, true);
            orientation = attributesValuesArray.getString(R.styleable.BreadcrumbView_orientation);
            breadcrumbTextColor = attributesValuesArray.getInteger(R.styleable.BreadcrumbView_breadcrumbTextColor, 0);
            breadcrumbTextSize = attributesValuesArray.getInteger(R.styleable.BreadcrumbView_breadcrumbTextSize, 14);
            breadcrumbBgColor = attributesValuesArray.getInteger(R.styleable.BreadcrumbView_breadcrumbBgColor, 0);
        } finally {
            attributesValuesArray.recycle();
        }
    }

    /**
     * Sets the onClick listerner for the breadcrumb items
     */
    public void setOnClickListener(OnItemSelectedListener onItemSelectedListener) {
        mListener = onItemSelectedListener;
        final ArrayList<String> elements = this.listOfElements;

        // Iterates on the child nodes of breadcrumb
        for (int i = 0; i < this.getChildCount(); i++) {
            View v = this.getChildAt(i);
            if (v != null && v.getTag() != null && v.getTag().toString().startsWith("brItem")) {
                // Set a onClick listener for each view element in the breadcrumb
                v.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String relativePathToCurrentCell = "";
                        if (isPathable) {
                            // Generate a relative path string from root up to the selected element
                            for (int i = 0; i < elements.size(); i++) {
                                relativePathToCurrentCell += elements.get(i) + "/";
                                if (elements.get(i).equals(((TextView) v).getText().toString().trim())) {
                                    //Set the highlighted item
                                    highlightedItem = ((TextView) v).getText().toString().trim();
                                    highlightedIndex = i;
                                    break;
                                }
                            }

                            BreadcrumbView p = (BreadcrumbView) v.getParent();
                            for (int i = 0; i < p.getChildCount(); i++) {
                                View curV = p.getChildAt(i);
                                if (curV != null && curV.getTag() != null && curV.getTag().toString().startsWith("brItem")) {
                                    // reset the visual style of all breadcrumb items to "Not Selected"
                                    curV.setBackgroundResource(R.drawable.bread_item_background);
                                    setBackgroundResizable(curV);
                                }
                            }
                            currentPath = "/" + relativePathToCurrentCell;
                            // set the visual style of selected breadcrumb item to "Selected"
                            v.setBackgroundResource(R.drawable.bread_item_background);
                            setBackgroundResizable(v);
                            v.getBackground().setColorFilter(Color.CYAN, PorterDuff.Mode.DARKEN);
                        }
                        if (mListener != null)
                            mListener.onBreadItemSelected(rootPath, currentPath);
                    }
                });
            }
        }
    }

    /**
     * Sets the list of elements inside the Breadcrumb view and show them on UI
     *
     * @param elements an ArrayList of type String which indicates the elements in the breadcrumb.
     *                 The items in the ArrayList should be sorted in the order which we want to show
     *                 them.
     * @return void - may be needed to change to a result status code
     */
    public void SetElements(final ArrayList<String> elements) {
        this.listOfElements = elements;
        final LinearLayout breadRoot = this;
        breadRoot.removeAllViews();

        if (orientation.equals("horizontal")) {
            breadRoot.setOrientation(LinearLayout.HORIZONTAL);
            breadRoot.setVerticalGravity(Gravity.CENTER_VERTICAL);
        } else {
            breadRoot.setOrientation(LinearLayout.VERTICAL);
            breadRoot.setHorizontalGravity(Gravity.CENTER_HORIZONTAL);
        }

        // initialize the breadcrumb item
        Boolean alreadyHighlight = false;
        for (int i = 0; i < this.listOfElements.size(); i++) {
            final View tv;
            if (orientation.equals("horizontal")) {
                // initialize the horizontal breadcrumb item
                tv = new TextView(getContext());
                tv.setBackgroundResource(R.drawable.bread_item_background);
                setBackgroundResizable(tv);
                ((TextView) tv).setTextColor(breadcrumbTextColor);
                ((TextView) tv).setTextSize(TypedValue.COMPLEX_UNIT_PT, breadcrumbTextSize);
                ((TextView) tv).setText(" " + this.listOfElements.get(i) + " ");
            } else {
                // initialize the vertical breadcrumb item
                tv = new VerticalTextView(getContext());
                ((VerticalTextView) tv).setTextColor(breadcrumbTextColor);
                ((VerticalTextView) tv).setTextSize(TypedValue.COMPLEX_UNIT_PT, breadcrumbTextSize);
                ((VerticalTextView) tv).setText(" " + this.listOfElements.get(i) + " ");

                // Set the listener for vertical items
                tv.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String selectedText = ((TextView) v).getText().toString().trim();
                        Toast.makeText(getContext(), selectedText, Toast.LENGTH_SHORT).show();
                    }
                });

            }
            tv.setTag("brItem" + String.valueOf(i));

            //Set the visual style of the current folder element as selected
            if (!alreadyHighlight && i == this.listOfElements.size() - 1 && isPathable) {
                // Selected element is the last one in breadcrumb
                tv.setBackgroundResource(R.drawable.bread_item_background);
                setBackgroundResizable(tv);
                tv.getBackground().setColorFilter(Color.CYAN, PorterDuff.Mode.DARKEN);
                highlightedItem = this.listOfElements.get(i);
                highlightedIndex = i;
            } else {
                // Selected element is not the last one in breadcrumb
                if (!alreadyHighlight && highlightedItem != "" && i == highlightedIndex) {
                    if (highlightedItem.equals(this.listOfElements.get(highlightedIndex))) {
                        tv.setBackgroundResource(R.drawable.bread_item_background);
                        setBackgroundResizable(tv);
                        tv.getBackground().setColorFilter(Color.CYAN, PorterDuff.Mode.DARKEN);
                        alreadyHighlight = true;
                    }
                }
            }

            // Add the item to the breadcrumb
            breadRoot.addView(tv);

            // Create the spacer between breadcrumb items
            if (i != this.listOfElements.size() - 1) {
                View spacerView = new View(getContext());
                if (orientation.equals("horizontal"))
                    spacerView.setLayoutParams(new LayoutParams(SPACER_WIDTH, LayoutParams.FILL_PARENT));
                else
                    spacerView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, SPACER_WIDTH));

                spacerView.setBackgroundColor(breadcrumbTextColor);
                breadRoot.addView(spacerView);
            }
        }
    }

    // This function allows the bacground image resizes based on the content size
    private void setBackgroundResizable(View tv) {
        BitmapDrawable background = (BitmapDrawable) tv.getBackground(); // assuming you have bg_tile as background.
        BitmapDrawable newBackground = new BitmapDrawable(background.getBitmap()) {
            @Override
            public int getMinimumWidth() {
                return 0;
            }

            @Override
            public int getMinimumHeight() {
                return 0;
            }
        };
        newBackground.setTileModeXY(background.getTileModeX(), background.getTileModeY());
        tv.setBackgroundDrawable(newBackground);
    }

    public void setRootPath(String p){
        rootPath = p;
    }

    public String getRootPath(){
        return rootPath;
    }

}
