package com.example.xujianxing;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.PermissionInfo;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompatSideChannelService;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.PermissionChecker;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import java.security.Permission;

public   class myPermission extends AppCompatActivity {
    private    Context context;
    private       String cPermissionToast="请进行授权！";
    /**
     * 检测并请求授权
     * @param context  传入调用授权窗体的上下文
     * @param cPermissions  待传入的权限数据 new String[]{ Manifest.permission.X.....}
     */
    public    void CheckSelfPermission(Context context,String[] cPermissions )
    {
        this.context= context;
        for(String cPermission:cPermissions) {
            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M) {
                if (ActivityCompat.checkSelfPermission(context, cPermission) != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, cPermission)) {
                        ActivityCompat.requestPermissions((Activity) context, cPermissions, 1);
                    } else {
                        ActivityCompat.requestPermissions((Activity) context, cPermissions, 1);
                    }
                }
            }
            else
            {
                if (ContextCompat.checkSelfPermission(context, cPermission) != PermissionChecker.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, cPermission)) {
                        ActivityCompat.requestPermissions((Activity) context, cPermissions, 1);
                    } else {
                        ActivityCompat.requestPermissions((Activity) context, cPermissions, 1);
                    }
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Boolean bHavePermission=true;
        StringBuilder sb=new StringBuilder();
        for(int iIndex=0;iIndex<grantResults.length;iIndex++)
        {
            if(grantResults[iIndex]!=PackageManager.PERMISSION_GRANTED)
            {
                    sb.append(permissions[iIndex]+"\r\n")  ;
                    bHavePermission=false;
            }
        }
        if(!bHavePermission)
        {
            Toast.makeText(this.context,cPermissionToast+"\r\n"+sb.toString(),Toast.LENGTH_LONG).show();
        }
    }
}
