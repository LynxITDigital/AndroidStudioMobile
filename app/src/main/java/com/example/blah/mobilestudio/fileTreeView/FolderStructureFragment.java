package com.example.blah.mobilestudio.fileTreeView;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.example.blah.mobilestudio.R;
import com.example.blah.mobilestudio.fileTreeView.FileNodeViewHolder;
import com.example.blah.mobilestudio.fileTreeView.FileTreeView;
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
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (context instanceof Activity) {
                doAttach((Activity) context);
            }
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            doAttach(activity);
        }
    }


    private void doAttach(Activity activity) {
        try {
            fileSelectedListener = (OnFileSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFileSelectedListener");
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("tState", tView.getSaveState());
        outState.putString("selectedFilePath", tView.getSelectedFilePath());
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
            if (fileSelectedListener != null) {
                fileSelectedListener.onFileSelected(filePath);
            }
        }
    };

}
