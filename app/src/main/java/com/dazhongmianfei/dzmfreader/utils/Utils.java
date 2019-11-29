package com.dazhongmianfei.dzmfreader.utils;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.AnimationDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dazhongmianfei.dzmfreader.R;
import com.dazhongmianfei.dzmfreader.book.been.BaseBook;
import com.dazhongmianfei.dzmfreader.config.ReaderApplication;
import com.dazhongmianfei.dzmfreader.config.ReaderConfig;

import org.litepal.LitePal;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.UUID;

/**
 * 常用工具类
 */
public class Utils {

    public static final String TAG = Utils.class.getSimpleName();
    public static Dialog mDialog = null;

    /**
     * 获取程序版本号
     *
     * @param context
     * @return
     */
    public static String getAppVersionName(Context context) {
        String versionName = "";

        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            versionName = packageInfo.versionName;
            if (versionName == null || versionName.length() <= 0) {
                return "";
            }
        } catch (Exception e) {
            Utils.printException(e);
        }
        return versionName;
    }

    /**
     * md5加密
     *
     * @param params
     * @return
     */
    public static String MD5(String params) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(params.getBytes("utf-8"));
            StringBuffer buf = new StringBuffer();
            for (byte b : md.digest()) {
                buf.append(String.format("%02x", b & 0xff));
            }
            return buf.toString().toUpperCase();
        } catch (Exception e) {
            Utils.printException(e);
            e.printStackTrace();
            return null;
        }
    }

    /**
     * toast
     *
     * @param info
     */



    /**
     * 获取当前手机系统版本号
     *
     * @return 系统版本号
     */
    public static String getSystemVersion() {
        return Build.VERSION.RELEASE;
    }

    /**
     * 获取平台版本
     *
     * @return
     */
    public static String getOsType() {
        return "2";
    }

    /**
     * 获取产品线 1：app 2：公众号 3：小程序
     *
     * @return
     */
    public static String getProduct() {
        return "1";
    }

    /**
     * <p>
     * 获取设备UUID <br/>
     * 1）使用IMEI生成
     * 根据官方提供的API（IMEI获取），所有设备都会返回一个设备ID，获取到后根据附录中API：UUID.fromString(String
     * uuid)，将设备ID作为参数生成UUID作为设备指纹； <br/>
     * 2）使用设备MAC地址生成 当特殊情况下获取不到IMEI时，使用设备MAC地址生成。
     * 具体做法为获取设备MAC地址（MAC地址获取API）作为参数来生成UUID作为设备指纹；<br/>
     * 3）使用随机UUID 当IMEI和MAC地址都获取不到的情况下
     * ，根据UUID生成规则生成一个标识，根据API:UUID.randomUUID()生成一个随机UUID作为设备指纹。
     * </p>
     * 如果SharedPreferences里没有则生成并保存,有的话直接从SharedPreferences里取
     * 清除数据后，SharedPreferences里的uuid跟着没有了，需重新生成
     *
     * @return 设备UUID。
     */
    public static final String getUUID(Context context) {
        String ServiceUUID;
        SharedPreferences sharedPreferences = context.getSharedPreferences("uuid", Context.MODE_PRIVATE);
        if (sharedPreferences.getString("UUID", "").equals("")) {
            SharedPreferences.Editor shareEditor = sharedPreferences.edit();
            if (!Utils.getIMEI(context).equals("")) {
                byte[] name = Utils.getIMEI(context).getBytes();
                ServiceUUID = UUID.nameUUIDFromBytes(name).toString();
            } else {
                ServiceUUID = UUID.randomUUID().toString();
            }
            try {
                shareEditor.putString("UUID", ServiceUUID);
                shareEditor.commit();
            } catch (Exception e) {
                Utils.printException(e);
            }
        } else {
            ServiceUUID = sharedPreferences.getString("UUID", "");
        }
        return ServiceUUID;
    }

    /**
     * 获取用户token
     *
     * @param context
     * @return
     */
    public static String getToken(Context context) {
        String token = AppPrefs.getSharedString(context, ReaderConfig.TOKEN, "");
        printLog(TAG, "getToken " + token);
        return token;
    }

    /**
     * 获取用户的唯一标识
     *
     * @param context
     * @return
     */
    public static String getUID(Context context) {
        String uid = AppPrefs.getSharedString(context, ReaderConfig.UID, "");
        if (TextUtils.isEmpty(uid)) {
            uid = "none";
        }
        printLog(TAG, "getUID " + uid);
        return uid;
    }

    /**
     * 判断用户是否登录
     *
     * @return
     */
    public static boolean isLogin(Context context) {
        String token = AppPrefs.getSharedString(context, ReaderConfig.TOKEN, "");
        return !TextUtils.isEmpty(token);
    }

    /**
     * <p>
     * 获取设备IMEI。
     * </p>
     *
     * @return 设备IMEI
     */
    public static String getIMEI(Context context) {
        try {
            final TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            String imei = tm.getDeviceId();
            if (!TextUtils.isEmpty(imei)) {
                return imei;
            } else {
                return "";
//            return getMac(context);
            }
        } catch (Exception e) {
        }
        return "";
    }

    /**
     * <p>
     * 打印日志。
     * </p>
     *
     * @param tag        打印的标签
     * @param logContent 需要打印的字符串
     */
    public static void printLog(String tag, String logContent) {
      MyToash.Log(tag, logContent);
   /*     if (ReaderConfig.mIsPrintLog) {
            if (tag == null) {
                tag = "ReaderDebug";
            }
            if (logContent == null) {
                logContent = "null";
            }

            int length = logContent.length();
            int offset = 3000;
            if (length == 0) {
                System.out.println(tag + "：");
            } else if (length > offset) {
                // 解决报文过长，打印不全的问题
                int n = 0;
                for (int i = 0; i < length; i += offset) {
                    n += offset;
                    if (n > length) {
                        n = length;
                    }
                    Log.i(tag, logContent.substring(i, n));
                }
            } else {
                Log.i(tag, logContent);
            }
        }*/
    }


    /**
     * 获取macaddress
     *
     * @param context
     * @return
     */
    public static String getMac(Context context) {

        String strMac = null;

        if (Build.VERSION.SDK_INT < 23) {
            Utils.printLog("getMacAddress=====", "6.0以下");
            strMac = getLocalMacAddressFromWifiInfo(context);
            return strMac;
        } else if (Build.VERSION.SDK_INT < 24 && Build.VERSION.SDK_INT >= 23) {
            Utils.printLog("getMacAddress=====", "6.0以上7.0以下");
            strMac = getMacAddress(context);
            return strMac;
        } else if (Build.VERSION.SDK_INT >= 24) {
            Utils.printLog("getMacAddress=====", "7.0以上");
            if (!TextUtils.isEmpty(getMacAddress(context))) {
                Utils.printLog("getMacAddress=====", "7.0以上1");
                strMac = getMacAddress(context);
                return strMac;
            } else if (!TextUtils.isEmpty(getMachineHardwareAddress())) {
                Utils.printLog("getMacAddress=====", "7.0以上2");
                strMac = getMachineHardwareAddress();
                return strMac;
            } else {
                Utils.printLog("getMacAddress=====", "7.0以上3");
                strMac = getLocalMacAddressFromBusybox();
                return strMac;
            }
        }

        return "02:00:00:00:00:00";
    }

    /**
     * 根据wifi信息获取本地mac
     *
     * @param context
     * @return
     */
    public static String getLocalMacAddressFromWifiInfo(Context context) {
        WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo winfo = wifi.getConnectionInfo();
        String mac = winfo.getMacAddress();
        return mac;
    }

    /**
     * android 6.0及以上、7.0以下 获取mac地址
     *
     * @param context
     * @return
     */
    public static String getMacAddress(Context context) {

        String str = "";
        String macSerial = "";
        try {
            Process pp = Runtime.getRuntime().exec("cat /sys/class/net/wlan0/address");
            InputStreamReader ir = new InputStreamReader(pp.getInputStream());
            LineNumberReader input = new LineNumberReader(ir);

            for (; null != str; ) {
                str = input.readLine();
                if (str != null) {
                    macSerial = str.trim();// 去空格
                    break;
                }
            }
        } catch (Exception ex) {
            Utils.printException(ex);
        }
        if (macSerial == null || "".equals(macSerial)) {
            try {
                return loadFileAsString("/sys/class/net/eth0/address").toUpperCase().substring(0, 17);
            } catch (Exception e) {
                e.printStackTrace();
                Utils.printException(e);
            }

        }
        return macSerial;
    }

    /**
     * Check whether accessing wifi state is permitted
     *
     * @param context
     * @return
     */
    private static boolean isAccessWifiStateAuthorized(Context context) {
        if (PackageManager.PERMISSION_GRANTED == context.checkCallingOrSelfPermission("android.permission.ACCESS_WIFI_STATE")) {
            Utils.printLog("----->" + "NetInfoManager", "isAccessWifiStateAuthorized:"
                    + "access wifi state is enabled");
            return true;
        } else
            return false;
    }

    private static String loadFileAsString(String fileName) throws Exception {
        FileReader reader = new FileReader(fileName);
        String text = loadReaderAsString(reader);
        reader.close();
        return text;
    }

    private static String loadReaderAsString(Reader reader) throws Exception {
        StringBuilder builder = new StringBuilder();
        char[] buffer = new char[4096];
        int readLength = reader.read(buffer);
        while (readLength >= 0) {
            builder.append(buffer, 0, readLength);
            readLength = reader.read(buffer);
        }
        return builder.toString();
    }


    /**
     * 根据IP地址获取MAC地址
     *
     * @return
     */
    public static String getMacAddress() {
        String strMacAddr = null;
        try {
            // 获得IpD地址
            InetAddress ip = getLocalInetAddress();
            byte[] b = NetworkInterface.getByInetAddress(ip)
                    .getHardwareAddress();
            StringBuffer buffer = new StringBuffer();
            for (int i = 0; i < b.length; i++) {
                if (i != 0) {
                    buffer.append(':');
                }
                String str = Integer.toHexString(b[i] & 0xFF);
                buffer.append(str.length() == 1 ? 0 + str : str);
            }
            strMacAddr = buffer.toString().toUpperCase();
        } catch (Exception e) {

        }

        return strMacAddr;
    }

    /**
     * 获取移动设备本地IP
     *
     * @return
     */
    private static InetAddress getLocalInetAddress() {
        InetAddress ip = null;
        try {
            // 列举
            Enumeration<NetworkInterface> en_netInterface = NetworkInterface.getNetworkInterfaces();
            while (en_netInterface.hasMoreElements()) {// 是否还有元素
                NetworkInterface ni = en_netInterface
                        .nextElement();// 得到下一个元素
                Enumeration<InetAddress> en_ip = ni.getInetAddresses();// 得到一个ip地址的列举
                while (en_ip.hasMoreElements()) {
                    ip = en_ip.nextElement();
                    if (!ip.isLoopbackAddress()
                            && ip.getHostAddress().indexOf(":") == -1)
                        break;
                    else
                        ip = null;
                }

                if (ip != null) {
                    break;
                }
            }
        } catch (SocketException e) {

            e.printStackTrace();
        }
        return ip;
    }

    /**
     * 获取本地IP
     *
     * @return
     */
    private static String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface
                    .getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf
                        .getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (SocketException ex) {
            ex.printStackTrace();
        }
        return null;
    }


    /**
     * android 7.0及以上 （2）扫描各个网络接口获取mac地址
     *
     */
    /**
     * 获取设备HardwareAddress地址
     *
     * @return
     */
    public static String getMachineHardwareAddress() {
        Enumeration<NetworkInterface> interfaces = null;
        try {
            interfaces = NetworkInterface.getNetworkInterfaces();
        } catch (SocketException e) {
            e.printStackTrace();
        }
        String hardWareAddress = null;
        NetworkInterface iF = null;
        if (interfaces == null) {
            return null;
        }
        while (interfaces.hasMoreElements()) {
            iF = interfaces.nextElement();
            try {
                hardWareAddress = bytesToString(iF.getHardwareAddress());
                if (hardWareAddress != null)
                    break;
            } catch (SocketException e) {
                e.printStackTrace();
            }
        }
        return hardWareAddress;
    }

    /***
     * byte转为String
     *
     * @param bytes
     * @return
     */
    private static String bytesToString(byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        StringBuilder buf = new StringBuilder();
        for (byte b : bytes) {
            buf.append(String.format("%02X:", b));
        }
        if (buf.length() > 0) {
            buf.deleteCharAt(buf.length() - 1);
        }
        return buf.toString();
    }

