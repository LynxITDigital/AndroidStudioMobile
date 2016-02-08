package com.example.blah.mobilestudio.fileTreeView;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.test.InstrumentationTestCase;
import android.test.suitebuilder.annotation.SmallTest;

import com.example.blah.mobilestudio.treeview.TreeNode;
import com.example.blah.mobilestudio.treeview.TreeNodeWrapperView;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by yehe on 4/02/2016.
 */
@RunWith(AndroidJUnit4.class)
@SmallTest
public class FileTreeViewTest extends InstrumentationTestCase {
    private FileTreeView fileTreeView;
    private String filePath;

    @Before
    public void setUp() throws Exception {
        Context context = InstrumentationRegistry.getTargetContext();
        File outputDir = context.getExternalCacheDir();
        assert outputDir != null;
        filePath = outputDir.getAbsolutePath() + "/111/222/333";
        File file = new File(filePath);
        //noinspection ResultOfMethodCallIgnored
        file.mkdirs();
        fileTreeView = new FileTreeView(context, file.getParentFile().getParentFile());
    }

    @Test
    public void shouldSetSelectedFilePath() throws Exception {
        // when highlights a file specified by file Path
        fileTreeView.highlight(filePath);

        // then view should set the selected file path
        assertThat(fileTreeView.getSelectedFilePath(), is(filePath));
    }

    @Test
    public void shouldExpandAllNodes() throws Exception {
        // when highlights a file specified by file Path
        fileTreeView.highlight(filePath);

        // then all nodes along the path starting from root node should be expanded
        TreeNode root = fileTreeView.getRoot();
        TreeNode levelOneNode = root.getChildren().get(0);
        TreeNode levelTwoNode = levelOneNode.getChildren().get(0);
        TreeNode levelThreeNode = levelTwoNode.getChildren().get(0);
        assertThat(levelOneNode.isExpanded(), is(true));
        assertThat(levelTwoNode.isExpanded(), is(true));
        assertThat(levelThreeNode.isExpanded(), is(true));
    }

    @Test
    public void shouldSetBackgroundColor() throws Exception {
        // when highlights a file specified by file Path
        fileTreeView.highlight(filePath);

        // then the highlighted node should set the to the green color
        TreeNode highlightedNode = fileTreeView.getHighlightedNode();
        TreeNodeWrapperView view = (TreeNodeWrapperView) highlightedNode.getViewHolder().getView();
        ColorDrawable background = (ColorDrawable) view.getNodeContainer().getBackground();
        assertThat(background.getColor(), is(Color.GREEN));
    }
}