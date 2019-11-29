package com.dazhongmianfei.dzmfreader.read.util;

import android.content.Context;
import android.os.Environment;

import com.dazhongmianfei.dzmfreader.bean.ChapterItem;
import com.dazhongmianfei.dzmfreader.read.bean.Cache;
import com.dazhongmianfei.dzmfreader.utils.MyToash;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/8/11 0011.
 */
public class BookUtil {
    private static final String cachedPath = Environment.getExternalStorageDirectory() + "/dazhongmianfei/Treader/";
    //存储的字符数
    public static final int cachedSize = 30000;
//    protected final ArrayList<WeakReference<char[]>> myArray = new ArrayList<>();

    private String m_strCharsetName;

    protected final ArrayList<Cache> myArray = new ArrayList<>();
    private String bookName;
    private String chapterPath;
    private long bookLen;
    private long position;
    private ChapterItem chapter;
    private String book_id;
    private String chapter_id;


    private String next_m_strCharsetName;
    protected final ArrayList<Cache> next_myArray = new ArrayList<>();
    private String next_bookName;
    private String next_chapterPath;
    private long next_bookLen;
    private long next_position;
    private ChapterItem next_chapter;
    //    private String book_id;
    private String next_chapter_id;


    public BookUtil() {
        File file = new File(cachedPath);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    public synchronized void openBook(Context context, ChapterItem chapter, String book_id, String chapter_id) throws IOException {



        if(chapter==null){
            MyToash.ToashError(context,"当前章节不存在");
            return;
        }

        this.book_id = book_id;
        this.chapter_id = chapter_id;
        this.chapter = chapter;
        //如果当前缓存不是要打开的书本就缓存书本同时删除缓存

        if (chapterPath == null || !chapterPath.equals(chapter.getChapter_path())) {
            cleanCacheFile();
            this.chapterPath = chapter.getChapter_path();
            bookName = chapter.getBook_name();
            cacheBook();
        }
    }


//    public synchronized void loadChapter(ChapterItem chapter, String book_id, String chapter_id) throws IOException {
//        this.book_id = book_id;
//        this.chapter_id = chapter_id;
//        this.chapter = chapter;
//        //如果当前缓存不是要打开的书本就缓存书本同时删除缓存
//
//        if (chapterPath == null || !chapterPath.equals(chapter.getChapter_path())) {
//            cleanCacheFile();
//            this.chapterPath = chapter.getChapter_path();
//            bookName = chapter.getBook_name();
//            cacheBook();
//        }
//
//
//    }

    public List<String> getPreLines(PageFactory pageFactory) {
        List<String> lines = new ArrayList<>();
        float width = 0;
        String line = "";

        char[] par = preLine();
        while (par != null) {
            List<String> preLines = new ArrayList<>();
            for (int i = 0; i < par.length; i++) {
                char word = par[i];
                float widthChar = pageFactory.getmPaint().measureText(word + "");
                width += widthChar;
                if (width > pageFactory.getmVisibleWidth()) {
                    width = widthChar;
                    preLines.add(line);
                    line = word + "";
                } else {
                    line += word;
                }
            }
            if (!line.isEmpty()) {
                preLines.add(line);
            }

            lines.addAll(0, preLines);

            if (lines.size() >= pageFactory.getmLineCount()) {
                break;
            }
            width = 0;
            line = "";
            par = preLine();
        }

        List<String> reLines = new ArrayList<>();
        int num = 0;
        for (int i = lines.size() - 1; i >= 0; i--) {
            if (reLines.size() < pageFactory.getmLineCount()) {
                reLines.add(0, lines.get(i));
            } else {
                num = num + lines.get(i).length();
            }
        }

        if (num > 0) {
            if (getPosition() > 0) {
                setPostition(getPosition() + num + 2);
            } else {
                setPostition(getPosition() + num);
            }
        }

        return reLines;
    }


    public List<String> getNextLines1(PageFactory pageFactory, int flag) {
        int lineCount = 0;
        if (flag == 1) {
            lineCount = pageFactory.getmLineCount1();
        } else {
            lineCount = pageFactory.getmLineCount2();
        }
        List<String> lines = new ArrayList<>();
        float width = 0;
        float height = 0;
        String line = "";
        while (next(true) != -1) {
            char word = (char) next(false);
            //判断是否换行
            if ((word + "").equals("\r") && (((char) next(true)) + "").equals("\n")) {

                next(false);
                if (!line.isEmpty()) {
                    lines.add(line);
                    line = "";
                    width = 0;
//                    height +=  paragraphSpace;
                    if (lines.size() == lineCount) {
                        break;
                    }
                }

            } else {
                float widthChar = pageFactory.getmPaint().measureText(word + "");
                width += widthChar;
                if (width > pageFactory.getmVisibleWidth()) {
                    width = widthChar;
                    lines.add(line);
                    line = word + "";
                } else {
                    line += word;
                }
            }

            if (lines.size() == lineCount) {
                if (!line.isEmpty()) {
                    setPostition(getPosition() - 1);
                }
                break;
            }
        }

        if (!line.isEmpty() && lines.size() < lineCount) {
            lines.add(line);
        }
        return lines;
    }


    public List<String> getNextLines(PageFactory pageFactory) {

        List<String> lines = new ArrayList<>();
        float width = 0;
        float height = 0;
        String line = "";
        while (next(true) != -1) {
            char word = (char) next(false);
            //判断是否换行
            if ((word + "").equals("\r") && (((char) next(true)) + "").equals("\n")) {
                next(false);
                if (!line.isEmpty()) {
                    lines.add(line);
                    line = "";
                    width = 0;
//                    height +=  paragraphSpace;
                    if (lines.size() == pageFactory.getmLineCount()) {
                        break;
                    }
                }

            } else {
                float widthChar = pageFactory.getmPaint().measureText(word + "");
                width += widthChar;
                if (width > pageFactory.getmVisibleWidth()) {
                    width = widthChar;
                    lines.add(line);
                    line = word + "";
                } else {
                    line += word;
                }
            }

            if (lines.size() == pageFactory.getmLineCount()) {
                if (!line.isEmpty()) {
                    setPostition(getPosition() - 1);
                }
                break;
            }
        }

        if (!line.isEmpty() && lines.size() < pageFactory.getmLineCount()) {
            lines.add(line);
        }
        return lines;
    }

    private void cleanCacheFile() {
        File file = new File(cachedPath);
        if (!file.exists()) {
            file.mkdir();
        } else {
            File[] files = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                files[i].delete();
            }
        }
    }

