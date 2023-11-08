package magichands.core.tool;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;

public class ClipBoardTool {

    // 获取剪贴板内容
    public static String getClipboardContent(Context context) {
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);

        // 检查剪贴板是否为空
        if (!clipboard.hasPrimaryClip()) {
            return null;
        }

        ClipData clipData = clipboard.getPrimaryClip();

        // 检查剪贴板数据项是否为空
        if (clipData == null || clipData.getItemCount() == 0) {
            return null;
        }

        ClipData.Item item = clipData.getItemAt(0);
        CharSequence text = item.getText();

        // 检查剪贴板文本是否为空
        if (text == null) {
            return null;
        }

        return text.toString();
    }

    // 写入剪贴板内容
    public static boolean setClipboardContent(Context context, String content) {
        try {
            ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);

            // 创建ClipData对象，并指定MIME类型为普通文本
            ClipData clipData = ClipData.newPlainText("text", content);

            // 将ClipData对象放入剪贴板
            clipboard.setPrimaryClip(clipData);

            return true; // 写入剪贴板成功
        } catch (Exception e) {
            e.printStackTrace();
            return false; // 写入剪贴板失败
        }
    }
}
