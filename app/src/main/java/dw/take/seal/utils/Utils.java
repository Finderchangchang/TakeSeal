package dw.take.seal.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.util.Base64;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Administrator on 2016/8/1.
 */
public class Utils {
    Context context;

    public Utils(Context context) {
        this.context = context;
    }

    static long myMax = 1099511627775L;

    //时加分
    public static String GetNOWTIMEPASSWORD() {
        String psw = "HBGD";
        String time = getNOWTime().substring(12);
        System.out.println("nowtiem:" + time);
        String shi = time.substring(0, 2);
        String fen = time.substring(3, 5);
        int num1 = Integer.parseInt(shi.substring(0, 1)) + Integer.parseInt(fen.substring(0, 1));
        int num2 = Integer.parseInt(shi.substring(1)) + Integer.parseInt(fen.substring(1));
        if (num1 >= 10) {
            num1 = num1 - 10;
        }
        if (num2 >= 10) {
            num2 -= 10;
        }
        return psw + num1 + num2;
    }

    /**
     * 获取版本号
     *
     * @return 当前应用的版本号
     */
    public String getVersion() {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            String version = info.versionName;
            return version;
        } catch (Exception e) {
            e.printStackTrace();
            return "获取版本号失败";
        }
    }

    /**
     * 比较两个日期之间的大小
     *
     * @param s1
     * @param s2
     * @return 前者大于后者返回true 反之false
     */
    public static boolean compareDate(String s1, String s2) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date d1 = sdf.parse(s1);
            Date d2 = sdf.parse(s2);
            Calendar f1 = Calendar.getInstance();
            Calendar f2 = Calendar.getInstance();
            f1.setTime(d1);
            f2.setTime(d2);
            int result = f1.compareTo(f2);
            if (result > 0)
                return true;
            else
                return false;
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }

    }

    /**
     * 获取描述码
     */
    public static String getMYImei(String imei) {
        String ts = "Firework Terminal Code:" + imei;

        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            digest.update(ts.getBytes());
            byte messageDigest[] = digest.digest();
            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            // 字节数组转换为 十六进制 数
            for (int i = 0; i < messageDigest.length; i++) {
                String shaHex = Integer.toHexString(messageDigest[i] & 0xFF);
                if (shaHex.length() < 2) {
                    hexString.append(0);
                }
                hexString.append(shaHex);
            }
            String sh1 = hexString.toString().toUpperCase();

            long myhash = 0L;
            char[] myChar = sh1.toCharArray();
            for (int i = 0; i < myChar.length; i++) {
                int myCHar = (myChar[i]);
                myhash = myhash * 33 + myCHar;

            }
            myhash = myMax & myhash;
            byte[] my8byte = new byte[8];
            for (int i = 0; i < my8byte.length; i++) {
                my8byte[i] = new Long(myhash & 0xff).byteValue();
                myhash = myhash >> 8;
            }
            String myResult = bytesToHexString(my8byte);
            myResult = myResult.toUpperCase().substring(0, myResult.length() - 6);
            String strCode = myResult.substring(0, 2) + "-" + myResult.substring(2, 4) + "-" + myResult.substring(4, 6) + "-" + myResult.substring(6, 8) + "-" + myResult.substring(8, 10);
            System.out.println("myResult:" + strCode);
            return strCode;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }
    }

    //将终端描述符转换为终端数值，生成授权码(终端描述符)（返回终端授权码)
    public static String getTerminalNumber(String terminalCode) {
        String temp = terminalCode.replace("-", "").toUpperCase() + "000000";
        byte[] re = hexStringToByte(temp);
        long my = byteToLong(re);
        //My269908102384
        if (my < 0 || my > myMax) {
            return "";
        } else {
            String ts = "" + my;

            try {
                MessageDigest digest = MessageDigest.getInstance("SHA-1");
                digest.update(ts.getBytes());
                byte messageDigest[] = digest.digest();
                // Create Hex String
                StringBuffer hexString = new StringBuffer();
                // 字节数组转换为 十六进制 数
                for (int i = 0; i < messageDigest.length; i++) {
                    String shaHex = Integer.toHexString(messageDigest[i] & 0xFF);
                    if (shaHex.length() < 2) {
                        hexString.append(0);
                    }
                    hexString.append(shaHex);
                }
                String sh1 = hexString.toString().toUpperCase();

                long myhash = 0L;
                char[] myChar = sh1.toCharArray();
                for (int i = 0; i < myChar.length; i++) {
                    int myCHar = (myChar[i]);
                    myhash = myhash * 33 + myCHar;

                }
                myhash = myMax & myhash;
                byte[] my8byte = new byte[8];
                for (int i = 0; i < my8byte.length; i++) {
                    my8byte[i] = new Long(myhash & 0xff).byteValue();
                    myhash = myhash >> 8;
                }
                String myResult = bytesToHexString(my8byte);
                myResult = myResult.toUpperCase().substring(0, myResult.length() - 6);
                String strCode = myResult.substring(0, 2) + "-" + myResult.substring(2, 4) + "-" + myResult.substring(4, 6) + "-" + myResult.substring(6, 8) + "-" + myResult.substring(8, 10);
                return strCode;
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
                return "";
            }
        }
    }

    /**
     * byte[]数组转换为16进制的字符串
     *
     * @param bytes 要转换的字节数组
     * @return 转换后的结果
     */
    private static String bytesToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }

    /**
     * 16进制字符串转换为byte[]
     *
     * @param hexString
     * @return
     */
    public static byte[] hexStringToBytes(String hexString) {
        if (hexString == null || hexString.equals("")) {
            return null;
        }
        hexString = hexString.toUpperCase().replace(" ", "");
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
        }
        return d;
    }

    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }

    /*
    * 把16进制字符串转换成字节数组
    * @param hex
    * @return
            */
    public static byte[] hexStringToByte(String hex) {
        int len = (hex.length() / 2);
        byte[] result = new byte[len];
        char[] achar = hex.toCharArray();
        for (int i = 0; i < len; i++) {
            int pos = i * 2;
            result[i] = (byte) (toByte(achar[pos]) << 4 | toByte(achar[pos + 1]));
        }
        return result;
    }

    private static byte toByte(char c) {
        byte b = (byte) "0123456789ABCDEF".indexOf(c);
        return b;
    }

    /**
     * 将64位的long值放到8字节的byte数组
     *
     * @param num
     * @return 返回转换后的byte数组
     */
    public static byte[] longToByteArray(long num) {
        byte[] result = new byte[8];
        result[0] = (byte) (num >>> 56);// 取最高8位放到0下标
        result[1] = (byte) (num >>> 48);// 取最高8位放到0下标
        result[2] = (byte) (num >>> 40);// 取最高8位放到0下标
        result[3] = (byte) (num >>> 32);// 取最高8位放到0下标
        result[4] = (byte) (num >>> 24);// 取最高8位放到0下标
        result[5] = (byte) (num >>> 16);// 取次高8为放到1下标
        result[6] = (byte) (num >>> 8); // 取次低8位放到2下标
        result[7] = (byte) (num); // 取最低8位放到3下标

        return result;
    }

    /**
     * 将8字节的byte数组转成一个long值
     *
     * @param byteArray
     * @return 转换后的long型数值
     */
    public static void byteArrayToInt(byte[] byteArray) {
        byte[] a = new byte[8];
        int i = a.length - 1, j = byteArray.length - 1;
        for (; i >= 0; i--, j--) {// 从b的尾部(即int值的低位)开始copy数据
            if (j >= 0)
                a[i] = byteArray[j];
            else
                a[i] = 0;// 如果b.length不足4,则将高位补0
        }
        // 注意此处和byte数组转换成int的区别在于，下面的转换中要将先将数组中的元素转换成long型再做移位操作，
        // 若直接做位移操作将得不到正确结果，因为Java默认操作数字时，若不加声明会将数字作为int型来对待，此处必须注意。
        long v0 = (long) (a[0] & 0xff) << 56;// &0xff将byte值无差异转成int,避免Java自动类型提升后,会保留高位的符号位
        long v1 = (long) (a[1] & 0xff) << 48;
        long v2 = (long) (a[2] & 0xff) << 40;
        long v3 = (long) (a[3] & 0xff) << 32;
        long v4 = (long) (a[4] & 0xff) << 24;
        long v5 = (long) (a[5] & 0xff) << 16;
        long v6 = (long) (a[6] & 0xff) << 8;
        long v7 = (long) (a[7] & 0xff);
        long my = v0 + v1 + v2 + v3 + v4 + v5 + v6 + v7;
    }

    //byte数组转成long
    public static long byteToLong(byte[] b) {
        long s = 0;
        long s0 = b[0] & 0xff;// 最低位
        long s1 = b[1] & 0xff;
        long s2 = b[2] & 0xff;
        long s3 = b[3] & 0xff;
        long s4 = b[4] & 0xff;// 最低位
        long s5 = b[5] & 0xff;
        long s6 = b[6] & 0xff;
        long s7 = b[7] & 0xff;

        // s0不变
        s1 <<= 8;
        s2 <<= 16;
        s3 <<= 24;
        s4 <<= 8 * 4;
        s5 <<= 8 * 5;
        s6 <<= 8 * 6;
        s7 <<= 8 * 7;
        s = s0 | s1 | s2 | s3 | s4 | s5 | s6 | s7;
        return s;
    }

    // Date(1361431509843)/
    public static String getDateString(String text) {
        // /Date(1361431509843)/
        try {
            if (!isEmptyString(text)) {
                text = text.replace("/", "");
                text = text.replace("\\", "");
                text = text.replace("Date", "");
                text = text.replace("(", "");
                text = text.replace(")", "");
                SimpleDateFormat formatter = new SimpleDateFormat(
                        "yyyy-MM-dd HH:mm:ss");
                return formatter.format(new Date(Long.valueOf(text)));
            }
        } catch (Exception e) {
            return "";
        }
        return "";
    }


    //base64 string转换为bitmap
    public static Bitmap getBitmapByte(String str) {
        Bitmap bitmap = null;
        try {
            byte[] buffer = Base64.decode(str.getBytes(), Base64.DEFAULT);
            if (buffer != null && buffer.length > 0) {
                bitmap = BitmapFactory.decodeByteArray(buffer, 0, buffer.length);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    //使用sha-256加密  也hs256
    public static String EncryPtSHA256(String str) {
        MessageDigest md = null;
        String tmp = null;
        String des = "";
        byte[] bt = str.getBytes();
        try {
            md = MessageDigest.getInstance("SHA-256");
            md.update(bt);
            byte[] bts = md.digest();
            for (int i = 0; i < bts.length; i++) {
                tmp = Integer.toHexString(bts[i] & 0xFF);
                if (tmp.length() == 1) {
                    des += "0";
                }
                des += tmp;
            }


        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return des;
    }

    /**
     * 设置TotalListView(自定义)的高度
     *
     * @param listView
     */
    public static void setListViewHeight(ListView listView) {
        // 获取ListView对应的Adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) { // listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0); // 计算子项View 的宽高
            totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        // listView.getDividerHeight()获取子项间分隔符占用的高度
        // params.height最后得到整个ListView完整显示需要的高度
        listView.setLayoutParams(params);
    }

    /**
     * 根据键值对读取存储在本地的数据
     *
     * @param key 键
     * @return 存储的值
     */
    public String ReadString(String key) {
        SharedPreferences sp = context.getSharedPreferences("takeseal",
                Context.MODE_PRIVATE);
        if (sp != null) {
            return sp.getString(key, "");
        } else {
            return "";
        }

    }

    /**
     *                                                                       
     *    * @param bitmap      原图
     *    * @param edgeLength  希望得到的正方形部分的边长
     *    * @return  缩放截取正中部分后的位图。
     *    
     */

    public static Bitmap centerSquareScaleBitmap(Bitmap bitmap, int edgeLength) {
        if (null == bitmap || edgeLength <= 0) {
            return null;
        }
        Bitmap result = bitmap;
        int widthOrg = bitmap.getWidth();
        int heightOrg = bitmap.getHeight();
        if (widthOrg > edgeLength && heightOrg > edgeLength) {
            //压缩到一个最小长度是edgeLength的bitmap
            int longerEdge = (int) (edgeLength * Math.max(widthOrg, heightOrg) / Math.min(widthOrg, heightOrg));
            int scaledWidth = widthOrg > heightOrg ? longerEdge : edgeLength;
            int scaledHeight = widthOrg > heightOrg ? edgeLength : longerEdge;
            Bitmap scaledBitmap;
            try {
                scaledBitmap = Bitmap.createScaledBitmap(bitmap, scaledWidth, scaledHeight, true);
            } catch (Exception e) {
                return null;
            }
            //从图中截取正中间的正方形部分。
            int xTopLeft = (scaledWidth - edgeLength) / 2;
            int yTopLeft = (scaledHeight - edgeLength) / 2;
            try {
                result = Bitmap.createBitmap(scaledBitmap, xTopLeft, yTopLeft, edgeLength, edgeLength);
                scaledBitmap.recycle();
            } catch (Exception e) {
                return null;
            }
        }
        return result;
    }

    //获取当前日期
    public static String getNowDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        String str = formatter.format(curDate);
        return str;
    }

    public static boolean DateCompare(String s1, String s2) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        try {
            Date d1 = sdf.parse(s1);
            Date d2 = sdf.parse(s2);
            if (Math.abs((d1.getTime() - d2.getTime()) / (24 * 3600 * 1000)) == 1) {
                return true;
            } else {
                return false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    //前几天  day -1
    public static String getNowDateBefor(int day) {
        Calendar ca = Calendar.getInstance();
        ca.setTime(new Date());
        ca.add(Calendar.DAY_OF_YEAR, day);
        Date mydate = ca.getTime();
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        System.out.println("mydate::" + sf.format(mydate));
        return sf.format(mydate);
    }

    //获取当前日期—+时间
    public static String getNOWTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        String str = formatter.format(curDate);
        return str;
    }

    public static Bitmap getimage(Context contxt, String srcPath) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        //开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);//此时返回bm为空

        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        //现在主流手机比较多是800*480分辨率，所以高和宽我们设置为

        float hh = 800f;//这里设置高度为800f
        float ww = 480f;//这里设置宽度为480f
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;//be=1表示不缩放
        if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = 1;//设置缩放比例
        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
//        return compressImage(bitmap);//压缩好比例大小后再进行质量压缩
        return bitmap;//压缩好比例大小后再进行质量压缩

    }


    public static Bitmap compressImage(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 80, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > 1024) {  //循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;//每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
        return bitmap;
    }

    //得到旋转的角度
    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    //旋转图片
    public static Bitmap rotaingImageView(int angle, Bitmap bitmap) {
        //旋转图片 动作
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        System.out.println("angle2=" + angle);
        // 创建新的图片
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
                bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return resizedBitmap;
    }

    /**
     * 将图片bitmap转换为base64字符串
     * http://bbs.3gstdy.com
     *
     * @param bitmap
     * @return 根据url读取出的图片的Bitmap信息
     */
    public static String encodeBitmap(Bitmap bitmap) {
        try {
          /*  int count = bitmap.getWidth() * bitmap.getHeight() / 1024;
            Log.d("bitmap:compress", "1压缩前:" + count);*/
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
/*             count = bitmap.getWidth() * bitmap.getHeight() / 1024;
            Log.d("bitmap:compress", "2压缩后:" + count);*/
            return Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT)
                    .trim();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }


    /**
     * 将内容以键值对的形式存储在本地
     *
     * @param key   键
     * @param value 值
     */
    public void WriteString(String key, String value) {
        SharedPreferences sp = context.getSharedPreferences("takeseal",
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, value);
        editor.commit();
    }

    /**
     * 对URL进行编码操作
     *
     * @param text
     * @return
     */
    public static String URLEncodeImage(String text) {
        if (Utils.isEmptyString(text))
            return "";
        return URLEncoder.encode(text);
    }

    /**
     * 判断字符串是否为空,为空返回空串
     * http://bbs.3gstdy.com
     *
     * @param text
     * @return
     */
    public static String URLEncode(String text) {
        if (isEmptyString(text))
            return "";
        if (text.equals("null"))
            return "";
        return text;
    }

    /**
     * 判断字符串是否为空
     * http://bbs.3gstdy.com
     *
     * @param str
     * @return
     */
    public static boolean isEmptyString(String str) {
        return (str == null || str.length() == 0);
    }
}
