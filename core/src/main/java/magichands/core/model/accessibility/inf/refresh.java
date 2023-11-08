package magichands.core.model.accessibility.inf;


import static magichands.core.model.accessibility.AppAccessibilityService.acc;

import android.view.accessibility.AccessibilityNodeInfo;

public class refresh {


    public static void recycle(AccessibilityNodeInfo parentNode){
        parentNode.recycle();
    }

    /**
     * 强制更新当前页面的节点信息
     *
     */
    public static void refreshCurrentPageNodes() {
        AccessibilityNodeInfo rootNode = acc.getRootInActiveWindow(); // 获取当前活动窗口的根节点

        if (rootNode == null) {
            return; // 如果根节点为空，则退出方法
        }

        rootNode.refresh(); // 强制刷新当前页面的所有节点信息

        // 循环遍历所有子节点，并递归调用refreshNode()方法来强制更新所有子节点的信息
        for (int i = 0; i < rootNode.getChildCount(); i++) {
            AccessibilityNodeInfo childNode = rootNode.getChild(i);
            if (childNode != null) {
                refreshNode(childNode);
            }
        }
    }

    /**
     * 递归方法，用于强制更新指定节点及其所有子节点的信息
     *
     * @param node 要更新的节点
     */
    private static void refreshNode(AccessibilityNodeInfo node) {
        node.refresh(); // 强制更新当前节点的信息

        // 循环遍历所有子节点，并递归调用refreshNode()方法来强制更新所有子节点的信息
        for (int i = 0; i < node.getChildCount(); i++) {
            AccessibilityNodeInfo childNode = node.getChild(i);
            if (childNode != null) {
                refreshNode(childNode);
            }
        }
    }


}
