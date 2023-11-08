package magichands.core.engine;

import static magichands.core.debug.Interaction.sendLogToServer;
import static magichands.core.debug.Windows.runHandler;
import static magichands.core.debug.Windows.ws;
import static magichands.core.debug.tool.method;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;

import org.mozilla.javascript.ImporterTopLevel;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringReader;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import magichands.core.Env;
import magichands.core.OpenApi;

public class rhino {
    static Context context = Env.Companion.getContext();
    static  int totalLines = 0;
    static String jsString=null;

    public static void start(Boolean ps,Boolean ms){

        org.mozilla.javascript.Context context = org.mozilla.javascript.Context.enter();
        Scriptable scope = new ImporterTopLevel(context);
        context.setOptimizationLevel(-1);
        context.setLanguageVersion(180);
        Log.e("版本", String.valueOf(context.getLanguageVersion()));
        OpenApi api = new OpenApi();
        Object jsObj = org.mozilla.javascript.Context.javaToJS(api, scope);
        ScriptableObject.putProperty(scope, "js", jsObj);


        if (ms){
            release(ps);
        }else{
            debug(ps);
        }
        try{
            context.evaluateString(scope,jsString, null, 1, null);

        } catch (Exception e) {
            String err=null;
            String errorMessage = e.getMessage();
            int lineNumber = -1;
            int columnNumber = -1;
            if (e instanceof org.mozilla.javascript.RhinoException) {
                org.mozilla.javascript.RhinoException rhinoException = (org.mozilla.javascript.RhinoException) e;
                lineNumber = rhinoException.lineNumber();
                columnNumber = rhinoException.columnNumber();

            }
            err="调用: " + Arrays.toString(e.getStackTrace());
            Pattern pattern = Pattern.compile("(.*?)\\(unnamed \\s.*");

            Matcher matcher = pattern.matcher(errorMessage);
            if (matcher.find()) {
                String value = matcher.group(1);
                System.out.println("匹配到的值为: " + value);
                err+=",原因："+ value+",列号: "+columnNumber;
            } else {
                System.out.println("未找到匹配的值");
                err+=",原因："+errorMessage+",列号: "+columnNumber;
            }
            System.out.println("报错信息: " + errorMessage);
            System.out.println("报错行号: " + lineNumber);
            System.out.println("报错列号: " + columnNumber);
            System.out.println("调用关系: " + Arrays.toString(e.getStackTrace()));
            // 根据行号和列号提取报错的具体代码
            if (lineNumber != -1 && columnNumber != -1) {
                String[] lines = jsString.split("\n");
                if (lineNumber <= lines.length) {
                    String errorLine = lines[lineNumber - 1];
                    String prevLine = lineNumber > 1 ? lines[lineNumber - 2] : "";
                    System.out.println("报错代码: " + errorLine);
                    System.out.println("上一行代码: " + prevLine);
                    pattern = Pattern.compile(".*line\\s*=\\s*(\\d+)");

                    matcher = pattern.matcher(prevLine);

                    if (matcher.find()) {
                        String value = matcher.group(1);
                        System.out.println("匹配到的值为: " + value);
                        err+=",行号："+value;
                    } else {
                        System.out.println("未找到匹配的值");
//                err+=",行号："+"具体行号无法获取！";
                        err+=",行号："+lineNumber;
                    }
                    pattern = Pattern.compile("file\\s*=\\s*['\"]([^'\"]+)['\"]");
                    matcher = pattern.matcher(prevLine);

                    if (matcher.find()) {
                        String value = matcher.group(1);
                        System.out.println("匹配到的值为: " + value);
                        err+=",文件："+value;
                    } else {
                        System.out.println("未找到匹配的值");
                        err+=",文件："+"具体文件无法获取！";
                    }
                    err+=",代码："+errorLine;
//            System.out.println(" ".repeat(columnNumber - 1) + "^");
                }
            }
            System.out.println("MagicHands报错日志："+err);
            sendLogToServer("MagicHands报错日志："+err);
            method("MagicHands报错日志："+err);
            Handler mainHandler = new Handler(Looper.getMainLooper());
            String finalErr = err;
            if (ws) {
                if (Settings.canDrawOverlays(Env.Companion.getActivity())) {
                    mainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            try {
//                                Class<?>     hostHandlerClass = Class.forName("pa.magichands.MainActivity");
//                                Method hostHandlerField = hostHandlerClass.getDeclaredMethod("getWindows"); // 替换为宿主中的 Handler 对象的变量名
//                                hostHandlerField.setAccessible(true);
//                                Handler hostHandler = (Handler)  hostHandlerField.invoke(hostHandlerClass.newInstance());  // 如果宿主 Handler 是静态的，则传入 null，否则传入宿主对象
//                                logwindows(hostHandler,"MagicHands报错日志："+ finalErr);
                                runHandler(2,magichands.core.Handler.getHandler("debugWindows"),"MagicHands报错日志："+ finalErr);

                            } catch (Exception e) {
                                System.out.println(e);
//                    throw new RuntimeException(e);
                            }
                        }
                    });
                }
            }

        } finally {
            org.mozilla.javascript.Context.exit();
        }

    }


    public static void debug(Boolean ps){
        totalLines=0;
        jsString=null;
        File f=new File(String.valueOf(context.getExternalFilesDir("debug/js")));
        String[] filenamelist= listFiles(f);
        if (ps){
            System.out.println("加密传输");
            okjs(filenamelist);
        }else{
            System.out.println("不加密传输");
            okjs1(filenamelist);
        }
    }


    public static void release(Boolean ps){
        totalLines=0;
        jsString=null;
        String[] filenamelist= new String[0];

        filenamelist = listFiles(context,"js");

        if (ps){
            okjss(filenamelist);
        }else{
            okjs11(filenamelist);
        }


    }
    public static void okjs(String [] filenamelist){

        for (String s : filenamelist) {
            if (containsSubstring(s, "init.js")) {
                jsString=key(readFileContent(s));

                break;
            }
        }
        for (String s : filenamelist) {
            if (!containsSubstring(s, "main.js")&&!containsSubstring(s, "init.js")) {
                jsString+= addLineNumberToString(key(readFileContent(s)),s);
            }

        }
        for (String s : filenamelist) {
            if (containsSubstring(s, "main.js")) {
                jsString+= addLineNumberToString(key(readFileContent(s)),s);
                break;
            }
        }
        System.out.println("加了多少"+totalLines);
        System.out.println("改变后的："+jsString);
    }

    public static void okjs1(String [] filenamelist){

        for (String s : filenamelist) {
            if (containsSubstring(s, "init.js")) {
                jsString=readFileContent(s);

                break;
            }
        }
        for (String s : filenamelist) {
            if (!containsSubstring(s, "main.js")&&!containsSubstring(s, "init.js")) {
                jsString+= addLineNumberToString(readFileContent(s),s);
            }

        }
        for (String s : filenamelist) {
            if (containsSubstring(s, "main.js")) {
                jsString+= addLineNumberToString(readFileContent(s),s);
                break;
            }
        }
        System.out.println("加了多少"+totalLines);
        System.out.println("改变后的："+jsString);
    }

    public static void okjss(String [] filenamelist){

        for (String s : filenamelist) {
            if (containsSubstring(s, "init.js")) {
                jsString=key(readFileFromAssets(context,s));

                break;
            }
        }
        for (String s : filenamelist) {
            if (!containsSubstring(s, "main.js")&&!containsSubstring(s, "init.js")) {
                jsString+= addLineNumberToString(key(readFileFromAssets(context,s)),s);
            }

        }
        for (String s : filenamelist) {
            if (containsSubstring(s, "main.js")) {
                jsString+= addLineNumberToString(key(readFileFromAssets(context,s)),s);
                break;
            }
        }
        System.out.println("加了多少"+totalLines);
        System.out.println("改变后的："+jsString);
    }

    public static void okjs11(String [] filenamelist){

        for (String s : filenamelist) {
            if (containsSubstring(s, "init.js")) {
                jsString=readFileFromAssets(context,s);

                break;
            }
        }
        for (String s : filenamelist) {
            if (!containsSubstring(s, "main.js")&&!containsSubstring(s, "init.js")) {
                jsString+= addLineNumberToString(readFileFromAssets(context,s),s);
            }

        }
        for (String s : filenamelist) {
            if (containsSubstring(s, "main.js")) {
                jsString+= addLineNumberToString(readFileFromAssets(context,s),s);
                break;
            }
        }
        System.out.println("加了多少"+totalLines);
        System.out.println("改变后的："+jsString);
    }

    public static String readFileFromAssets(Context context, String filePath) {
        AssetManager assetManager = context.getAssets();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[4096];
        try {
            InputStream inputStream = assetManager.open(filePath);
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                byteArrayOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return byteArrayOutputStream.toString();
    }
    public static String readFileContent(String filePath) {
        // 创建一个字符串缓冲区，用于存储文件内容
        StringBuilder content = new StringBuilder();

        // 使用try-with-resources语句，自动关闭文件流
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            // 逐行读取文件内容
            while ((line = reader.readLine()) != null) {
                // 将每行内容追加到字符串缓冲区
                content.append(line).append(System.lineSeparator());
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // 返回文件内容的字符串
        return content.toString();
    }



    public static boolean containsSubstring(String inputString, String substring) {
        return inputString.contains(substring);
    }

    public static String addLineNumberToString(String inputString,String name) {
        StringBuilder result = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new StringReader(inputString))) {
            String line;
            while ((line = reader.readLine()) != null) {

                String modifiedLine = null;
                if (containsSubstring(line, "logd(")) {
                    totalLines++;
                    modifiedLine = "line=" + totalLines + ",file='" + name + "'" + "\n" + line.replaceFirst("logd\\(", "logd('栈:'+file+' #'+line,") + "\n";
                }else{
                    if (containsSubstring(line, "toast(")) {
                        totalLines++;
                        modifiedLine = "line=" + totalLines + ",file='" + name + "'" + "\n" + line.replaceFirst("toast\\(", "toast('栈:'+file+' #'+line,") + "\n";
                    }else{
                        modifiedLine=line+ "\n";
                    }
                }
                result.append(modifiedLine);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result.toString();
    }
    public static String[]  listFiles(File dir){
        List<String> fileList = new ArrayList<>();
        File[] files = dir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    String[] subFileList = listFiles(file); // 递归读取子目录下的文件
                    for (String subFile : subFileList) {
                        fileList.add(subFile);
                    }
                } else {
                    fileList.add(file.getAbsolutePath());
                }
            }
        }
        return fileList.toArray(new String[0]);
    }

    public static String[] listFiles(Context context, String folderName) {
        List<String> fileList = new ArrayList<>(); // 用于保存文件名的列表
        try {
            Stack<String> stack = new Stack<>(); // 用于存放待处理的路径
            stack.push(folderName); // 将初始路径入栈

            while (!stack.isEmpty()) {
                String currentPath = stack.pop(); // 弹出当前路径

                // 获取当前路径下的全部文件、文件夹
                String[] list = context.getAssets().list(currentPath);

                if (list == null || list.length <= 0) {
                    // 当前为文件或当前目录下为空时，跳过当前路径的处理
                    continue;
                }

                for (int i = 0; i < list.length; i++) {
                    String subPath;
                    if ("".equals(currentPath)) {
                        // 如果当前是根目录
                        subPath = list[i];
                    } else {
                        // 如果当前不是根目录
                        subPath = currentPath + "/" + list[i];
                    }

                    // 判断是否为文件
                    InputStream inputStream = null;
                    try {
                        inputStream = context.getAssets().open(subPath);
                        if (inputStream != null) {
                            // 如果能成功打开文件流，说明是文件而非目录
                            inputStream.close();
                            fileList.add(subPath);
                        }
                    } catch (IOException e) {
                        // 报错说明是目录，将子目录路径入栈，以便后续处理
                        stack.push(subPath);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        // 将列表转换为字符串数组并返回
        return fileList.toArray(new String[0]);
    }


    public static String key(String js){
        Class<?> libProviderClazz = null;
        Object string=null;
        try {
            libProviderClazz = Class.forName("engine.key.python.api");
            Method start = libProviderClazz.getDeclaredMethod(
                    "js",
                    Context.class,
                    String.class
            ); // 获取方法
            start.setAccessible(true); // 把方法设为public，让外部可以调用
            string = start.invoke(libProviderClazz.newInstance(), context, js); // 调用方法并获取返回值
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        System.out.println("解密前:"+string);
        System.out.println("解密后:"+string);

        return String.valueOf(string);
    }


}
