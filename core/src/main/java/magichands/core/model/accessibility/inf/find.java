package magichands.core.model.accessibility.inf;





import static magichands.core.model.accessibility.AppAccessibilityService.acc;

import android.util.Log;
import android.view.accessibility.AccessibilityNodeInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class find {
    /**

     根据控件ID查找对应的节点
     @param nodeId 控件ID
     @return AccessibilityNodeInfo
     */
    public static AccessibilityNodeInfo findNodeById(String nodeId) {
        AccessibilityNodeInfo rootNode = acc.getRootInActiveWindow();
        if (rootNode == null || nodeId == null) {
            return null;
        }
        List<AccessibilityNodeInfo> nodes = rootNode.findAccessibilityNodeInfosByViewId(nodeId);
        if (nodes != null && !nodes.isEmpty()) {
            return nodes.get(0);
        }
        return null;
    }

    /**
     根据控件文本查找对应的节点
     @param text 控件文本
     @return AccessibilityNodeInfo
     */
//    public static AccessibilityNodeInfo findNodeByText(String text) {
//        AccessibilityNodeInfo rootNode = acc.getRootInActiveWindow();
//        if (rootNode == null || text == null) {
//            return null;
//        }
//        List<AccessibilityNodeInfo> nodes = rootNode.findAccessibilityNodeInfosByText(text);
//        if (nodes != null && !nodes.isEmpty()) {
//            return nodes.get(0);
//        }
//        return null;
//    }


//    public static AccessibilityNodeInfo findNodeByText(String text) {
//        AccessibilityNodeInfo rootNode = acc.getRootInActiveWindow();
//        if (rootNode == null || text == null) {
//            return null;
//        }
//        List<AccessibilityNodeInfo> nodes = rootNode.findAccessibilityNodeInfosByText(text);
//        if (nodes != null && !nodes.isEmpty()) {
//            // 遍历找到的节点，判断其 text 属性是否包含传入的 text 参数
//            for (AccessibilityNodeInfo node : nodes) {
//                CharSequence nodeText =node.getText() ;
//                System.out.println("当前页面："+node.getText());
//                if (nodeText != null && nodeText.toString().contains(text)) {
//                    return node;
//                }
//            }
//        }
//        return null;
//    }

    public static AccessibilityNodeInfo findNodeByText(String text) {
        AccessibilityNodeInfo rootNode = acc.getRootInActiveWindow();
        if (rootNode == null || text == null) {
            return null;
        }
        return findNodeByTextInNode(rootNode, text);
    }

    private static AccessibilityNodeInfo findNodeByTextInNode(AccessibilityNodeInfo node, String text) {
        CharSequence nodeText = node.getText();
        if (nodeText != null && nodeText.toString().contains(text)) {
            return node;
        }
        for (int i = 0; i < node.getChildCount(); i++) {
            AccessibilityNodeInfo childNode = node.getChild(i);
            if (childNode != null) {
                AccessibilityNodeInfo foundNode = findNodeByTextInNode(childNode, text);
                if (foundNode != null) {
                    return foundNode;
                }
            }
        }
        return null;
    }





    /**
     * 通过节点描述查找节点
     * @param desc 节点描述
     * @return AccessibilityNodeInfo
     */
    /**
     * 根据节点的描述信息查找 AccessibilityNodeInfo 对象
     * @param desc 要查找的节点描述信息
     * @return 查找到的节点，如果没有找到则返回 null
     */
    public static AccessibilityNodeInfo findNodeByDesc(String desc) {
        AccessibilityNodeInfo rootNode = acc.getRootInActiveWindow();
        if (rootNode == null || desc == null) {
            return null;
        }
        List<AccessibilityNodeInfo> nodes = rootNode.findAccessibilityNodeInfosByText(desc);
        if (nodes != null && !nodes.isEmpty()) {
            for (AccessibilityNodeInfo node : nodes) {
                if (desc.equals(node.getContentDescription())) {
                    return node;
                }
            }
        }
        return null;
    }



    /**
     查找传入节点的前一个兄弟节点
     @param node 待查找节点
     @return AccessibilityNodeInfo
     */
    public static AccessibilityNodeInfo findPreviousSibling(AccessibilityNodeInfo node) {
        if (node == null) {
            return null;
        }
        AccessibilityNodeInfo parent = node.getParent();
        if (parent == null) {
            return null;
        }
        int childCount = parent.getChildCount();
        int index = -1;
        for (int i = 0; i < childCount; i++) {
            AccessibilityNodeInfo child = parent.getChild(i);
            if (child == null) {
                continue;
            }
            if (child.equals(node)) {
                index = i;
                break;
            }
        }
        if (index > 0) {
            return parent.getChild(index - 1);
        }
        return null;
    }

    /**

     查找传入节点的下一个兄弟节点
     @param node 传入的节点
     @return 传入节点的下一个兄弟节点，若不存在则返回null
     */
    public static AccessibilityNodeInfo findNextSibling(AccessibilityNodeInfo node) {
        if (node == null) {
            return null;
        }
        AccessibilityNodeInfo parent = node.getParent();
        if (parent == null) {
            return null;
        }
        int childCount = parent.getChildCount();
        int index = -1;
        for (int i = 0; i < childCount; i++) {
            AccessibilityNodeInfo child = parent.getChild(i);
            if (child == null) {
                continue;
            }
            if (child.equals(node)) {
                index = i;
                break;
            }
        }
        if (index >= 0 && index < childCount - 1) {
            return parent.getChild(index + 1);
        }
        return null;
    }

    /**

     输出节点信息到日志
     @param node AccessibilityNodeInfo对象
     */
    public static void printNodeInfo(AccessibilityNodeInfo node) {
        if (node == null) {
            return;
        }
        JSONObject obj = new JSONObject();
        try {
            obj.put("text", node.getText());
            obj.put("id", node.getViewIdResourceName());
            obj.put("desc", node.getContentDescription());
            obj.put("pkg", node.getPackageName());
// 其他信息可以在此添加
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("AccessibilityDemo", obj.toString());
    }
    /**
     * 获取给定节点的父节点
     * @param node 要获取父节点的节点
     * @return 返回给定节点的父节点，如果不存在则返回 null
     */
    public static AccessibilityNodeInfo getParentNode(AccessibilityNodeInfo node) {
        if (node == null) {
            return null;
        }
        return node.getParent();
    }
    /**
     * 获取给定节点的所有子节点
     * @param node 要获取子节点的节点
     * @return 返回给定节点的所有子节点列表，如果不存在子节点则返回 null
     */
    public static List<AccessibilityNodeInfo> getChildNodes(AccessibilityNodeInfo node) {
        if (node == null) {
            return null;
        }
        List<AccessibilityNodeInfo> childNodes = new ArrayList<>();
        int childCount = node.getChildCount();
        for (int i = 0; i < childCount; i++) {
            AccessibilityNodeInfo childNode = node.getChild(i);
            if (childNode != null) {
                childNodes.add(childNode);
            }
        }
        return childNodes;
    }


}
