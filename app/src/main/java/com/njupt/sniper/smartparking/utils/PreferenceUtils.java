/*
 * Copyright 2012 GitHub Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.njupt.sniper.smartparking.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;

import static android.content.Context.MODE_PRIVATE;
import static android.os.Build.VERSION.SDK_INT;
import static android.os.Build.VERSION_CODES.GINGERBREAD;


/**
 * Utility class for working with {@link SharedPreferences}
 */
public class PreferenceUtils {

    public static final String CURRENT_ACCOUNT = "current_account";

    public static final String CURRENT_ACCOUNT_ID = "current_account_id";

    private static final String GUIDE_TAG_1 = "guide_tag_1";

    private static final String OBJFILENAME = "saveObject";


    /**
     * Get code browsing preferences
     *
     * @param context
     * @return preferences
     */

    public static SharedPreferences getAccountPreference(final Context context) {
        return context.getSharedPreferences("account", MODE_PRIVATE);
    }

    public static SharedPreferences getFpStateCodePreference(final Context context) {
        return context.getSharedPreferences("fp_state_code", MODE_PRIVATE);
    }

    public static void saveAccountId(final Context context, final String id) {
        SharedPreferences sp = getAccountPreference(context);
        save(sp.edit().putString(CURRENT_ACCOUNT_ID, id));
    }

    public static String getAccountId(final Context context) {
        SharedPreferences sp = getAccountPreference(context);
        return sp.getString(CURRENT_ACCOUNT_ID, "");
    }

    private static boolean isEditorApplyAvailable() {
        return SDK_INT >= GINGERBREAD;
    }

    /**
     * Save preferences in given editor
     *
     * @param editor
     */
    @SuppressLint("NewApi")
    public static void save(final Editor editor) {
        if (isEditorApplyAvailable())
            editor.apply();
        else
            editor.commit();
    }

    private static void setInt(Context context, String spName, String key, int value) {
        if (context != null) {
            SharedPreferences sp = context.getSharedPreferences(spName, MODE_PRIVATE);
            Editor editor = sp.edit();
            editor.putInt(key, value);
            save(editor);
        }
    }

    private static int getInt(Context context, String spName, String key, int defaultValue) {
        if (context != null) {
            SharedPreferences sp = context.getSharedPreferences(spName, MODE_PRIVATE);
            return sp.getInt(key, defaultValue);
        } else {
            return defaultValue;
        }
    }

    //getLong,setLong
    private static void setLong(Context context, String spName, String key, Long value) {
        if (context != null) {
            SharedPreferences sp = context.getSharedPreferences(spName, MODE_PRIVATE);
            Editor editor = sp.edit();
            editor.putLong(key, value);
            save(editor);
        }
    }

    private static Long getLong(Context context, String spName, String key, Long defaultValue) {
        if (context != null) {
            SharedPreferences sp = context.getSharedPreferences(spName, MODE_PRIVATE);
            return sp.getLong(key, defaultValue);
        } else {
            return defaultValue;
        }
    }

    private static void setString(Context context, String spName, String key, String value) {
        if (context != null) {
            SharedPreferences sp = context.getSharedPreferences(spName, MODE_PRIVATE);
            Editor editor = sp.edit();
            editor.putString(key, value);
            save(editor);
        }
    }

    private static String getString(Context context, String spName, String key, String defaultValue) {
        if (context != null) {
            SharedPreferences sp = context.getSharedPreferences(spName, MODE_PRIVATE);
            return sp.getString(key, defaultValue);
        } else {
            return defaultValue;
        }
    }

    private static void setBool(Context context, String spName, String key, boolean value) {
        if (context != null) {
            SharedPreferences sp = context.getSharedPreferences(spName, MODE_PRIVATE);
            Editor editor = sp.edit();
            editor.putBoolean(key, value);
            save(editor);
        }
    }

    private static boolean getBool(Context context, String spName, String key, boolean defaultValue) {
        if (context != null) {
            SharedPreferences sp = context.getSharedPreferences(spName, MODE_PRIVATE);
            return sp.getBoolean(key, defaultValue);
        } else {
            return defaultValue;
        }
    }

