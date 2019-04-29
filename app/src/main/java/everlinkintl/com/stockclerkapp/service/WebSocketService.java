package everlinkintl.com.stockclerkapp.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import com.google.gson.Gson;


import java.util.ArrayList;
import java.util.List;

import everlinkintl.com.stockclerkapp.common.Cons;
import everlinkintl.com.stockclerkapp.common.SharedPreferencesUtil;
import everlinkintl.com.stockclerkapp.common.TextToSpeechCommon;
import everlinkintl.com.stockclerkapp.common.Tools;
import everlinkintl.com.stockclerkapp.data.BasicData;
import everlinkintl.com.stockclerkapp.data.NotificationRead;
import io.reactivex.CompletableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.StompClient;
import ua.naiksoftware.stomp.dto.StompHeader;

public class WebSocketService extends Service {
    private StompClient mStompClient;
    private CompositeDisposable compositeDisposable;
    TextToSpeechCommon textToSpeechCommon;
    // 两次点击按钮之间的最小点击间隔时间(单位:ms)
    private static final int MIN_CLICK_DELAY_TIME = 6000;
    // 最后一次点击的时间
    private long lastClickTime;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    Handler handler = new Handler() {
        // 通过复写handlerMessage() 从而确定更新UI的操作
        @Override
        public void handleMessage(Message msg) {
            // 根据不同线程发送过来的消息，执行不同的UI操作
            // 根据 Message对象的what属性 标识不同的消息
            switch (msg.what) {
                case 2:
                    restart();
                    break;
                case 3:
                    if (!Tools.isEmpty(msg.obj)) {
                        Intent intent = new Intent(Cons.RECEIVER_ACTION_STOCK);
                        intent.putExtra(Cons.RECEIVER_PUT_RSULT_STOCK, (String) msg.obj);
                        getApplicationContext().sendBroadcast(intent);
                    }
                    break;
            }
        }
    };


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (Build.VERSION.SDK_INT >= 26) {
            String CHANNEL_ID = "my_app";
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    "恒联物流", NotificationManager.IMPORTANCE_DEFAULT);
            ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).createNotificationChannel(channel);
            Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setContentTitle("恒联物流")
                    .setContentText("恒联物流服务").build();
            startForeground(1, notification);
        }
        socket();
        return super.onStartCommand(intent, flags, startId);
    }

    private void socket() {
        String token = (String) SharedPreferencesUtil.getParam(getApplicationContext(), Cons.EVERLINKINT_LOGIN_SP_STOCK, "");
        String name = (String) SharedPreferencesUtil.getParam(getApplicationContext(), Cons.EVERLINKINT_LOGIN_NAME_STOCK, "");
        List<StompHeader> headers = new ArrayList<>();
        headers.add(new StompHeader("token", token));
        headers.add(new StompHeader("username", name));
        headers.add(new StompHeader(StompHeader.ACK, "client"));
        if (Tools.isEmpty(token)) {
            return;
        }
        mStompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, Cons.HOST + token);
        resetSubscriptions();
        registerStompTopic();
        mStompClient.withClientHeartbeat(1000).withServerHeartbeat(1000);
        Disposable dispLifecycle = mStompClient.lifecycle()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(lifecycleEvent -> {
                    switch (lifecycleEvent.getType()) {
                        case OPENED:
                            Tools.ToastsShort(getApplicationContext(), "连接已开启");
                            break;
                        case ERROR:
                            restart();
                            break;
                        case CLOSED:
                            Tools.ToastsShort(getApplicationContext(), "连接关闭正在重启");
                            Message message = handler.obtainMessage(2);     // Message
                            handler.sendMessageDelayed(message, MIN_CLICK_DELAY_TIME);
                            break;
                        case FAILED_SERVER_HEARTBEAT:
                            restart();
                            break;
                    }
                });
        compositeDisposable.add(dispLifecycle);
        mStompClient.connect(headers);
    }

    private void restart() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {// 两次点击的时间间隔大于最小限制时间，则触发点击事件
            lastClickTime = currentTime;
            Tools.ToastsShort(getApplicationContext(), "连接出错 请稍等正在重新连接...");
            colseStompClient();
            resetSubscriptions();
            socket();
        }
    }

    //订阅消息
    private void registerStompTopic() {
        Disposable dispTopic2 = mStompClient.topic("/user/queue/notifications")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(topicMessage -> {
                    if (textToSpeechCommon == null) {
                        textToSpeechCommon = new TextToSpeechCommon(getApplicationContext());
                    }
                    Gson gson1 = new Gson();
                    NotificationRead notificationRead = gson1.fromJson(topicMessage.getPayload(), NotificationRead.class);
                    textToSpeechCommon.speech(notificationRead.getContent());
                    BasicData basicData =new BasicData();
                    basicData.setCode(Tools.code().get("tos"));
                    Message message = handler.obtainMessage(3);     // Message
                    message.obj=gson1.toJson(basicData);
                    handler.sendMessage(message);
                }, throwable -> {
                    restart();
                });
        compositeDisposable.add(dispTopic2);
    }
    private void resetSubscriptions() {
        if (compositeDisposable != null) {
            compositeDisposable.dispose();
        }
        compositeDisposable = new CompositeDisposable();
    }


    protected CompletableTransformer applySchedulers() {
        return upstream -> upstream
                .unsubscribeOn(Schedulers.newThread())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 发送消息
     *
     * @param name /app/hello2 发送的地址
     * @param cont 发送的内容 json 字符串
     */
    public void sendEchoViaStomp(String name, String cont) {
        if (!mStompClient.isConnected()) return;
        compositeDisposable.add(mStompClient.send(name, cont)
                .compose(applySchedulers())
                .subscribe(() -> {
                }, throwable -> {
                    restart();
                }));

    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    private void colseStompClient() {
        if (mStompClient != null) mStompClient.disconnect();
    }

    @Override
    public void onDestroy() {
        colseStompClient();
        if (compositeDisposable != null) compositeDisposable.dispose();
        // 重启service
        Tools tools = new Tools();
        tools.startService(getApplicationContext());
        super.onDestroy();
    }

}
