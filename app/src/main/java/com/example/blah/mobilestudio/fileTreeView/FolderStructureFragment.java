package com.example.blah.mobilestudio.fileTreeView;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.blah.mobilestudio.R;
import com.example.blah.mobilestudio.treeview.TreeNode;

import java.io.File;

/**
 * Created by Ye He on 20/01/16.
 */
public class FolderStructureFragment extends Fragment {
    private FileTreeView tView;
    private OnFileSelectedListener fileSelectedListener;
    public static final String FILE_PATH = "FILE_PATH";
    private String tState;
    private String selectedFilePath;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.project_navi_fragment, container, false);

        if (savedInstanceState != null) {
            tState = savedInstanceState.getString("tState");
            selectedFilePath = savedInstanceState.getString("selectedFilePath");
        }

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();

        ViewGroup containerView = (ViewGroup) getActivity().findViewById(R.id.container);

        Bundle b = getArguments();
        String filePath = b.getString(FILE_PATH);
        addFileTreeView(containerView, filePath);

        if (tState != null) {
            tView.restoreState(tState);
        }

        if (selectedFilePath != null) {
            tView.highlight(selectedFilePath);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        doAttach((Activity) context);
    }

    private void doAttach(Activity activity) {
        try {
            fileSelectedListener = (OnFileSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFileSelectedListener");
        }
    }

    /**
     * Call super.onSaveInstanceState() in the end.
     * Please refer to http://stackoverflow.com/questions/7575921/illegalstateexception
     * -can-not-perform-this-action-after-onsaveinstancestate-wit
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString("tState", tView.getSaveState());
        outState.putString("selectedFilePath", tView.getSelectedFilePath());
        super.onSaveInstanceState(outState);
    }

    /**
     *  Expand folder view to the file following the path and highlight the node.
     *
     *  @param path an absolute path giving the location of the file
     */
    public void highlightFile(String path) {
        tView.highlight(path);
    }

    public interface OnFileSelectedListener {
        void onFileSelected(String path);
    }

    public void setOnClickListener(OnFileSelectedListener onFileSelectedListener) {
        fileSelectedListener = onFileSelectedListener;
    }

    private void addFileTreeView(ViewGroup containerView, String filePath) {
        File rootFile = new File(filePath);

        tView = new FileTreeView(this.getActivity(), rootFile);
        tView.setDefaultNodeClickListener(myNodeClickListener);

        containerView.addView(tView.getView());
    }

    private TreeNode.TreeNodeClickListener myNodeClickListener = new TreeNode.TreeNodeClickListener() {
        @Override
        public void onClick(TreeNode node, Object value) {
            FileNodeViewHolder.IconTreeItem item = (FileNodeViewHolder.IconTreeItem) value;
            String filePath = item.file.getAbsolutePath();
            tView.setHighlightedNode(node);
            tView.setSelectedFilePath(filePath);
            // build up children nodes
            File[] files = item.file.listFiles();
            node.deleteChildren();
            if (files != null) {
                for (File file : files) {
                    FileTreeFactory.setUpNode(node, file);
                }
            }
            if (fileSelectedListener != null) {
                fileSelectedListener.onFileSelected(filePath);
            }
        }
    };

}
