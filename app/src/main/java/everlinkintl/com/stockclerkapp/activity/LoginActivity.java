package everlinkintl.com.stockclerkapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindArray;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.OnClick;
import everlinkintl.com.stockclerkapp.MyBsetActivity;
import everlinkintl.com.stockclerkapp.R;
import everlinkintl.com.stockclerkapp.common.Cons;
import everlinkintl.com.stockclerkapp.common.SharedPreferencesUtil;
import everlinkintl.com.stockclerkapp.common.Tools;
import everlinkintl.com.stockclerkapp.data.LoginData;
import http.API;
import http.Okhttp;

/**
 * 登陆
 */
public class LoginActivity extends MyBsetActivity {
    @BindView(R.id.login_phone_et)
    EditText loginPhoneEt;
    @BindView(R.id.login_pssword_et)
    EditText loginPsswordEt;
    @BindString(R.string.user_login)
    String userLogin;

    @Override
    protected int getContentLayoutId() {
        return R.layout.login_layout;
    }

    @Override
    protected void setData(String string) {
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitleName(userLogin);
        setGoneBreak();
    }

    @OnClick({R.id.login_btn, R.id.login_password_tv, R.id.login_register_tv})
    public void onViewClicked(View view) {
        if (!Tools.isFastClick()) {
            return;
        }
        Intent intent;
        switch (view.getId()) {
            case R.id.login_btn:
                loginVerify();
                break;
            case R.id.login_password_tv:
//                intent = new Intent(getApplicationContext(), ForgetPasswordActivity.class);
//                startActivity(intent);
                break;
            case R.id.login_register_tv:
//                intent = new Intent(getApplicationContext(), RegisterActivity.class);
//                startActivity(intent);
                break;
        }

    }

    private void loginVerify() {
        if (Tools.isEmpty(loginPhoneEt.getText().toString().trim())) {
            Tools.ToastsShort(getApplicationContext(), "请填写手机号");
            return;
        }
        if (Tools.isEmpty(loginPsswordEt.getText().toString().trim())) {
            Tools.ToastsShort(getApplicationContext(), "请填写密码");
            return;
        }
        Map<String, String> map = new HashMap<>();
        map.put("username", loginPhoneEt.getText().toString().trim());
        map.put("password", loginPsswordEt.getText().toString().trim());
        API.loging(map, this, new Okhttp.BasicsBack() {
            @Override
            public void onFalia(String errst) {
              Tools.ToastsShort(getApplicationContext(),errst);
            }
            @Override
            public void onsuccess(String object) {
                Gson gson = new Gson();
                LoginData loginData = gson.fromJson(object, LoginData.class);
                SharedPreferencesUtil.setParam(getApplicationContext(), Cons.EVERLINKINT_LOGIN_SP_STOCK, loginData.getToken());
                SharedPreferencesUtil.setParam(getApplicationContext(), Cons.EVERLINKINT_LOGIN_NAME_STOCK, loginPhoneEt.getText().toString().trim());
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                Tools tools =new Tools();
                tools.startService(getApplicationContext());
            }
        });
    }
}
