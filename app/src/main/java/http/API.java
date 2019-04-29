package http;

import android.app.Activity;

import java.util.Map;

/***   API 函数调用
 *             Map <String ,String>  map= new HashMap<>();
 *         API.loging(map,this.getApplicationContext(),new Okhttp.Objectcallback() {
 *             @Override
 *             public void onsuccess(Object object) {
 *
 *             }
 *
 *             @Override
 *             public void fileOnsuccess(Object object) {
 *
 *             }
 *
 *             @Override
 *             public void onFalia(int code, String errst) {
 *
 *             }
 *
 *             @Override
 *             public void downLoadInProgress(float progress, long total) {
 *
 *             }
 *
 *             @Override
 *             public void downLoadOnsuccess(File file) {
 *
 *             }
 *         });
 */
public class API {
    //登陆
    public static void loging(Map<String,String> map, Activity activity, Okhttp.BasicsBack handler){
       Okhttp.post("/login",map,activity,true,handler);
    }
    //校验token
    public static void checkoutToken(Map<String,String> map,  Activity activity, Okhttp.BasicsBack handler ){
        Okhttp.get("/users/current",map,activity,false,handler);
    }


}