    public int next(boolean back) {
        position += 1;
        if (position >= bookLen) {
            position = bookLen;
            return -1;
        }
        char result = current();
        if (back) {
            position -= 1;
        }

        return result;
    }

    public char[] nextLine() {
        if (position >= bookLen) {
            return null;
        }
        String line = "";
        while (position < bookLen) {
            int word = next(false);
            if (word == -1) {
                break;
            }
            char wordChar = (char) word;
            if ((wordChar + "").equals("\r") && (((char) next(true)) + "").equals("\n")) {
                next(false);
                break;
            }
            line += wordChar;
        }
        return line.toCharArray();
    }

    public char[] preLine() {
        if (position <= 0) {
            return null;
        }
        String line = "";
        while (position >= 0) {
            int word = pre(false);
            if (word == -1) {
                break;
            }
            char wordChar = (char) word;
            if ((wordChar + "").equals("\n") && (((char) pre(true)) + "").equals("\r")) {
                pre(false);
//                line = "\r\n" + line;
                break;
            }
            line = wordChar + line;
        }
        return line.toCharArray();
    }

    public char current() {

//        int pos = (int) (position % cachedSize);
//        int cachePos = (int) (position / cachedSize);
        int cachePos = 0;
        int pos = 0;
        int len = 0;
        for (int i = 0; i < myArray.size(); i++) {
            long size = myArray.get(i).getSize();
            if (size + len - 1 >= position) {
                cachePos = i;
                pos = (int) (position - len);
                break;
            }
            len += size;
        }

        char[] charArray = block(cachePos);
        if(charArray.length>pos){
            return charArray[pos];
        }
      return 0;
    }

    public char next_current() {
//        int pos = (int) (position % cachedSize);
//        int cachePos = (int) (position / cachedSize);
        int cachePos = 0;
        int pos = 0;
        int len = 0;
        for (int i = 0; i < next_myArray.size(); i++) {
            long size = next_myArray.get(i).getSize();
            if (size + len - 1 >= next_position) {
                cachePos = i;
                pos = (int) (next_position - len);
                break;
            }
            len += size;
        }

        char[] charArray = next_block(cachePos);
        return charArray[pos];
    }


