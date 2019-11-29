package com.dazhongmianfei.dzmfreader.utils;

import android.content.Context;
import android.os.Environment;

import com.dazhongmianfei.dzmfreader.comic.been.BaseComic;
import com.dazhongmianfei.dzmfreader.comic.been.BaseComicImage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.channels.FileChannel;

/**
 * 文件操作管理类
 */
public class FileManager {
    /**
     * 文件存储根目录
     */
    public static String FILEROOT;


    public static final void initialize(Context context) {
        FILEROOT = context.getFilesDir().getPath().concat("/");
    }

    /**
     * <p>
     * 判断SDCard是否存在。
     * </p>
     *
     * @return SD卡存在与否。
     */
    public static final boolean isSDCardExists() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * <p>
     * 获取SDCard路径。
     * </p>
     *
     * @return SD卡路径。
     */
    public static final String getSDCardRoot() {
        String root = "";
        if (isSDCardExists()) {
            root = Environment.getExternalStorageDirectory().toString() + "/dazhongmianfei/";
        }
        return root;
    }

    //漫画下载更目录
    public static final String getManhuaSDCardRoot() {

        return FileManager.getSDCardRoot() + "image/comic/";
    }

    //获取漫画图片
    public static final File getManhuaSDCardRootImg(BaseComicImage baseComicImage) {

        String ImgName = "";
        String localPath = getManhuaSDCardRoot().concat(baseComicImage.comic_id + "/").concat(baseComicImage.chapter_id + "/");
        if (baseComicImage.image.contains(".jpg")) {
            ImgName = baseComicImage.image_id + ".jpg";
        } else if (baseComicImage.image.contains(".jpeg")) {
            ImgName = baseComicImage.image_id + ".jpeg";
        } else if (baseComicImage.image.contains(".png")) {
            ImgName = baseComicImage.image_id + ".png";
        } else {
            return null;
        }

        String s = localPath.concat(ImgName);
        MyToash.Log("XXomicChapterRR", s);
        File file = new File(localPath.concat(ImgName));
        if (file.exists()) {
            MyToash.Log("XXomicChapterRR","true");
            return file;
        }
        MyToash.Log("XXomicChapterRR","false");
        return null;
    }

    //获取漫画图片
    public static final String getManhuaSDCardRootImgString(BaseComicImage baseComicImage) {

        String ImgName = "";
        String localPath = getManhuaSDCardRoot().concat(baseComicImage.comic_id + "/").concat(baseComicImage.chapter_id + "/").concat(baseComicImage.getUpdate_time() + "/");
        if (baseComicImage.image.contains(".jpg")) {
            ImgName = baseComicImage.image_id + ".jpg";
        } else if (baseComicImage.image.contains(".jpeg")) {
            ImgName = baseComicImage.image_id + ".jpeg";
        } else if (baseComicImage.image.contains(".png")) {
            ImgName = baseComicImage.image_id + ".png";
        } else {
            return null;
        }
        File file = new File(localPath.concat(ImgName));
        if (file.exists()) {

            return localPath.concat(ImgName);
        }

        return null;
    }

    /**
     * <p>
     * 读取文件。
     * </p>
     *
     * @param filePath 文件路径
     * @return 文件数据
     */
    public static final byte[] readFile(String filePath) {
        byte[] buffer = null;
        FileInputStream fis = null;
        try {
            File file = new File(filePath);
            if (file.exists()) {
                fis = new FileInputStream(file);
                int len = fis.available();
                buffer = new byte[len];
                fis.read(buffer);
            }
        } catch (Exception e) {
            Utils.printException(e);
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                }
            }
        }
        return buffer;
    }


    /**
     * <p>
     * 创建文件
     * </p>
     *
     * @param path   文件路径
     * @param buffer 比特流缓冲区
     */
    public static final File createFile(String path, byte[] buffer) {

        File file = new File(path);
        if (file.exists()) {
            return file;
        }
        FileOutputStream fstream = null;
        try {

            // 判断父目录是否存在
            if (!file.exists()) {
                File fileDir = file.getParentFile();
                fileDir.mkdirs();
            }
            file.createNewFile();
            fstream = new FileOutputStream(file);
            fstream.write(buffer);
        } catch (Exception e) {
            Utils.printException(e);
        } finally {
            if (fstream != null) {
                try {
                    fstream.close();
                } catch (IOException e) {
                    Utils.printException(e);
                }
            }
        }

        return file;
    }

    /**
     * <p>
     * 创建目录。
     * </p>
     *
     * @param path 目录
     */
    public static final void createPath(String path) {
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    /**
     * <p>
     * 保存文件，采用普通流方式保存。<br>
     * 如果是路径则创建（不存在时）；如果是文件则保存。
     * </p>
     *
     * @param filePath 文件路径
     * @param buffer   内容
     */
    public static final void saveFile(String filePath, byte[] buffer) {
        if (null == filePath || filePath.equals("")) {
            return;
        }
        if (buffer == null) {
            return;
        }

        if (filePath.endsWith("/")) {
            // 数据以/结尾（例：sygh/css/），表示是路径，此时应建立文件夹
            createPath(filePath);
        } else {
            createFile(filePath, buffer);
        }
    }


    /**
     * <p>
     * 删除文件或目录。
     * </p>
     *
     * @param filePath 文件路径
     * @return 删除成功返回true，否则返回false。
     */
    public static final boolean deleteFile(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            return false;
        }
        if (file.isFile()) {
            return file.delete();
        } else if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null && files.length > 0) {
                for (File tempF : files) {
                    deleteFile(tempF.getPath());
                }
            }
            // 删除file 本身
            return file.delete();
        }
        return true;
    }

    public static boolean isExist(String filePath) {
        File file = new File(filePath);
        return file.exists();
    }

    public static String txt2String(File file) {
        StringBuilder result = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));//构造一个BufferedReader类来读取文件
            String s = null;
            while ((s = br.readLine()) != null) {//使用readLine方法，一次读一行
                result.append(System.lineSeparator() + s);
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result.toString();
    }


    /**
     * 根据java.nio.*的流获取文件大小
     *
     * @param file
     */
    public static long getFileSize(File file) {
        FileChannel fc = null;
        try {
            if (file.exists() && file.isFile()) {
                String fileName = file.getName();
                FileInputStream fis = new FileInputStream(file);
                fc = fis.getChannel();

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != fc) {
                try {
                    fc.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        try {
            return fc.size();
        } catch (IOException e) {
            return 0;

        }
    }


    public static void GlideCopy(File source, File target) {
        FileInputStream fileInputStream = null;
        FileOutputStream fileOutputStream = null;
        try {
            fileInputStream = new FileInputStream(source);
            fileOutputStream = new FileOutputStream(target);
            byte[] buffer = new byte[1024];
            while (fileInputStream.read(buffer) > 0) {
                fileOutputStream.write(buffer);
            }

            MyToash.Log("XXomicChapter5   5", source.getAbsolutePath()+"  "+target.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fileInputStream.close();
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
