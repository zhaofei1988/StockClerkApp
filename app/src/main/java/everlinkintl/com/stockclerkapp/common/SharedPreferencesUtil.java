package everlinkintl.com.stockclerkapp.common;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesUtil {

    /**
     *
     SharedPreferencesUtils.setParam(this, "String", "xiaanming");
     SharedPreferencesUtils.setParam(this, "int", 10);
     SharedPreferencesUtils.setParam(this, "boolean", true);
     SharedPreferencesUtils.setParam(this, "long", 100L);
     SharedPreferencesUtils.setParam(this, "float", 1.1f);

     SharedPreferencesUtils.getParam(TimerActivity.this, "String", "");                                                                                        SharedPreferencesUtils.getParam(TimerActivity.this, "int", 0);
     SharedPreferencesUtils.getParam(TimerActivity.this, "boolean", false);
     SharedPreferencesUtils.getParam(TimerActivity.this, "long", 0L);
     SharedPreferencesUtils.getParam(TimerActivity.this, "float", 0.0f);
     * 保存数据的方法，我们需要拿到保存数据的具体类型，然后根据类型调用不同的保存方法
     * @param context
     * @param key
     * @param object
     */
    public static void setParam(Context context , String key, Object object){

        SharedPreferences sp = context.getSharedPreferences(Cons.SHARED_PREFERENCES_FILE_NAME_STOCK, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

         if(object instanceof Integer){
            editor.putInt(key, (Integer)object);
        }
        else if(object instanceof Boolean){
            editor.putBoolean(key, (Boolean)object);
        }
        else if(object instanceof Float){
            editor.putFloat(key, (Float)object);
        }
        else if(object instanceof Long){
            editor.putLong(key, (Long)object);
        }else {
            editor.putString(key, (String)object);
        }

        editor.commit();
    }


    /**
     * 得到保存数据的方法，我们根据默认值得到保存的数据的具体类型，然后调用相对于的方法获取值
     * @param context
     * @param key
     * @param defaultObject
     * @return
     */
    public static Object getParam(Context context , String key, Object defaultObject){
        String type = defaultObject.getClass().getSimpleName();
        SharedPreferences sp = context.getSharedPreferences(Cons.SHARED_PREFERENCES_FILE_NAME_STOCK, Context.MODE_PRIVATE);
       if("Integer".equals(type)){
            return sp.getInt(key, (Integer)defaultObject);
        }
        else if("Boolean".equals(type)){
            return sp.getBoolean(key, (Boolean)defaultObject);
        }
        else if("Float".equals(type)){
            return sp.getFloat(key, (Float)defaultObject);
        }
        else if("Long".equals(type)){
            return sp.getLong(key, (Long)defaultObject);
        }else {
            return sp.getString(key, (String)defaultObject);
        }
    }

    /**
     * 清除所有数据
     * @param context
     */
    public static void clearAll(Context context) {
        SharedPreferences sp = context.getSharedPreferences(Cons.SHARED_PREFERENCES_FILE_NAME_STOCK,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear().commit();
    }

    /**
     * 清除指定数据
     * @param context
     */
    public static void clearItem(Context context,String key) {
        SharedPreferences sp = context.getSharedPreferences(Cons.SHARED_PREFERENCES_FILE_NAME_STOCK,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(key);
        editor.commit();
    }
}
