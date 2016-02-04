package com.example.blah.mobilestudio.breadcrumbview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.provider.CalendarContract;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import com.example.blah.mobilestudio.R;

/**
 * Created by alit on 21/01/2016.
 */
public class BreadcrumbView extends LinearLayout {
    private ArrayList<String> listOfElements;

    Paint paintColorStyle;
    String rootPath = "";
    String currentPath = "";
    String orientation = "";
    Boolean isPathable = true;
    Integer breadcrumbTextColor;
    Integer breadcrumbTextSize;
    Integer breadcrumbBgColor;
    private static int SPACER_WIDTH = 5;
    OnItemSelectedListener mListener;

    public BreadcrumbView (Context context, AttributeSet attrs){
        super(context, attrs);

        this.listOfElements= new ArrayList<String>();
        paintColorStyle = new Paint();
        TypedArray attributesValuesArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.BreadcrumbView, 0, 0);

        try {
            currentPath ="";
            isPathable = attributesValuesArray.getBoolean(R.styleable.BreadcrumbView_isPathable, true);
            orientation = attributesValuesArray.getString(R.styleable.BreadcrumbView_orientation);
            breadcrumbTextColor = attributesValuesArray.getInteger(R.styleable.BreadcrumbView_breadcrumbTextColor, 0);
            breadcrumbTextSize = attributesValuesArray.getInteger(R.styleable.BreadcrumbView_breadcrumbTextSize, 14);
            breadcrumbBgColor = attributesValuesArray.getInteger(R.styleable.BreadcrumbView_breadcrumbBgColor, 0);
        }
        finally {
            attributesValuesArray.recycle();
        }
    }

    public void setOnClickListener(OnItemSelectedListener onItemSelectedListener) {
        mListener  = onItemSelectedListener;
        final ArrayList<String> elements = this.listOfElements;

        // Iterates on the child nodes of breadcrumb
        for(int i = 0; i<this.getChildCount();i++){
            View v = this.getChildAt(i);
            if(v != null && v.getTag() != null && v.getTag().toString().startsWith("brItem")) {
                // Set a onClick listener for each view element in the breadcrumb
                v.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String relativePathToCurrentCell = "";
                        if (isPathable) {
                            // Generate a relative path string from root up to the selected element
                            for (int i = 0; i < elements.size(); i++) {
                                relativePathToCurrentCell += elements.get(i) + "/";
                                if (elements.get(i).equals(((TextView) v).getText().toString().trim()))
                                    break;
                            }

                            BreadcrumbView p = (BreadcrumbView)v.getParent();
                            for(int i = 0; i<p.getChildCount();i++) {
                                View curV = p.getChildAt(i);
                                if (curV != null && curV.getTag() != null && curV.getTag().toString().startsWith("brItem")) {
                                    // reset the visual style of all breadcrumb items to "Not Selected"
                                    curV.setBackgroundResource(R.drawable.bread_item_background);
                                    SetBackgroundResizable(curV);
                                }
                            }
                            currentPath = "/" + relativePathToCurrentCell;
                            // set the visual style of selected breadcrumb item to "Selected"
                            v.setBackgroundResource(R.drawable.bread_item_background);
                            SetBackgroundResizable(v);
                            v.getBackground().setColorFilter(Color.CYAN, PorterDuff.Mode.DARKEN);
                        }
                        if (mListener != null)
                            mListener.onBreadItemSelected(currentPath);
                    }
                });
            }
        }
    }

    /**
     * Sets the list of elements inside the Breadcrumb view and show them on UI
     * @param  elements  an ArrayList of type String which indicates the elements in the breadcrumb.
     *                   The items in the ArrayList should be sorted in the order which we want to show
     *                   them.
     * @return      void - may be needed to change to a result status code
     */
    public void SetElements(final ArrayList<String> elements)
    {
        this.listOfElements = elements;
        final LinearLayout breadRoot = this;
        breadRoot.removeAllViews();

        if(orientation.equals("horizontal")) {
            breadRoot.setOrientation(LinearLayout.HORIZONTAL);
            breadRoot.setVerticalGravity(Gravity.CENTER_VERTICAL);
        }else{
            breadRoot.setOrientation(LinearLayout.VERTICAL);
            breadRoot.setHorizontalGravity(Gravity.CENTER_HORIZONTAL);
        }

        for (int i=0;i<this.listOfElements.size();i++){
            final View tv;
            if(orientation.equals("horizontal")) {
                // initialize the breadcrumb item
                tv = new TextView(getContext());
                tv.setBackgroundResource(R.drawable.bread_item_background);
                SetBackgroundResizable(tv);
                ((TextView)tv).setTextColor(breadcrumbTextColor);
                ((TextView)tv).setTextSize(TypedValue.COMPLEX_UNIT_PT, breadcrumbTextSize);
                ((TextView)tv).setText(" " + this.listOfElements.get(i) + " ");
            }else{
                // initialize the vertical breadcrumb item
                tv = new VerticalTextView(getContext());
                ((VerticalTextView)tv).setTextColor(breadcrumbTextColor);
                ((VerticalTextView)tv).setTextSize(TypedValue.COMPLEX_UNIT_PT, breadcrumbTextSize);
                ((VerticalTextView)tv).setText(" " + this.listOfElements.get(i) + " ");

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
            if(i == this.listOfElements.size() - 1 && isPathable){
                tv.setBackgroundResource(R.drawable.bread_item_background);
                SetBackgroundResizable(tv);
                tv.getBackground().setColorFilter(Color.CYAN, PorterDuff.Mode.DARKEN);
            }

            // Add the item to the breadcrumb
            breadRoot.addView(tv);

            // Create the spacer between breadcrumb items
            if(i != this.listOfElements.size() - 1)
            {
                View spacerView = new View(getContext());
                if(orientation.equals("horizontal"))
                    spacerView.setLayoutParams(new LayoutParams(SPACER_WIDTH, LayoutParams.FILL_PARENT));
                else
                    spacerView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, SPACER_WIDTH));

                spacerView.setBackgroundColor(breadcrumbTextColor);
                breadRoot.addView(spacerView);
            }
        }

    }

    // This function allows the bacground image resizes based on the content size
    public void SetBackgroundResizable(View tv) {
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

}
