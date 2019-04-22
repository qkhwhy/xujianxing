package com.example.xujianxing;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.AssetManager;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;

public class ScanAndPrintBoth extends AppCompatActivity {
    MyReceiver myReceiver;
    PopupWindow popupWindow=new PopupWindow();
    myPermission permission = new myPermission();
    String cFileName = Environment.getExternalStorageDirectory() + File.separator + "control" + File.separator + "control.zpl";
    StringBuilder sb_read = new StringBuilder();
    Context context;
    TextView myScan;
    String mytest = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_print_both);
        context = this;
        myScan = this.findViewById(R.id.myread);
        permission.CheckSelfPermission(context, new String[]{
                Manifest.permission.BLUETOOTH
                , Manifest.permission.BLUETOOTH_ADMIN
                , Manifest.permission.ACCESS_COARSE_LOCATION
                , Manifest.permission.ACCESS_FINE_LOCATION
                , Manifest.permission.INTERNET
                , Manifest.permission.WRITE_EXTERNAL_STORAGE
                , Manifest.permission.READ_EXTERNAL_STORAGE

        });

        try {
            FileInputStream inputstream = new FileInputStream(cFileName);
            if (inputstream != null) {
                InputStreamReader inputreader = new InputStreamReader(
                        inputstream, "utf-8");
                char input[] = new char[inputstream.available()];
                inputreader.read(input);
                sb_read.append(new String(input));
                inputreader.close();
            }

            inputstream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void connect(View view) {
        buttonclick("RP_BT_Connect", "", "");
        if(myReceiver!=null)
            return;
        else
             myReceiver=new MyReceiver();

        IntentFilter filter = new IntentFilter();

        filter.addAction(Intent.ACTION_TIME_TICK);
        filter.addAction(Intent.ACTION_TIME_CHANGED);
        registerReceiver(myReceiver,filter);
    }

    public void close(View view) {
        unregisterReceiver(myReceiver);
        myReceiver=null;
         readassets();
         finish();


    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter("scan");
        registerReceiver(myScaner, intentFilter);
        IntentFilter intentPrintFilter = new IntentFilter("com.honeywell.printer.service.callback");
        registerReceiver(PrintReceiver, intentPrintFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(myScaner);
        unregisterReceiver(PrintReceiver);
    }

    private void readassets() {
        InputStream input;
        AssetManager assetManager = getAssets();
        File outFile =new File(getCacheDir().getAbsolutePath(),"control.zpl");
        Toast.makeText(context, outFile.getAbsolutePath() ,Toast.LENGTH_LONG).show();
        try {
            input = assetManager.open("control.zpl");
            byte[] buffer = new byte[input.available()];
            input.read(buffer);
            input.close();
            String cR = new String(buffer);
            Toast.makeText(context, cR, 0).show();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public BroadcastReceiver myScaner = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("scan")) {
                String c = intent.getStringExtra("data");
                myScan.setText(c);
                buttonclick("RP_BT_Print", c, "this is code-city");
            }
        }
    };

    private BroadcastReceiver PrintReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, final Intent intent) {
            //PB蓝牙版本
            if (intent.getStringExtra("cmd").equals("onPrinterChangestate")) {

                if (intent.getStringExtra("state").equals("CANCEL")) {
                    Toast.makeText(ScanAndPrintBoth.this, "打印取消", Toast.LENGTH_SHORT).show();
                } else if (intent.getStringExtra("state").equals("COMPLETE")) {
                    Toast.makeText(ScanAndPrintBoth.this, "打印完成", Toast.LENGTH_SHORT).show();
                } else if (intent.getStringExtra("state").equals("ENDDOC")) {
                    Toast.makeText(ScanAndPrintBoth.this, "打印结束", Toast.LENGTH_SHORT).show();
                } else if (intent.getStringExtra("state").equals("FINISHED")) {
                    Toast.makeText(ScanAndPrintBoth.this, "执行结束", Toast.LENGTH_SHORT).show();
                } else if (intent.getStringExtra("state").equals("NONE")) {
                    Toast.makeText(ScanAndPrintBoth.this, "执行成功", Toast.LENGTH_SHORT).show();
                } else if (intent.getStringExtra("state").equals("STARTDOC")) {
                    Toast.makeText(ScanAndPrintBoth.this, "开始打印", Toast.LENGTH_SHORT).show();
                } else if (intent.getStringExtra("state").equals("No Device")) {
                    Toast.makeText(ScanAndPrintBoth.this, "没有连接到设备", Toast.LENGTH_SHORT).show();
                } else {
                    //RP
                    Toast.makeText(ScanAndPrintBoth.this, intent.getStringExtra("state"), Toast.LENGTH_SHORT).show();
                }

            } else if (intent.getStringExtra("cmd").equals("onConnectState")) {
                //RP 和PB蓝牙版本 一样
                if (intent.getStringExtra("state").equals("no connect")) {
                    Toast.makeText(ScanAndPrintBoth.this, "没有连接设备", Toast.LENGTH_SHORT).show();
                } else if (intent.getStringExtra("state").equals("connect Fail")) {
                    Toast.makeText(ScanAndPrintBoth.this, "连接断开", Toast.LENGTH_SHORT).show();
                } else if (intent.getStringExtra("state").equals("connect ok")) {
                    Toast.makeText(ScanAndPrintBoth.this, "连接成功", Toast.LENGTH_SHORT).show();
                }

            } else if (intent.getStringExtra("cmd").equals("onWIFIState"))//PB WIFI版本
            {

                if (intent.getStringExtra("state").equals("0")) {
                    Toast.makeText(ScanAndPrintBoth.this, "连接&命令执行成功", Toast.LENGTH_SHORT).show();
                } else if (intent.getStringExtra("state").equals("1")) {
                    Toast.makeText(ScanAndPrintBoth.this, "连接失败", Toast.LENGTH_SHORT).show();
                } else if (intent.getStringExtra("state").equals("2")) {
                    Toast.makeText(ScanAndPrintBoth.this, "缺纸", Toast.LENGTH_SHORT).show();
                } else if (intent.getStringExtra("state").equals("3")) {
                    Toast.makeText(ScanAndPrintBoth.this, "纸仓打开", Toast.LENGTH_SHORT).show();
                }
            }


        }
    };

    private void buttonclick(String FunName, String value1, String value2) {
        Intent intent = new Intent("com.honeywell.printer.service");
        switch (FunName) {
            case "RP_BT_Connect"://RP BT连接
                intent.putExtra("cmd", "STARTCONNECTPRINTERFORRP_BT");
                intent.putExtra("mac", "84:25:3F:1F:0F:6D");
                sendBroadcast(intent);
                break;
            case "RP_WIFI_Connect":
                intent.putExtra("cmd", "STARTCONNECTPRINTERFORRP_WIFI");
                intent.putExtra("ip", "192.168.43.196");
                intent.putExtra("port", 9100);
                sendBroadcast(intent);
                break;
            case "RP_BT_Query":
                //BT 查询打印机状态
                intent.putExtra("cmd", "GETPRINTERSTATEFORRP_BT");

                sendBroadcast(intent);
                break;
            case "RP_WIFI_Query":
                //WIFI 查询打印机状态
                intent.putExtra("cmd", "GETPRINTERSTATEFORRP_WIFI");
                sendBroadcast(intent);
                break;
            case "RP_BT_Print":

                //BT  DPL指令打印
                intent.putExtra("cmd", "SENDDPLCMDRP_BT");
                try {
                    byte[] buffer = sb_read.toString().replace("qkhqkh1", value1).replace("qkhqkh2", value2).getBytes();
                    intent.putExtra("data", buffer);
                    sendBroadcast(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;
            case "RP_WIFI_Print":
                //WIFI DPL 指令打印
                intent.putExtra("cmd", "SENDDPLCMDRP_WIFI");
                try {
                    byte[] buffer2 = sb_read.toString().getBytes();
                    intent.putExtra("data", buffer2);
                    sendBroadcast(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case "RP_BT_PDF":
                //WIFI DPL 指令打印
                intent.putExtra("cmd", "PRINTPDFRP_BT");
                intent.putExtra("filepath", Environment.getExternalStorageDirectory() + "/1.pdf");
                intent.putExtra("headwidth", 384);
                sendBroadcast(intent);
                break;
            case "RP_WIFI_PDF":
                //WIFI DPL 指令打印
                intent.putExtra("cmd", "PRINTPDFRP_WIFI");
                intent.putExtra("filepath", Environment.getExternalStorageDirectory() + "/1.pdf");
                intent.putExtra("headwidth", 384);
                sendBroadcast(intent);
                break;
            case "RP_BT_PNG":
                //WIFI DPL 指令打印
                intent.putExtra("cmd", "PRINTIMAGERP_BT");
                intent.putExtra("filepath", Environment.getExternalStorageDirectory() + "/1.png");
                sendBroadcast(intent);
                break;
            case "RP_WIFI_PNG":
                //WIFI DPL 指令打印
                intent.putExtra("cmd", "PRINTIMAGERP_WIFI");
                intent.putExtra("filepath", Environment.getExternalStorageDirectory() + "/1.png");
                sendBroadcast(intent);
                break;
        }

    }


    public void xml()
    {
        StringBuilder sb=new StringBuilder();
        sb.append( "<?xml version=\"1.0\" encoding=\"utf-8\"?> \r\n");
        sb.append( "<string xmlns=\"http://tempuri.org/\">kg★公斤◆ ");
        sb.append( "㎡★平米◆ ");
        sb.append( "Pcs★pcs◆ ");
        sb.append( "Roll★卷</string> ");
        Toast.makeText(context,myXml.xml(sb),0).show();
    }
}
