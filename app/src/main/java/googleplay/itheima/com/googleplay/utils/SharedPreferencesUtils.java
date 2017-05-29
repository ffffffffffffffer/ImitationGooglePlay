package googleplay.itheima.com.googleplay.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * @author TanJJ
 * @time 2017/5/29 10:58
 * @ProjectName GooglePlay
 * @PackageName googleplay.itheima.com.googleplay.utils
 * @des Shared储存
 */

public class SharedPreferencesUtils {
    private final Context context;
    private final SharedPreferences mSharedPreferences;

    public SharedPreferencesUtils(Context context, String key) {
        this.context = context;
        mSharedPreferences = context.getSharedPreferences(key, Context.MODE_PRIVATE);
    }

    public void putInt(String key, int value) {
        SharedPreferences.Editor edit = mSharedPreferences.edit();
        edit.putInt(key, value);
        edit.commit();
    }

    public int getInt(String key, int defValue) {
        return mSharedPreferences.getInt(key, defValue);
    }

    public void putLong(String key, long value) {
        SharedPreferences.Editor edit = mSharedPreferences.edit();
        edit.putLong(key, value);
        edit.commit();
    }

    public long getLong(String key, long defValue) {
        return mSharedPreferences.getLong(key, defValue);
    }
}
