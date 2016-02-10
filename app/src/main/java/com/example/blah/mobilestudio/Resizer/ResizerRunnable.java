package com.example.blah.mobilestudio.Resizer;

import android.view.View;

import com.example.blah.mobilestudio.R;
import com.example.blah.mobilestudio.activities.MainActivity;

/**
 * Created by Stephen on 10/02/2016.
 */
public class ResizerRunnable implements Runnable {
    private boolean isLandscape;
    private View outerLayout;

    private View heirarchyFrameLayout;
    private View horizontalResizer;
    private View contentFrameLayout;

    private View topLayout;
    private View verticalResizer;
    private View androidMonitorLayout;

    private View toolbarView;

    private View verticalBreadCrumbView;

    private ResizerFragment horizontalResizerFragment;
    private ResizerFragment verticalResizerFragment;

    /**
     *
     * @param outerLayout - the outer layout that contains all of the other views to be resized
     * @param isLandscape - boolean, true if the view is in landscape mode
     * @param horizontalResizerFragment - the horizontal resizer fragment, can be null
     * @param verticalResizerFragment - the vertical resizer fragment
     */
    public ResizerRunnable(View outerLayout,
                           boolean isLandscape,
                           ResizerFragment horizontalResizerFragment,
                           ResizerFragment verticalResizerFragment)
    {
        this.outerLayout = outerLayout;
        this.isLandscape = isLandscape;
        this.horizontalResizerFragment = horizontalResizerFragment;
        this.verticalResizerFragment = verticalResizerFragment;

        heirarchyFrameLayout = outerLayout.findViewById(R.id.explorer_fragment);


        topLayout = outerLayout.findViewById(R.id.top_layout);
        verticalResizer = outerLayout.findViewById(R.id.top_monitor_resizer_vertical_fragment);
        androidMonitorLayout = outerLayout.findViewById(R.id.android_monitor_outer_layout);

        toolbarView = outerLayout.findViewById(R.id.toolbar);

        verticalBreadCrumbView = outerLayout.findViewById(R.id.verticalTabFragment);
        horizontalResizer = outerLayout.findViewById(R.id.explorer_content_resizer_horizontal_fragment);

        if(isLandscape){
            contentFrameLayout = outerLayout.findViewById(R.id.content_fragment);
        }
    }

    @Override
    public void run() {
        if(isLandscape){
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

        verticalResizerFragment.configure(new SizableRegion(
                toolbarView.getHeight() + MainActivity.REGION_OFFSET,
                outerLayout.getHeight() - (MainActivity.REGION_OFFSET + horizontalResizer.getWidth())
        ));
    }

    void configureHorizontalResizerRegion() {
        horizontalResizerFragment.configure(new SizableRegion(
                verticalBreadCrumbView.getWidth() + MainActivity.REGION_OFFSET,
                outerLayout.getWidth() - (MainActivity.REGION_OFFSET + verticalResizer.getHeight())
        ));
    }
}

