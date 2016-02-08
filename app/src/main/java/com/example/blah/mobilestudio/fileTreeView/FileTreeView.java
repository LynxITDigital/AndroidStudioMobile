package com.example.blah.mobilestudio.fileTreeView;

import android.content.Context;
import android.graphics.Color;

import com.example.blah.mobilestudio.R;
import com.example.blah.mobilestudio.treeview.AndroidTreeView;
import com.example.blah.mobilestudio.treeview.TreeNode;
import com.example.blah.mobilestudio.treeview.TreeNodeWrapperView;

import java.io.File;

/**
 * Created by Ye He on 20/01/16.
 */
public class FileTreeView extends AndroidTreeView {
    private TreeNode highlightedNode;
    private String selectedFilePath;

    public FileTreeView(Context context, File file) {
        super(context);
        TreeNode root = TreeNode.root();
        setUpNode(root, file);
        setRoot(root);

        setDefaultViewHolder(FileNodeViewHolder.class);
        setUse2dScroll(true);
        setFullWidth(true);
    }


    public void highlight(String path) {
        TreeNode treeNode = expandTo2(path, mRoot);
        if (treeNode != null) {
            setHighlightedNode(treeNode);
            selectedFilePath = path;
        }
    }

    private void setUpNode(TreeNode root, File rootFile) {
        if (rootFile.exists()) {
            TreeNode rootDirNode;

            if (rootFile.isDirectory()) {
                rootDirNode = new TreeNode(new FileNodeViewHolder.IconTreeItem(R.drawable.folder_48, rootFile));
                File[] files = rootFile.listFiles();
                if (files != null) {
                    for (File file : files) {
                        setUpNode(rootDirNode, file);
                    }
                }
            } else {
                rootDirNode = new TreeNode(new FileNodeViewHolder.IconTreeItem(R.drawable.file_48, rootFile));
            }

            root.addChild(rootDirNode);
        }
    }

    private TreeNode expandTo(String path, TreeNode root) {
        int index = path.indexOf(File.separator, 1);
        if (index != -1) {
            String filename = path.substring(1, index);
            String leftPath = path.substring(index, path.length());

            for (TreeNode treeNode : root.getChildren()) {
                FileNodeViewHolder.IconTreeItem item = (FileNodeViewHolder.IconTreeItem) treeNode.getValue();
                if (item.file.getName().equals(filename)) {
                    expandNode(treeNode);
                    return expandTo(leftPath, treeNode);
                }
            }

            return expandTo(leftPath, root);
        } else {
            String filename = path.substring(1, path.length());

            for (TreeNode treeNode : root.getChildren()) {
                FileNodeViewHolder.IconTreeItem item = (FileNodeViewHolder.IconTreeItem) treeNode.getValue();
                if (item.file.getName().equals(filename)) {
                    return treeNode;
                }
            }

            return null;
        }
    }

    private TreeNode expandTo2(String path, TreeNode root) {
        if (path == null) {
            return null;
        }

        File file = new File(path);
        String parentPath = file.getParent();

        for (TreeNode childNode : root.getChildren()) {
            FileNodeViewHolder.IconTreeItem item = (FileNodeViewHolder.IconTreeItem) childNode.getValue();
            if (item.file.getName().equals(file.getName())) {
                expandNode(childNode);
                return childNode;
            }
        }

        TreeNode treeNode = expandTo2(parentPath, root);
        if (treeNode == null) {
            return null;
        } else {
            for (TreeNode childNode : treeNode.getChildren()) {
                FileNodeViewHolder.IconTreeItem item = (FileNodeViewHolder.IconTreeItem) childNode.getValue();

                if (item.file.getName().equals(file.getName())) {
                    expandNode(childNode);
                    return childNode;
                }

            }
            return null;
        }
    }

    public void setHighlightedNode(TreeNode highlightedNode) {
        TreeNodeWrapperView view;
        if (this.highlightedNode != null) {
            view = (TreeNodeWrapperView) this.highlightedNode.getViewHolder().getView();
            view.getNodeContainer().setBackgroundColor(Color.TRANSPARENT);
        }
        this.highlightedNode = highlightedNode;
        view = (TreeNodeWrapperView) this.highlightedNode.getViewHolder().getView();
        view.getNodeContainer().setBackgroundColor(Color.GREEN);
    }

    public TreeNode getHighlightedNode() {
        return highlightedNode;
    }

    public String getSelectedFilePath() {
        return selectedFilePath;
    }

    public void setSelectedFilePath(String selectedFilePath) {
        this.selectedFilePath = selectedFilePath;
    }

    public TreeNode getRoot() {
        return mRoot;
    }
}