/**
 * android 7.0及以上 （3）通过busybox获取本地存储的mac地址
 *
 */

    /**
     * 根据busybox获取本地Mac
     *
     * @return
     */
    public static String getLocalMacAddressFromBusybox() {
        String result = "";
        String Mac = "";
        result = callCmd("busybox ifconfig", "HWaddr");
        // 如果返回的result == null，则说明网络不可取
        if (result == null) {
            return "网络异常";
        }
        // 对该行数据进行解析
        // 例如：eth0 Link encap:Ethernet HWaddr 00:16:E8:3E:DF:67
        if (result.length() > 0 && result.contains("HWaddr") == true) {
            Mac = result.substring(result.indexOf("HWaddr") + 6,
                    result.length() - 1);
            result = Mac;
        }
        return result;
    }

    private static String callCmd(String cmd, String filter) {
        String result = "";
        String line = "";
        try {
            Process proc = Runtime.getRuntime().exec(cmd);
            InputStreamReader is = new InputStreamReader(proc.getInputStream());
            BufferedReader br = new BufferedReader(is);

            while ((line = br.readLine()) != null
                    && line.contains(filter) == false) {
                result += line;
            }

            result = line;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * <p>
     * 打印异常信息。
     * </p>
     *
     * @param e Exception。
     */
    public static final void printException(Throwable e) {
        Log.e("ReaderAppError", "", e);

        FileWriter writer = null;
        try {
            // 保存到本地文件
            SimpleDateFormat baseDateFormat = new SimpleDateFormat("yyyy_MM_dd");
            String dateStr = baseDateFormat.format(new Date()); //  日志按day区分

            String mFilePath = FileManager.getSDCardRoot().concat(ReaderConfig.newInstance().getLocalLogDir()).concat("errorlog/android_errorlog_").concat(dateStr).concat(".log");
            File file = new File(mFilePath);
            if (!file.exists()) {
                // 判断父目录是否存在
                File fileDir = file.getParentFile();
                fileDir.mkdirs();
                file.createNewFile();
            }
            // 打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件
            writer = new FileWriter(mFilePath, true);
            PrintWriter pw = new PrintWriter(writer);
            e.printStackTrace(pw);
            writer.write("\n");
        } catch (Exception e1) {
            Log.e("ReaderError", "", e1);
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
                writer = null;
            } catch (IOException e2) {
                Log.e("ReaderError", "", e2);
            }
        }
    }

    /**
     * dp转成px
     *
     * @param dp
     * @return
     */


    /**
     * 数据加载
     *
     * @param act
     * @return
     */
    public static Dialog showProgressBar(Context act) {

        return showProgressBar(act, "正在加载");
    }

    /**
     * 数据加载
     *
     * @param act
     * @return
     */
    public static Dialog showProgressBar(Context act, String msg) {

        return showProgressBar(act, msg, true);
    }

    public static Dialog showProgressBar(Context context, String msg, final boolean canCacel) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.loading_dialog, null);// 得到加载view
        TextView msgView = v.findViewById(R.id.progress_msg);
        msgView.setText(msg);
        // main.xml中的ImageView
        ImageView spaceshipImage = v.findViewById(R.id.progress_drawable);

        AnimationDrawable anim = (AnimationDrawable) spaceshipImage.getDrawable();
        anim.start();

        Dialog loadingDialog = new Dialog(context, R.style.loading_dialog);// 创建自定义样式dialog

        loadingDialog.setCancelable(canCacel);// 不可以用“返回键”取消
        loadingDialog.setContentView(v, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));// 设置布局
        loadingDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                if (keyEvent.getKeyCode() == KeyEvent.KEYCODE_BACK)
                    if (canCacel)
                        dialogInterface.dismiss();
                return true;
            }
        });
        return loadingDialog;
    }

    public static void showLoadingDialog(Context context) {
        try {
            if (mDialog == null) {
                mDialog = showProgressBar(context);
                mDialog.show();
                Log.i(TAG + "---monitor", "---showdialog--3");
            } else if (!mDialog.isShowing()) {
                mDialog.show();
                Log.i(TAG + "---monitor", "---showdialog--4");
            }
        } catch (Exception e) {
        }
    }

    public static void showLoadingDialog(Context context, String msg) {
        try {
            if (mDialog == null) {
                mDialog = showProgressBar(context, msg);
                mDialog.show();
                Log.i(TAG + "---monitor", "---showdialog--1");
            } else if (!mDialog.isShowing()) {
                mDialog.show();
                Log.i(TAG + "---monitor", "---showdialog--2");
            }
        } catch (Exception e) {
        }
    }

    public static void hideLoadingDialog() {
        try {
            if (mDialog != null && mDialog.isShowing()) {
                Log.i(TAG + "---monitor", "---hideloading");
                mDialog.cancel();
                mDialog = null;
            }
        } catch (Exception e) {
        }
    }





    /**
     * 检测网络是否可用
     * <p/>
     * 同步方法，支持多线程
     */
    public static synchronized NetType checkNet(Context context) {
        NetType netType = NetType.TYPE_NONE;
        try {
            // 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
            ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivity != null) {
                // 获取网络连接管理的对象
                NetworkInfo info = connectivity.getActiveNetworkInfo();
                // 判断当前网络是否已经连接
                if (info != null && info.isConnected() && info.getState() == NetworkInfo.State.CONNECTED) {

                    // 判断当前的接入点
                    if (ConnectivityManager.TYPE_WIFI == info.getType()) {// wifi连接
                        netType = NetType.TYPE_WIFI;
                    } else if (ConnectivityManager.TYPE_MOBILE == info.getType()) {// 手机方式连接
                        /**
                         * 获取网络类型
                         *
                         * NETWORK_TYPE_CDMA 网络类型为CDMA NETWORK_TYPE_EDGE
                         * 网络类型为EDGE NETWORK_TYPE_EVDO_0 网络类型为EVDO0
                         * NETWORK_TYPE_EVDO_A 网络类型为EVDOA NETWORK_TYPE_GPRS
                         * 网络类型为GPRS NETWORK_TYPE_HSDPA 网络类型为HSDPA
                         * NETWORK_TYPE_HSPA 网络类型为HSPA NETWORK_TYPE_HSUPA
                         * 网络类型为HSUPA NETWORK_TYPE_UMTS 网络类型为UMTS
                         *
                         * 在中国，联通的3G为UMTS或HSDPA，移动和联通的2G为GPRS或EGDE，电信的2G为CDMA，
                         * 电信的3G为EVDO
                         */

                        if (TelephonyManager.NETWORK_TYPE_GPRS == info.getSubtype() || TelephonyManager.NETWORK_TYPE_IDEN == info.getSubtype()
                                || TelephonyManager.NETWORK_TYPE_EDGE == info.getSubtype() || TelephonyManager.NETWORK_TYPE_CDMA == info.getSubtype()
                                || TelephonyManager.NETWORK_TYPE_1xRTT == info.getSubtype()) {
                            netType = NetType.TYPE_2G;
                        } else if (TelephonyManager.NETWORK_TYPE_EVDO_0 == info.getSubtype() || TelephonyManager.NETWORK_TYPE_EVDO_A == info.getSubtype()
                                || TelephonyManager.NETWORK_TYPE_EVDO_B == info.getSubtype() || TelephonyManager.NETWORK_TYPE_UMTS == info.getSubtype()
                                || TelephonyManager.NETWORK_TYPE_HSDPA == info.getSubtype() || TelephonyManager.NETWORK_TYPE_HSUPA == info.getSubtype()
                                || TelephonyManager.NETWORK_TYPE_HSPA == info.getSubtype() || TelephonyManager.NETWORK_TYPE_EHRPD == info.getSubtype()
                                || TelephonyManager.NETWORK_TYPE_HSPAP == info.getSubtype()) {// 3G
                            netType = NetType.TYPE_3G;
                        } else if (TelephonyManager.NETWORK_TYPE_LTE == info.getSubtype()) {
                            netType = NetType.TYPE_4G;
                        }
                    } else {// 其它未知连接方式
                        netType = NetType.TYPE_UNKNOWN;
                    }
                    Utils.printLog(TAG, "当前网络类型|" + netType.getDesc() + "|" + info.getType() + "|" + info.getSubtype());
                }
            }

        } catch (Exception e) {
            Utils.printException(e);
        }
        return netType;
    }

    /**
     * 判断书籍是否在书架
     *
     * @param book_id
     * @return
     */
    public static boolean[] isBookInShelf(Context context, String book_id) {

        List<BaseBook> bookList = LitePal.where("book_id = ?", book_id).find(BaseBook.class);

        if (bookList.size() == 0) {
            return new boolean[]{false, false};
        } else if (bookList.get(0).getUid().equals(Utils.getUID(context))) {
            return new boolean[]{true, false};
        } else if (bookList.get(0).getUid().equals("temp")) {
            return new boolean[]{false, true};
        }
        return new boolean[]{false, false};
    }
/*
    public static boolean isBookInShelfNodetleteTemp(Context context, String book_id) {

        List<BaseBook> bookList = LitePal.where("book_id = ? and uid =?", book_id, Utils.getUID(context)).find(BaseBook.class);
        return bookList.size() != 0;
    }*/
}
