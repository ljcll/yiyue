package cn.edu.swufe.gupiao;

import android.content.Context;
import android.content.Intent;
import android.service.autofill.SaveInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class denglu extends AppCompatActivity {
    private EditText et_username;
    private EditText et_password;
    private EditText et_password2;
    private EditText et_mail;
    private Button btn_login;
    private Button btn_register;
    private CheckBox checkbox;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_denglu);
        Map<String, String> userInfo = SaveIn.getSaveInformation(this);
        if (userInfo != null) {
            et_username.setText(userInfo.get("username"));
            et_password.setText(userInfo.get("password"));
        }
        et_username =(EditText) findViewById(R.id.et_username);
        et_password =(EditText) findViewById(R.id.et_password);
        et_password2 =(EditText) findViewById(R.id.reg_password2);
        et_mail = (EditText) findViewById(R.id.reg_mail);
        checkbox = (CheckBox) findViewById(R.id.checkBox);
        btn_login =(Button) findViewById(R.id.button_login);
        btn_register =(Button) findViewById(R.id.button_register);
        btn_login.setOnClickListener(new MyButton());
        btn_register.setOnClickListener(new MyButton());
    }
    public  class MyButton implements View.OnClickListener{
        @Override
        public void onClick(View view){
            String username =et_username.getText().toString().trim();
            String password =et_password.getText().toString().trim();
            switch (view.getId()) {
                //当点击登录按钮时
                case R.id.button_login:
                    if(TextUtils.isEmpty(username) || TextUtils.isEmpty(password)){
                        Toast.makeText(denglu.this,"密码或账号不能为空",Toast.LENGTH_SHORT).show();
                    } else {
                        if(checkbox.isChecked()){
                            //保存密码的操作
                        }
                        Toast.makeText(denglu.this,"登录成功",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(denglu.this, MainActivity.class);
                        startActivity(intent);
                    }
                    break;
                //当点击注册按钮事件时
                case R.id.button_register:
                    Intent intent = new Intent(denglu.this,RegisterActivity.class);
                    startActivity(intent);
                    break;

            }
        }
    }
}






