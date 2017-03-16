package ju.example.com.mapplication.util;

import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * 类名
 *
 * @author zhangchengju
 *         实现的主要功能:
 *         创建日期 2017/3/16
 *         修改者，修改日期，修改内容
 */
public class FileUtil {

    private static final String TAG = FileUtil.class.getSimpleName();
    //private static final String FILE_PATH = NewsApplication.mNewsApp.getFilesDir().getAbsolutePath();
    private static final String FILE_PATH = "/data/data/ecarx.news/files";


    /**
     * 文件的保存
     *
     * @param fileName 文件名。文件的路径已为/data/data/ecarx.news/files
     */
    public static void saveObject(String fileName, Object object) {
        ObjectOutputStream objectOutputStream = null;
        try {
            File file = new File(FILE_PATH + File.separator + fileName);
            if (file.exists()) {
                file.delete();
            }
            file.createNewFile();

            objectOutputStream = new ObjectOutputStream(new FileOutputStream(file));
            objectOutputStream.writeObject(object);
            objectOutputStream.flush();
        } catch (Exception e) {
            Log.e(TAG, "saveObject():" + e);
        } finally {
            if (objectOutputStream != null) {
                try {
                    objectOutputStream.close();
                } catch (IOException e) {
                    Log.e(TAG, "saveObject():" + e);
                }
            }
        }
    }


    /**
     * 文件的读取
     */
    public static Object readObject(String fileName) {
        Object object = null;
        ObjectInputStream objectInputStream = null;
        File file = null;

        try {
            file = new File(FILE_PATH);
            if (!file.exists()) {
                file.mkdir();
            }
            file = new File(FILE_PATH + File.separator + fileName);
            objectInputStream = new ObjectInputStream(new FileInputStream(file));
            object = objectInputStream.readObject();
        } catch (Exception e) {
            Log.e(TAG, "readObject():" + e);
        } finally {
            if (objectInputStream != null) {
                try {
                    objectInputStream.close();
                } catch (IOException e) {
                    Log.e(TAG, "readObject():" + e);
                }
            }
        }
        return object;
    }

    public static void clearObject(String fileName) {
        try {
            File file = new File(fileName);
            if (file.exists()) {
                file.delete();
                Log.d(TAG, "delete file success");
            }
        } catch (Exception e) {
            Log.e(TAG, " clearObject(): " + e);
        }

    }
}
