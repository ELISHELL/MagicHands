package magichands.core.tool;

import android.icu.text.SimpleDateFormat;

import java.util.Date;
import java.util.Locale;

public class TimeTool {



    /**
     * 将时间戳转换成标准时间
     *
     * @param timestamp 时间戳，单位为毫秒
     * @return 标准时间字符串，格式为"yyyy-MM-dd HH:mm:ss"
     */
    public static String timestampToStandardTime(long timestamp,String pn) {
        SimpleDateFormat sdf = new SimpleDateFormat(pn, Locale.getDefault());
        return sdf.format(new Date(timestamp));
    }

}
