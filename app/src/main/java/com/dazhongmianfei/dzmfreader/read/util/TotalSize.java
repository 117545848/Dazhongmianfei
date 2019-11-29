package com.dazhongmianfei.dzmfreader.read.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by scb on 2018/11/21.
 */

public class TotalSize {
    private TotalSize() {
    }

    private static TotalSize totalSize;
    private List<BookInt> pageSizeList = new ArrayList<>();

    public static TotalSize getTotalSize() {
        synchronized (TotalSize.class) {
            if (totalSize == null) {
                totalSize = new TotalSize();
            }
        }
        return totalSize;
    }

    public List<BookInt> getPageSizeList() {
        return pageSizeList;
    }

    public void setPageSizeList(List<BookInt> pageSizeList) {
        this.pageSizeList = pageSizeList;
    }

    public void addPageSizeList(BookInt bookInt) {

        if (pageSizeList == null) {
            pageSizeList = new ArrayList<>();
        }


        if (!pageSizeList.contains(bookInt)) {
            if (pageSizeList.size() == 0) {
                bookInt.beforN_size = 0;

            } else {
                if (pageSizeList != null) {
                    for (BookInt bookIntt : pageSizeList) {

                        // BeforSize+=bookInt.chapter_size;
                    }
                }
            }


            pageSizeList.add(bookInt);
            Collections.sort(pageSizeList, new Comparator<BookInt>() {
                @Override
                public int compare(BookInt bookInt, BookInt t1) {
                    if (bookInt.chapter_id <= t1.chapter_id) {//o1属性小于等于o2属性时，返回1
                        return 1;
                    } else {
                        return -1;
                    }
                }
            });

        } else {


        }


    }

    public int getBeforSize(int chapter_id) {
        int BeforSize = 0;
        if (pageSizeList != null) {
            for (BookInt bookInt : pageSizeList) {
                BeforSize += bookInt.chapter_size;
            }
        }
        return BeforSize;

    }

    public class BookInt {
        public int book_id;
        public int chapter_id;
        public int chapter_size;//本章大小
        public int beforN_size;//本章之前的 总大小

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {

                return false;

            }

            if (this == obj) {

                return true;

            }

            if (obj instanceof BookInt) {

                BookInt BookInt = (BookInt) obj;

                return BookInt.book_id == this.book_id && BookInt.chapter_id == this.chapter_id;

            }

            return false;


        }

        public BookInt(int book_id, int chapter_id, int chapter_size) {
            this.book_id = book_id;
            this.chapter_id = chapter_id;
            this.chapter_size = chapter_size;
        }
    }


    /**
     * 中文數字转阿拉伯数组【十万九千零六十  --> 109060】
     *
     * @param chineseNumber
     * @return
     * @author 雪见烟寒
     */
    @SuppressWarnings("unused")
    private static int chineseNumber2Int(String chineseNumber) {
        int result = 0;
        int temp = 1;//存放一个单位的数字如：十万
        int count = 0;//判断是否有chArr
        char[] cnArr = new char[]{'一', '二', '三', '四', '五', '六', '七', '八', '九'};
        char[] chArr = new char[]{'十', '百', '千', '万', '亿'};
        for (int i = 0; i < chineseNumber.length(); i++) {
            boolean b = true;//判断是否是chArr
            char c = chineseNumber.charAt(i);
            for (int j = 0; j < cnArr.length; j++) {//非单位，即数字
                if (c == cnArr[j]) {
                    if (0 != count) {//添加下一个单位之前，先把上一个单位值添加到结果中
                        result += temp;
                        temp = 1;
                        count = 0;
                    }
                    // 下标+1，就是对应的值
                    temp = j + 1;
                    b = false;
                    break;
                }
            }
            if (b) {//单位{'十','百','千','万','亿'}
                for (int j = 0; j < chArr.length; j++) {
                    if (c == chArr[j]) {
                        switch (j) {
                            case 0:
                                temp *= 10;
                                break;
                            case 1:
                                temp *= 100;
                                break;
                            case 2:
                                temp *= 1000;
                                break;
                            case 3:
                                temp *= 10000;
                                break;
                            case 4:
                                temp *= 100000000;
                                break;
                            default:
                                break;
                        }
                        count++;
                    }
                }
            }
            if (i == chineseNumber.length() - 1) {//遍历到最后一个字符
                result += temp;
            }
        }
        return result;
    }

    public static int getChapter_num(String Chapter_num) {
        try {
            return Integer.parseInt(Chapter_num);
        } catch (Exception e) {
            return chineseNumber2Int(Chapter_num);
        }


    }

    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        return isNum.matches();
    }
}
