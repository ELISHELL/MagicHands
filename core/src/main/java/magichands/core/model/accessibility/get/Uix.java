package magichands.core.model.accessibility.get;

import android.accessibilityservice.AccessibilityService;
import android.content.Context;
import android.graphics.Rect;
import android.icu.text.SimpleDateFormat;
import android.util.Log;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.AccessibilityWindowInfo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Date;
import java.util.List;

public class Uix {
    private static final String TAG = "UiHierarchyUtil";

    /**
     * 获取当前界面的 UI 层级信息并保存为 UiAutomator Viewer 可用的 XML 文件
     *
     * @param accessibilityService 无障碍服务对象
     */
    public static void saveCurrentUiHierarchyToFile(AccessibilityService accessibilityService) {
        List<AccessibilityWindowInfo> windows = accessibilityService.getWindows();
        Writer writer = null;
        try {
            File file = createOutputFile(accessibilityService.getApplicationContext());
            if (file == null) {
                Log.e(TAG, "Failed to create output file");
                return;
            }

            writer = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
            writer.write("<hierarchy rotation=\"0\">\n");

            for (AccessibilityWindowInfo window : windows) {
                AccessibilityNodeInfo rootNode = window.getRoot();
                if (rootNode != null) {
                    dumpNodeToXml(rootNode, writer, 1);
                    rootNode.recycle();
                }
            }

            writer.write("</hierarchy>");

            Log.d(TAG, "UI hierarchy saved to file: " + file.getAbsolutePath());
        } catch (IOException e) {
            Log.e(TAG, "Failed to save UI hierarchy to file", e);
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    Log.e(TAG, "Failed to close file writer", e);
                }
            }
        }
    }


    /**
     * 创建输出文件
     *
     * @param context 上下文
     * @return 输出文件
     * @throws IOException 创建文件失败时抛出 IOException
     */
    private static File createOutputFile(Context context) throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String fileName = "1" + ".xml";
//        File dir = new File(Environment.getExternalStorageDirectory(), "UiAutomator");
        File dir = new File(context.getExternalFilesDir("debug") + "/uix");
        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                return null;
            }
        }
        File file = new File(dir, fileName);
        if (!file.exists()) {
            if (!file.createNewFile()) {
                return null;
            }
        }
        return file;
    }

    /**
     * 将 AccessibilityNodeInfo 节点及其子节点的信息保存到文件
     *
     * @param node   节点
     * @param writer 文件写入器
     * @param indent 缩进级别
     * @throws IOException 写入文件失败时抛出 IOException
     */


    private static void dumpNodeToXml(AccessibilityNodeInfo node, Writer writer, int indent)
            throws IOException {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < indent; i++) {
            sb.append("\t");
        }
        sb.append("<node");
        sb.append(" drawingorder=\"" + node.getDrawingOrder() + "\"");
        sb.append(" index=\"" + "0" + "\"");
        sb.append(" layer=\"" + "0" + "\"");
        sb.append(" depth=\"" + "0" + "\"");
        sb.append(" text=\"" + node.getText() + "\"");
        sb.append(" clz=\"" + node.getClassName() + "\"");
        sb.append(" pkg=\"" + node.getPackageName() + "\"");
        sb.append(" desc=\"" + node.getContentDescription() + "\"");
        sb.append(" checkable=\"" + node.isCheckable() + "\"");
        sb.append(" checked=\"" + node.isChecked() + "\"");
        sb.append(" clickable=\"" + node.isClickable() + "\"");
        sb.append(" enabled=\"" + node.isEnabled() + "\"");
        sb.append(" focusable=\"" + node.isFocusable() + "\"");
        sb.append(" focused=\"" + node.isFocused() + "\"");
        sb.append(" scrollable=\"" + node.isScrollable() + "\"");
        sb.append(" longclickable=\"" + node.isLongClickable() + "\"");
        sb.append(" password=\"" + node.isPassword() + "\"");
        sb.append(" selected=\"" + node.isSelected() + "\"");
        sb.append(" nid=\"" + "node" + "\"");
        sb.append(" id=\"" + node.getViewIdResourceName() + "\"");
        sb.append(" visible=\"" + node.isVisibleToUser() + "\"");
        sb.append(" multiline=\"" + node.isMultiLine() + "\"");
        sb.append(" dismissable=\"" + node.isDismissable() + "\"");
        sb.append(" editable=\"" + node.isEditable() + "\"");
        Rect rect = new Rect();
        node.getBoundsInScreen(rect);
        sb.append(" bounds=\"" + rect.toShortString() + "\"");
        sb.append(" left=\"" + rect.left + "\"");
        sb.append(" top=\"" + rect.top + "\"");
        sb.append(" right=\"" + rect.right + "\"");
        sb.append(" bottom=\"" + rect.bottom + "\"");
        sb.append(" parentid=\"" + "node.getParent()" + "\"");
        sb.append(" childcount=\"" + node.getChildCount() + "\">"); // 使用封闭标签
        writer.write(sb.toString());
        writer.write("\n");
        int childCount = node.getChildCount();
        for (int i = 0; i < childCount; i++) {
            AccessibilityNodeInfo childNode = node.getChild(i);
            if (childNode != null) {
                dumpNodeToXml(childNode, writer, indent + 1);
                childNode.recycle();
            }
        }
        writer.write("</node>\n"); // 添加闭合标签
    }


