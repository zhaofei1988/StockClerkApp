package everlinkintl.com.stockclerkapp.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.vise.log.ViseLog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.OnClick;
import everlinkintl.com.stockclerkapp.MyApplication;
import everlinkintl.com.stockclerkapp.MyBsetActivity;
import everlinkintl.com.stockclerkapp.R;
import everlinkintl.com.stockclerkapp.adapter.MainListAdapter;
import everlinkintl.com.stockclerkapp.common.Tools;
import everlinkintl.com.stockclerkapp.data.TackDetailsData;
import everlinkintl.com.stockclerkapp.zxing.android.CaptureActivity;

public class MainActivity extends MyBsetActivity {
    @BindString(R.string.toolbar_scan)
    String toolbarScan;
    List<TackDetailsData> list;
    MainListAdapter adapter;
    @BindView(R.id.main_list)
    ListView listView;
    @BindView(R.id.bottom)
    RelativeLayout bottom;
    private long exitTime = 0;
    private static final String DECODED_CONTENT_KEY = "codedContent";
    private static final int REQUEST_CODE_SCAN = 0x0000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitleName("交接任务");
        setGoneBreak();
        setTitleRigthTv(true, toolbarScan, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CaptureActivity.class);
                startActivityForResult(intent, REQUEST_CODE_SCAN);
            }
        });
        list = new ArrayList<>();
        adapter = new MainListAdapter(getApplicationContext());
        adapter.setData(list);
        listView.setAdapter(adapter);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                //    通过AlertDialog.Builder这个类来实例化我们的一个AlertDialog的对象
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                //    设置Title的内容
                builder.setTitle("提示");
                //    设置Content来显示一个信息
                builder.setMessage("确定删除吗？");
                //    设置一个PositiveButton
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                //    设置一个NegativeButton
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                //    显示出该对话框
                builder.show();
                return false;
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 扫描二维码/条码回传
        if (requestCode == REQUEST_CODE_SCAN && resultCode == RESULT_OK) {
            if (data != null) {
                //返回的文本内容
                String content = data.getStringExtra(DECODED_CONTENT_KEY);
                Tools.ToastsLong(getApplicationContext(),content);
            }
        }
    }
    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void setData(String string) {

    }

    @OnClick({R.id.bottom_get, R.id.bottom_out})
    public void onViewClicked(View view) {
        if (!Tools.isFastClick()) {
            return;
        }
        switch (view.getId()) {
            case R.id.bottom_get:

                break;
            case R.id.bottom_out:

                break;
        }
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void exit() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Tools.ToastsShort(getApplicationContext(), "再按一次退出");
            exitTime = System.currentTimeMillis();
        } else {
            ((MyApplication) getApplication()).finishAll();
        }
    }
}
