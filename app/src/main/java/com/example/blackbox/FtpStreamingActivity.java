package com.example.blackbox;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class FtpStreamingActivity extends AppCompatActivity {

    private final String TAG = "FtpActivity";
    private ConnectFTP ConnectFTP;
    String status;
    String[] ftpFileList;
    String ip;
    int port;
    String username;
    String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ftp);
        Button button = findViewById(R.id.ConF_btn);
        Button button2 = findViewById(R.id.sharedP_btn);
        EditText editText1 = findViewById(R.id.editTextIp);
        EditText editText2 = findViewById(R.id.editTextPt);
        EditText editText3 = findViewById(R.id.editTextId);
        EditText editText4 = findViewById(R.id.editTextPw);
        editText4.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

        ConnectFTP = new ConnectFTP();

        SharedPreferences sharedPreferences = getSharedPreferences("sharedPreferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        if(sharedPreferences.getInt("port",0)==0)
        {
            button2.setVisibility(View.INVISIBLE);
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ip = editText1.getText().toString();
                port = Integer.parseInt(editText2.getText().toString());
                username = editText3.getText().toString();
                password = editText4.getText().toString();

                editor.putString("ip", ip);
                editor.putInt("port", port);
                editor.putString("username", username);
                editor.putString("password", password);
                editor.commit();

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        status = ConnectFTP.ftpConnect(ip,username,password,port);
                        ftpFileList = ConnectFTP.ftpGetFileList("");
                    }
                }).start();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),status,Toast.LENGTH_SHORT).show();
                    }
                }, 3000);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(getApplicationContext(), FtpListActivity.class);
                        // listActivity2 intent를 생성
                        intent.putExtra("ftpFileList",ftpFileList);
                        // pos를 pos라는 키값으로 intent의 Extra로 담음
                        intent.putExtra("ip", ip);
                        intent.putExtra("port", port);
                        intent.putExtra("username", username);
                        intent.putExtra("password", password);
                        startActivity(intent);
                        // intent(listActivity2)를 실행함 이때 Extra들도 같이 보냄
                    }
                }, 3000);
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ip=sharedPreferences.getString("ip","");
                port=sharedPreferences.getInt("port",21);
                username=sharedPreferences.getString("username","");
                password=sharedPreferences.getString("password","");

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        status = ConnectFTP.ftpConnect(ip,username,password,port);
                        ftpFileList = ConnectFTP.ftpGetFileList("");
                    }
                }).start();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),status,Toast.LENGTH_SHORT).show();
                    }
                }, 3000);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(getApplicationContext(), FtpListActivity.class);
                        // listActivity2 intent를 생성
                        intent.putExtra("ftpFileList",ftpFileList);
                        // pos를 pos라는 키값으로 intent의 Extra로 담음
                        intent.putExtra("ip", ip);
                        intent.putExtra("port", port);
                        intent.putExtra("username", username);
                        intent.putExtra("password", password);
                        startActivity(intent);
                        // intent(listActivity2)를 실행함 이때 Extra들도 같이 보냄
                    }
                }, 3000);
            }
        });
    }
}