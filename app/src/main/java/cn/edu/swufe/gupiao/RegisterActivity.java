package cn.edu.swufe.gupiao;

import android.content.Context;
import android.content.Intent;
import android.service.autofill.SaveInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private EditText reg_username;
    private EditText reg_password;
    private EditText reg_password2;
    private EditText reg_mail;
    private Button reg_btn_sure;
    private Button reg_btn_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_activity);
        reg_username = (EditText) findViewById(R.id.reg_username);
        reg_password = (EditText) findViewById(R.id.reg_password);
        reg_password2 = (EditText) findViewById(R.id.reg_password2);
        reg_mail = (EditText) findViewById(R.id.reg_mail);
        reg_btn_sure = (Button) findViewById(R.id.reg_btn_sure);
        reg_btn_login = (Button) findViewById(R.id.reg_btn_login);
        reg_btn_sure.setOnClickListener(new RegisterButton());
        reg_btn_login.setOnClickListener(new RegisterButton());
    }

    public class RegisterButton implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            String username = reg_username.getText().toString().trim();
            String password = reg_password.getText().toString().trim();
            String password2 = reg_password2.getText().toString().trim();
            String mail = reg_mail.getText().toString().trim();
            switch (v.getId()) {
                //注册开始，判断注册条件
                case R.id.reg_btn_sure:
                    if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password) || TextUtils.isEmpty(password2) || TextUtils.isEmpty(mail)) {
                        Toast.makeText(RegisterActivity.this, "各项均不能为空", Toast.LENGTH_SHORT).show();
                    } else {
                        if (TextUtils.equals(password, password2)) {
                            //执行注册操作
                            SaveIn.SaveInformation(RegisterActivity.this,username,password,password2,mail);
                            Toast.makeText(RegisterActivity.this,"注册成功,请返回登录",Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(RegisterActivity.this, "两次输入的密码不一样", Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;
                case R.id.reg_btn_login:
                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                    startActivity(intent);
                    break;

            }
        }
    }
}


