package magichands.core.tool;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.lang.reflect.Field;

import dalvik.system.DexClassLoader;

public class LoadDexTool {

    public static void load(Context context, String Path){
        try {
            Class<?> dexPathListClass= Class.forName("dalvik.system.DexPathList");
            Field dexElementsField = dexPathListClass.getDeclaredField("dexElements");
            dexElementsField.setAccessible(true);
            Class<?> classLoaderClass = Class.forName("dalvik.system.BaseDexClassLoader");
            Field pathListFied = classLoaderClass.getDeclaredField("pathList");
            pathListFied.setAccessible(true);

            //获取host类加载器
            ClassLoader pathClassLoader = context.getClassLoader();
            Object hostPathList = pathListFied.get(pathClassLoader);
            //获取host DexElemnts对象
            Object[] hostDexElemnts = (Object[]) dexElementsField.get(hostPathList);

            //获取plugin类加载器

            ClassLoader pluginClassLoader = new DexClassLoader(Path,context.getCacheDir().getAbsolutePath(),null,pathClassLoader);
            Object pluginPathList = pathListFied.get(pluginClassLoader);
            //获取plugin DexElemnts对象
            Object[] pluginDexElemnts = (Object[]) dexElementsField.get(pluginPathList);

            //合并

            Object[] newElements = (Object[]) Array.newInstance(hostDexElemnts.getClass().getComponentType(), hostDexElemnts.length + pluginDexElemnts.length);

            System.arraycopy(hostDexElemnts,0,newElements,0,hostDexElemnts.length);
            System.arraycopy(pluginDexElemnts,0,newElements,hostDexElemnts.length,pluginDexElemnts.length);

            //赋值host de
            dexElementsField.set(hostPathList,newElements);
            Log.d("TAG", "=====: ");
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("TAG", "=====: "+e);
        }


    }



    public static Class<?> load(String name,String pkg,Context c) {

        // getDir("dex1", 0)会在/data/data/**package/下创建一个名叫”app_dex1“的文件夹，其内存放的文件是自动生成output.dex
        File OutputDir = getCacheDir(c.getApplicationContext());
        String dexPath = name;
        System.out.println("路径"+dexPath);
        File desFile=new File(dexPath);
        try {
            if (!desFile.exists()) {
                desFile.createNewFile();
                copyFiles(c,"plugins/"+name,desFile);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        /**
         * 参数1 dexPath：待加载的dex文件路径，如果是外存路径，一定要加上读外存文件的权限
         * 参数2 optimizedDirectory：解压后的dex存放位置，此位置一定要是可读写且仅该应用可读写（安全性考虑），所以只能放在data/data下。
         * 参数3 libraryPath：指向包含本地库(so)的文件夹路径，可以设为null
         * 参数4 parent：父级类加载器，一般可以通过Context.getClassLoader获取到，也可以通过ClassLoader.getSystemClassLoader()取到。
         */
        DexClassLoader classLoader = new DexClassLoader(dexPath, OutputDir.getAbsolutePath(),null,c.getClassLoader());
        Log.d("TAG111", String.valueOf(classLoader));

        Class libProviderClazz = null;
        try{
            libProviderClazz=classLoader.loadClass(pkg);
            Log.d(TAG, String.valueOf(libProviderClazz));
            return libProviderClazz;
        }catch(Exception exception){
            exception.printStackTrace();
        }
        return libProviderClazz;

    }

    public static void copyFiles(Context context, String fileName, File desFile) {
        InputStream in = null;
        OutputStream out = null;
        try {
            in = context.getApplicationContext().getAssets().open(fileName);
            out = new FileOutputStream(desFile.getAbsolutePath());
            byte[] bytes = new byte[1024];
            int i;
            while ((i = in.read(bytes)) != -1)
                out.write(bytes, 0 , i);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if (in != null)
                    in.close();
                if (out != null)
                    out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public static boolean hasExternalStorage() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * 获取缓存路径
     *
     * @param context
     * @return 返回缓存文件路径
     */
    public static File getCacheDir(Context context) {
        File cache;
        if (hasExternalStorage()) {
            cache = context.getExternalCacheDir();
        } else {
            cache = context.getCacheDir();
        }
        if (!cache.exists())
            cache.mkdirs();
        return cache;
    }

    public static void copyAssetsFolderToSdCard(Context context, String assetsFolderName, String sdCardFolderPath) throws IOException {
        AssetManager assetManager = context.getAssets();
        String[] files = assetManager.list(assetsFolderName);
        if (files != null) {
            // 创建目标文件夹
            File sdCardFolder = new File(sdCardFolderPath);
            if (!sdCardFolder.exists()) {
                sdCardFolder.mkdirs();
            }

            for (String fileName : files) {
                String assetsFilePath = assetsFolderName + File.separator + fileName;
                String sdCardFilePath = sdCardFolderPath + File.separator + fileName;

                if (isDirectory(assetManager, assetsFilePath)) {
                    // 如果是文件夹，则递归复制文件夹及其子文件夹
                    copyAssetsFolderToSdCard(context, assetsFilePath, sdCardFilePath);
                } else {
                    // 如果是文件，则直接复制文件
                    copyAssetFileToSdCard(context, assetsFilePath, sdCardFilePath);
                }
            }
        }
    }

    private static boolean isDirectory(AssetManager assetManager, String path) {
        try {
            // 尝试打开文件，如果报错则表示是文件夹
            InputStream inputStream = assetManager.open(path);
            inputStream.close();
            return false;
        } catch (IOException e) {
            return true;
        }
    }

    private static void copyAssetFileToSdCard(Context context, String assetsFilePath, String sdCardFilePath) throws IOException {
        InputStream inputStream = context.getAssets().open(assetsFilePath);
        OutputStream outputStream = new FileOutputStream(sdCardFilePath);
        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) > 0) {
            outputStream.write(buffer, 0, length);
        }
        outputStream.flush();
        outputStream.close();
        inputStream.close();
    }

}