    /**
     * desc:保存对象
     *
     * @param context
     * @param key
     * @param obj     要保存的对象，只能保存实现了serializable的对象
     *                modified:
     */
    private static void saveObject(Context context, String key, Object obj) {
        try {
            // 保存对象
            Editor sharedata = context.getSharedPreferences("saveObject", 0).edit();
            //先将序列化结果写到byte缓存中，其实就分配一个内存空间
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream os = new ObjectOutputStream(bos);
            //将对象序列化写入byte缓存
            os.writeObject(obj);
            //将序列化的数据转为16进制保存
            String bytesToHexString = bytesToHexString(bos.toByteArray());
            //保存该16进制数组
            sharedata.putString(key, bytesToHexString);
            sharedata.commit();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("", "保存obj失败");
        }
    }

    /**
     * desc:将数组转为16进制
     *
     * @param bArray
     * @return modified:
     */
    private static String bytesToHexString(byte[] bArray) {
        if (bArray == null) {
            return null;
        }
        if (bArray.length == 0) {
            return "";
        }
        StringBuffer sb = new StringBuffer(bArray.length);
        String sTemp;
        for (int i = 0; i < bArray.length; i++) {
            sTemp = Integer.toHexString(0xFF & bArray[i]);
            if (sTemp.length() < 2)
                sb.append(0);
            sb.append(sTemp.toUpperCase());
        }
        return sb.toString();
    }

    /**
     * desc:获取保存的Object对象
     *
     * @param context
     * @param key
     * @return modified:
     */
    private static Object readObject(Context context, String key) {
        try {
            SharedPreferences sharedata = context.getSharedPreferences(OBJFILENAME, 0);
            if (sharedata.contains(key)) {
                String string = sharedata.getString(key, "");
                if (TextUtils.isEmpty(string)) {
                    return null;
                } else {
                    //将16进制的数据转为数组，准备反序列化
                    byte[] stringToBytes = StringToBytes(string);
                    ByteArrayInputStream bis = new ByteArrayInputStream(stringToBytes);
                    ObjectInputStream is = new ObjectInputStream(bis);
                    //返回反序列化得到的对象
                    Object readObject = is.readObject();
                    return readObject;
                }
            }
        } catch (StreamCorruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        //所有异常返回null
        return null;
    }

    /**
     * desc:将16进制的数据转为数组
     * <p>创建人：聂旭阳 , 2014-5-25 上午11:08:33</p>
     *
     * @param data
     * @return modified:
     */
    private static byte[] StringToBytes(String data) {
        String hexString = data.toUpperCase().trim();
        if (hexString.length() % 2 != 0) {
            return null;
        }
        byte[] retData = new byte[hexString.length() / 2];
        for (int i = 0; i < hexString.length(); i++) {
            int int_ch;  // 两位16进制数转化后的10进制数
            char hex_char1 = hexString.charAt(i); ////两位16进制数中的第一位(高位*16)
            int int_ch1;
            if (hex_char1 >= '0' && hex_char1 <= '9')
                int_ch1 = (hex_char1 - 48) * 16;   //// 0 的Ascll - 48
            else if (hex_char1 >= 'A' && hex_char1 <= 'F')
                int_ch1 = (hex_char1 - 55) * 16; //// A 的Ascll - 65
            else
                return null;
            i++;
            char hex_char2 = hexString.charAt(i); ///两位16进制数中的第二位(低位)
            int int_ch2;
            if (hex_char2 >= '0' && hex_char2 <= '9')
                int_ch2 = (hex_char2 - 48); //// 0 的Ascll - 48
            else if (hex_char2 >= 'A' && hex_char2 <= 'F')
                int_ch2 = hex_char2 - 55; //// A 的Ascll - 65
            else
                return null;
            int_ch = int_ch1 + int_ch2;
            retData[i / 2] = (byte) int_ch;//将转化后的数放入Byte里
        }
        return retData;
    }



}
