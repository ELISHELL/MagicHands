package magichands.core.tool;

import android.content.Context;
import android.content.res.AssetManager;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class FileTool {

    public static String readJsonFile(String filePath, String key) {
        Gson gson = new Gson();
        JsonParser parser = new JsonParser();
        System.out.println("Key to retrieve: " + filePath);
        System.out.println("Key to retrieve: " + key);
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            JsonElement rootElement = parser.parse(reader);
            JsonObject jsonObject = rootElement.getAsJsonObject();
            System.out.println("Key to retrieve: " + jsonObject);
            // 获取指定键的值并返回
            JsonElement valueElement = jsonObject.get(key);
            if (valueElement != null) {
                return valueElement.getAsString(); // 将JsonElement转换为字符串返回
            } else {
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 判断指定路径的文件是否存在
     *
     * @param filePath 文件路径
     * @return 存在返回 true，否则返回 false
     */
    public static boolean isFileExists(String filePath) {
        File file = new File(filePath);
        return file.exists();
    }
    /**
     * 判断指定路径的文件夹是否存在
     *
     * @param dirPath 文件夹路径
     * @return 存在返回 true，否则返回 false
     */
    public static boolean isDirectoryExists(String dirPath) {
        File directory = new File(dirPath);
        return directory.isDirectory() && directory.exists();
    }


    /**
     * 读取指定文件中所有行的内容
     *
     * @param filePath 文件路径
     * @return 包含所有行内容的字符串数组，如果文件不存在或读取失败，则返回 null
     */
    public static String[] readLinesFromFile(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            return null;
        }

        List<String> linesList = new ArrayList<>();
        BufferedReader reader = null;

        try {
            reader = new BufferedReader(new FileReader(file));
            String line = null;
            while ((line = reader.readLine()) != null) {
                linesList.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        String[] linesArray = new String[linesList.size()];
        return linesList.toArray(linesArray);
    }

    /**
     * 读取指定文件中指定行的内容
     *
     * @param filePath 文件路径
     * @param lineNumber 行号（从 1 开始）
     * @return 指定行的内容字符串，如果文件不存在或行号不正确，则返回 null
     */
    public static String readLineFromFile(String filePath, int lineNumber) {
        File file = new File(filePath);
        if (!file.exists() || lineNumber <= 0) {
            return null;
        }

        BufferedReader reader = null;
        String line = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            int currentLineNumber = 1;
            while ((line = reader.readLine()) != null) {
                if (currentLineNumber == lineNumber) {
                    break;
                }
                currentLineNumber++;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return line;
    }

    /**
     * 删除指定文件夹
     *
     * @param folderPath 文件夹路径
     * @return 如果成功删除，则返回 true，否则返回 false
     */
    public static boolean deleteFolder(String folderPath) {
        File folder = new File(folderPath);
        if (!folder.exists() || !folder.isDirectory()) {
            return false;
        }

        File[] files = folder.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    if (!file.delete()) {
                        return false;
                    }
                } else if (file.isDirectory()) {
                    deleteFolder(file.getAbsolutePath());
                }
            }
        }

        return folder.delete();
    }

    /**
     * 创建文件夹，如果文件夹已存在，则不执行任何操作
     *
     * @param folderPath 文件夹路径
     * @return 如果成功创建或文件夹已经存在，则返回 true；否则返回 false
     */
    public static boolean createFolder(String folderPath) {
        File folder = new File(folderPath);
        if (folder.exists() && folder.isDirectory()) {
            // 文件夹已存在，不执行任何操作
            return true;
        }

        boolean result = folder.mkdirs();
        if (!result) {
            // 创建文件夹失败
            return false;
        }

        return true;
    }


    /**
     * 获取指定文件夹下所有文件名的列表
     *
     * @param folderPath 文件夹路径
     * @return 文件名列表；如果文件夹不存在或者不是一个文件夹，返回 null
     */
    public static List<String> listFolderFiles(String folderPath) {
        File folder = new File(folderPath);
        if (!folder.exists() || !folder.isDirectory()) {
            return null;
        }

        List<String> fileNames = new ArrayList<>();
        File[] files = folder.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    fileNames.add(file.getName());
                }
            }
        }

        return fileNames;
    }

    /**
     * 将内容追加写入文本文件，并自动换行
     *
     * @param filePath 文件路径
     * @param content  要写入的内容
     * @return 如果写入成功，则返回 true；否则返回 false
     */
    public static boolean appendTextToFile(String filePath, String content) {
        BufferedWriter writer = null;

        try {
            // 创建 BufferedWriter 对象
            writer = new BufferedWriter(new FileWriter(filePath, true));
            // 写入内容并换行
            writer.write(content + System.lineSeparator());
            // 刷新缓存区
            writer.flush();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            // 关闭流
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    /**
     * 将内容写入文本文件（覆盖原有内容）
     *
     * @param filePath 文件路径
     * @param content  要写入的内容
     * @return 如果写入成功，则返回 true；否则返回 false
     */
    public static boolean writeTextToFile(String filePath, String content) {
        FileWriter writer = null;

        try {
            // 创建 FileWriter 对象
            writer = new FileWriter(filePath, false);
            // 写入内容
            writer.write(content);
            // 刷新缓存区
            writer.flush();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            // 关闭流
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    /**
     * 删除指定的文件
     *
     * @param filePath 要删除的文件路径
     * @return 如果删除成功，则返回 true；否则返回 false
     */
    public static boolean deleteFile(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            return file.delete();
        } else {
            return false;
        }
    }

    /**
     * 创建一个新文件
     *
     * @param filePath 文件路径
     * @return 如果创建成功，则返回 true；否则返回 false
     */
    public static boolean createNewFile(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            try {
                return file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        } else {
            return false;
        }
    }



    /**

     删除指定文件中指定行的内容
     @param filePath 要删除内容的文件路径
     @param lineNumber 要删除的行号
     @return 成功返回 true，失败返回 false
     @throws IOException 如果无法读取或写入文件，会抛出 IOException 异常
     */
    public static Boolean deleteLine(String filePath, int lineNumber)  {
        File inputFile = new File(filePath);
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(inputFile));
            BufferedWriter writer = new BufferedWriter(new FileWriter(inputFile.getAbsolutePath() + ".tmp"));
            String line;
            int currentLineNumber = 1;
            boolean deleted = false;
            while ((line = reader.readLine()) != null) {
                if (currentLineNumber == lineNumber && !deleted) {
                    deleted = true;
                    currentLineNumber++;
                    continue;
                }
                writer.write(line);
                if (line.endsWith("\n")) {
                    // 如果该行以换行符结尾，说明该行原来是多行，我们需要保留该换行符，以便下一行能够正确连接上来
                    writer.newLine();
                } else {
                    // 如果该行不以换行符结尾，说明该行原来是单行，我们需要添加换行符，以便下一行能够正确连接上来
                    if (currentLineNumber != lineNumber && reader.ready()) {
                        // 只有当前行不是要删除的行，且不是文件最后一行，才需要添加换行符
                        writer.newLine();
                    }
                }
                currentLineNumber++;
            }
            reader.close();
            writer.close();
            if (!inputFile.delete()) {
                return false;
            }
            if (!new File(inputFile.getAbsolutePath() + ".tmp").renameTo(inputFile)) {
                return false;
            }
            return true;
        } catch (Exception e) {
//            throw new RuntimeException(e);
        }
        return false;

    }
    /**

     替换文件中的指定字符串为另一个字符串
     @param filePath 文件路径
     @param oldString 要替换的字符串
     @param newString 替换成的字符串
     @return 成功返回 true，失败返回 false
     @throws IOException 如果文件操作失败抛出 IOException
     */
    public static Boolean replaceString(String filePath, String oldString, String newString)  {
        File inputFile = new File(filePath);
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(inputFile));
            BufferedWriter writer = new BufferedWriter(new FileWriter(inputFile.getAbsolutePath() + ".tmp"));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains(oldString)) {
                    line = line.replace(oldString, newString);
                }
                writer.write(line);
                if (line.endsWith("\n")) {
                    writer.newLine();
                } else {
                    writer.newLine();
                }
            }
            reader.close();
            writer.close();
            if (!inputFile.delete()) {
                return false;
            }
            if (!new File(inputFile.getAbsolutePath() + ".tmp").renameTo(inputFile)) {
                return false;
            }
            return true;
        } catch (Exception e) {
//            throw new RuntimeException(e);
        }
        return false;

    }

    /**

     在给定文件中查找指定的文本，并返回第一次出现的行号。如果没有找到文本，则返回-1。
     @param filePath 要查找的文件路径
     @param searchText 要查找的文本
     @return 第一次出现指定文本的行号，如果没有找到返回-1
     @throws RuntimeException 如果发生任何I/O错误
     */
    public static int findLineNumber(String filePath, String searchText)  {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(filePath));
            String line;
            int lineNumber = 0;
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                if (line.contains(searchText)) {
                    reader.close();
                    return lineNumber;
                }
            }
            reader.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return -1; // 没有找到
    }

    public static InputStream getAssetFile(Context context, String filePath) {
        AssetManager assetManager = context.getAssets();
        try {
            return assetManager.open(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean saveInputStreamToFile(InputStream inputStream, String filePath) {
        try {
            OutputStream outputStream = new FileOutputStream(filePath);
            byte[] buffer = new byte[4096];
            int length;
            while ((length = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, length);
            }
            outputStream.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }


}