    public int pre(boolean back) {
        position -= 1;
        if (position < 0) {
            position = 0;
            return -1;
        }
        char result = current();
        if (back) {
            position += 1;
        }
        return result;
    }

    public long getNext_bookLen() {
        return next_bookLen;
    }

    public void setNext_bookLen(long next_bookLen) {
        this.next_bookLen = next_bookLen;
    }

    public long getNext_position() {
        return next_position;
    }

    public void setNext_position(long next_position) {
        this.next_position = next_position;
    }

    public long getPosition() {
        return position;
    }

    public void setPostition(long position) {
        this.position = position;
    }

    //缓存书本
    private void cacheBook() throws IOException {
//        ContentValues values = new ContentValues();
//        values.put("charset", m_strCharsetName);
//        LitePal.updateAll(ChapterItem.class, values, "book_id = ? and chapter_id = ?", book_id, chapter_id);

        m_strCharsetName = chapter.getCharset();

        File file = new File(chapterPath);
        InputStreamReader reader = new InputStreamReader(new FileInputStream(file), m_strCharsetName);
        int index = 0;
        bookLen = 0;
        myArray.clear();
        while (true) {
            char[] buf = new char[cachedSize];
            int result = reader.read(buf);
            if (result == -1) {
                reader.close();
                break;
            }

            String bufStr = new String(buf);
            bufStr = bufStr.replaceAll("\n\\s*", "\r\n\u3000\u3000");
            bufStr = bufStr.replaceAll("\u0000", "");
            buf = bufStr.toCharArray();
            bookLen += buf.length;

            Cache cache = new Cache();
            cache.setSize(buf.length);
            cache.setData(new WeakReference<char[]>(buf));

            myArray.add(cache);

            try {
                File cacheBook = new File(fileName(index));
                if (!cacheBook.exists()) {
                    cacheBook.createNewFile();
                }
                final OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(fileName(index)), "UTF-16LE");
                writer.write(buf);
                writer.close();
            } catch (IOException e) {
              //  throw new RuntimeException("Error during writing " + fileName(index));
            }
            index++;
        }
    }

    public long getBookLen() {
        return bookLen;
    }

    /**
     * 返回当前章节对象
     *
     * @return
     */
    public ChapterItem getCurrentChapter() {
        return chapter;
    }

    protected String fileName(int index) {
        return cachedPath + bookName + index + ".txt";
    }

    //获取书本缓存
    public char[] block(int index) {
        if (myArray.size() == 0) {
            return new char[1];
        }
        char[] block = myArray.get(index).getData().get();
        if (block == null) {
            try {
                File file = new File(fileName(index));
                int size = (int) file.length();
                if (size < 0) {
                    throw new RuntimeException("Error during reading " + fileName(index));
                }
                block = new char[size / 2];
                InputStreamReader reader = new InputStreamReader(new FileInputStream(file), "UTF-16LE");
                if (reader.read(block) != block.length) {
                   // throw new RuntimeException("Error during reading " + fileName(index));
                }
                reader.close();
            } catch (IOException e) {
               // throw new RuntimeException("Error during reading " + fileName(index));
            }
            Cache cache = myArray.get(index);
            cache.setData(new WeakReference<char[]>(block));
//            myArray.set(index, new WeakReference<char[]>(block));
        }
        return block;
    }

    //获取书本缓存
    public char[] next_block(int index) {
        if (next_myArray.size() == 0) {
            return new char[1];
        }
        char[] block = next_myArray.get(index).getData().get();
        if (block == null) {
            try {
                File file = new File(fileName(index));
                int size = (int) file.length();
                if (size < 0) {
                    throw new RuntimeException("Error during reading " + fileName(index));
                }
                block = new char[size / 2];
                InputStreamReader reader =
                        new InputStreamReader(
                                new FileInputStream(file),
                                "UTF-16LE"
                        );
                if (reader.read(block) != block.length) {
                  //  throw new RuntimeException("Error during reading " + fileName(index));
                }
                reader.close();
            } catch (IOException e) {
                //throw new RuntimeException("Error during reading " + fileName(index));
            }
            Cache cache = next_myArray.get(index);
            cache.setData(new WeakReference<char[]>(block));
//            myArray.set(index, new WeakReference<char[]>(block));
        }
        return block;
    }

}
