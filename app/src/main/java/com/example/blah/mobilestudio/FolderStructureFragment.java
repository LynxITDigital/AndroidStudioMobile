package com.example.blah.mobilestudio;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.project_navi_fragment, container, false);
        ViewGroup containerView = (ViewGroup) rootView.findViewById(R.id.container);

        Bundle b = getArguments();
        String filePath = b.getString(FILE_PATH);
        addFileTreeView(containerView, filePath);

        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("tState", tView.getSaveState());
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
            tView.setHighlightedNode(node);
            if (fileSelectedListener != null) {
                fileSelectedListener.onFileSelected(item.file.getAbsolutePath());
            }
        }
    };

}