//
//    private static void dumpNodeToXml(AccessibilityNodeInfo node, Writer writer, int indent)
//            throws IOException {
//        StringBuilder sb = new StringBuilder();
//        for (int i = 0; i < indent; i++) {
//            sb.append("\t");
//        }
//        sb.append("<node");
//        sb.append(" drawingorder=\"" + node.getDrawingOrder() + "\"");
//        sb.append(" index=\"" + "0" + "\"");
//        sb.append(" layer=\"" + "0" + "\"");
//        sb.append(" depth=\"" + "0" + "\"");
//        sb.append(" text=\"" + node.getText() + "\"");
//        sb.append(" clz=\"" + node.getClassName() + "\"");
//        sb.append(" pkg=\"" + node.getPackageName() + "\"");
//        sb.append(" desc=\"" + node.getContentDescription() + "\"");
//        sb.append(" checkable=\"" + node.isCheckable() + "\"");
//        sb.append(" checked=\"" + node.isChecked() + "\"");
//        sb.append(" clickable=\"" + node.isClickable() + "\"");
//        sb.append(" enabled=\"" + node.isEnabled() + "\"");
//        sb.append(" focusable=\"" + node.isFocusable() + "\"");
//        sb.append(" focused=\"" + node.isFocused() + "\"");
//        sb.append(" scrollable=\"" + node.isScrollable() + "\"");
//        sb.append(" longclickable=\"" + node.isLongClickable() + "\"");
//        sb.append(" password=\"" + node.isPassword() + "\"");
//        sb.append(" selected=\"" + node.isSelected() + "\"");
//        sb.append(" nid=\"" + "node" + "\"");
//        sb.append(" id=\"" + node.getViewIdResourceName() + "\"");
//        sb.append(" visible=\"" + node.isVisibleToUser() + "\"");
//        sb.append(" multiline=\"" + node.isMultiLine() + "\"");
//        sb.append(" dismissable=\"" + node.isDismissable() + "\"");
//        sb.append(" editable=\"" + node.isEditable() + "\"");
//        Rect rect = new Rect();
//        node.getBoundsInScreen(rect);
//        sb.append(" bounds=\"" + rect.toShortString() + "\"");
//        sb.append(" left=\"" + rect.left + "\"");
//        sb.append(" top=\"" + rect.top + "\"");
//        sb.append(" right=\"" + rect.right + "\"");
//        sb.append(" bottom=\"" + rect.bottom + "\"");
//        sb.append(" parentid=\"" + "node.getParent()" + "\"");
//        sb.append(" childcount=\"" + node.getChildCount() + "\"/>\n"); // 使用自闭合标签
//        writer.write(sb.toString());
//        writer.write("\n");
//        int childCount = node.getChildCount();
//        for (int i = 0; i < childCount; i++) {
//            AccessibilityNodeInfo childNode = node.getChild(i);
//            if (childNode != null) {
//                dumpNodeToXml(childNode, writer, indent + 1);
//                childNode.recycle();
//            }
//        }
//        writer.write("</node>\n"); // 添加闭合标签
//    }


}
