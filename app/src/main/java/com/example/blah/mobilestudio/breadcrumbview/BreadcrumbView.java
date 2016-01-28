package com.example.blah.mobilestudio.breadcrumbview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
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

    Integer vertLen = 0;
    private static int SPACER_WIDTH = 3;
    OnItemSelectedListener mListener;

    public BreadcrumbView (Context context, AttributeSet attrs){
        super(context, attrs);


        this.listOfElements= new ArrayList<String>();
        paintColorStyle = new Paint();
        TypedArray attributesValuesArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.BreadcrumbView, 0, 0);

        try {
            rootPath = "";
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

    @Override
    protected void onDraw(Canvas canvas){

    }


    public void setOnClickListener(OnItemSelectedListener onItemSelectedListener) {
        mListener  = onItemSelectedListener;
        final ArrayList<String> elements = this.listOfElements;

        for(int i = 0; i<this.getChildCount();i++){
            View v = this.getChildAt(i);
            if(v != null && v.getTag() != null && v.getTag().toString().startsWith("brItem")) {
                v.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String relativePathToCurrentCell = "";
                        if (isPathable) {

                            for (int i = 0; i < elements.size(); i++) {
                                relativePathToCurrentCell += elements.get(i) + "/";
                                if (elements.get(i).equals(((TextView) v).getText().toString().trim()))
                                    break;
                            }
                            currentPath = rootPath + "/" + relativePathToCurrentCell;
                        } else {
                            currentPath = ((TextView) v).getText().toString().trim();
                        }

                        if (mListener != null)
                            mListener.onBreadItemSelected(currentPath);

                        Log.d("Selected Item is: ", currentPath);
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

        final LinearLayout breadRoot = this;// findViewById(R.id.bread_bar);
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
                tv = new TextView(getContext());
                ((TextView)tv).setTextColor(breadcrumbTextColor);
                ((TextView)tv).setTextSize(breadcrumbTextSize);
                ((TextView)tv).setText(" " + this.listOfElements.get(i) + " ");
            }else{
                tv = new VerticalTextView(getContext());
                ((VerticalTextView)tv).setTextColor(breadcrumbTextColor);
                ((VerticalTextView)tv).setTextSize(breadcrumbTextSize);
                ((VerticalTextView)tv).setText(" " + this.listOfElements.get(i) + " ");

            }
            tv.setTag("brItem" + String.valueOf(i));

            tv.addOnLayoutChangeListener(new OnLayoutChangeListener() {
                @Override
                public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {}
            });

            breadRoot.addView(tv);

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

}
