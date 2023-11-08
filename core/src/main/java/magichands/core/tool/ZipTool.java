package magichands.core.tool;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;

import java.io.File;

public class ZipTool {
    /**
     * 使用zip4j库压缩一个文件或者文件夹
     * @param srcFilePath 需要压缩的文件或者文件夹的路径
     * @param destFilePath 压缩后的zip文件的路径
     * @param password 加密密码，如果不加密可以为null
     * @throws ZipException
     */
    public static void compress(String srcFilePath, String destFilePath, String password) throws ZipException {
        ZipParameters parameters = new ZipParameters();
        parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
        parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);
        if (password != null) {
            parameters.setEncryptFiles(true);
            parameters.setEncryptionMethod(Zip4jConstants.ENC_METHOD_STANDARD);
            parameters.setPassword(password);
        }
        ZipFile zipFile = new ZipFile(destFilePath);
        File file = new File(srcFilePath);
        if (file.isDirectory()) {
            zipFile.addFolder(file, parameters);
        } else if (file.isFile()) {
            zipFile.addFile(file, parameters);
        }
    }
    /**
     * 使用zip4j库解压一个文件或者文件夹
     * @param zipFilePath 需要解压的zip文件的路径
     * @param destFilePath 解压后的文件或者文件夹的路径
     * @param password 解压密码，如果不加密可以为null
     * @throws ZipException
     */
    public static void decompress(String zipFilePath, String destFilePath, String password) throws ZipException {
        ZipFile zipFile = new ZipFile(zipFilePath);
        if (zipFile.isEncrypted() && password != null) {
            zipFile.setPassword(password);
        }
        zipFile.extractAll(destFilePath);
    }

}
