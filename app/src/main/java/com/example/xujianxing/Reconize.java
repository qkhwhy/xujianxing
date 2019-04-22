package com.example.xujianxing;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.speech.RecognizerIntent;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

public class Reconize extends AppCompatActivity {
    private static final int Request_Recongnize = 100;
    myPermission permission = new myPermission();
Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reconize);
        context=this;
        permission.CheckSelfPermission(context, new String[]{
                Manifest.permission.BLUETOOTH
                , Manifest.permission.BLUETOOTH_ADMIN
                , Manifest.permission.ACCESS_COARSE_LOCATION
                , Manifest.permission.ACCESS_FINE_LOCATION
                , Manifest.permission.INTERNET
                , Manifest.permission.WRITE_EXTERNAL_STORAGE
                , Manifest.permission.READ_EXTERNAL_STORAGE

        });


    }
    public void speechInput(View view) {
        try {
            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);  //ACTION_RECOGNIZE_SPEECH接收输入语音，ACTION_WEB_SEARCH触发网络搜索或语音操作
            //指定自有形式的输入
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);  //EXTRA_LANGUAGE_MODEL表示用于输入音频的语言模型
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT,"开始语音");//EXTRA_PROMPT语音输入对话框中的提示字符串
            //intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1);  //EXTRA_MAX_RESULTS限制潜在识别结果的数目
             intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.ENGLISH);  //EXTRA_LANGUAGE指定默认值以外的输入语言
            startActivityForResult(intent, Request_Recongnize);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "找不到语音设备", 1).show();
        }
    }
    public void speechWebSearch(View view) {
        try {
            Intent intent = new Intent(RecognizerIntent.ACTION_WEB_SEARCH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH); //LANGUAGE_MODEL_WEB_SEARCH表示进行网络搜索
            startActivityForResult(intent, Request_Recongnize);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

      public  void test(View view)
      {
          Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
          intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
          intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "fadfadsfdasf");
          intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,Locale.CHINESE);
          try {
              startActivityForResult(intent, Request_Recongnize);
          } catch (Exception e) {
              showDownloadDialog();
          }
      }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==Request_Recongnize && resultCode== Activity.RESULT_OK)
        {
            ArrayList<String> matces=data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            StringBuilder sb=new StringBuilder();
            for(String piece:matces)
            {
                sb.append(piece).append("\n");
                Toast.makeText(context,sb.toString(),0).show();
            }
        }
    }

    private void showDownloadDialog()
    {

        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("Not Available");
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setMessage("There is no recognition application installed. Would you lick to download one?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
               Intent marketIntent=new Intent(Intent.ACTION_VIEW);
               marketIntent.setData(Uri.parse("market://details?id=com.google.android.voicesearch"));
            }
        });
        builder.setNegativeButton("No",null);
        builder.create().show();
    }
}
