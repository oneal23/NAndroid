package com.nandroid.demo;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.move.xanim.OnSubscribe;
import com.move.xanim.Xanim;
import com.move.xanim.XanimAnimator;
import com.move.xanim.XanimMaker;

import java.security.MessageDigest;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private RelativeLayout main_container;//容器 xml也就这一个控件
    private int height;//屏幕高度
    private int width;//屏幕宽度
    private int duration;//动画时间
    private int left;//距离左边界的距离
    private Activity activity;
    private Bitmap bitmap;//鲜花的对象

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        activity = this;

//        main_container = (RelativeLayout) findViewById(R.id.main_container);
//
//        height = getResources().getDisplayMetrics().heightPixels;
//        width = getResources().getDisplayMetrics().widthPixels;
//
//        bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.e71);
//
//        mhandler.post(runnable);//sta
        getSignature();
        findViewById(R.id.ibOk).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("weixin://send?phone=8615618242825");
                startActivity(new Intent(Intent.ACTION_VIEW,uri));
            }
        });
    }

    Random random = new Random();

    Handler mhandler = new Handler();

    Runnable runnable = new Runnable() {
        @Override
        public void run() {

            randomFlower();

            mhandler.postDelayed(this, 300);
        }
    };

    private void randomFlower() {
        final ImageView imageView = new ImageView(activity);
        main_container.addView(imageView);
        imageView.setImageBitmap(bitmap);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        duration = new Random().nextInt(2000) + 2000;
        left = random.nextInt(width - 200) + 100;

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams
                (100, 100);
        params.setMargins(left, 0, 0, 0);
        imageView.setLayoutParams(params);

        XanimAnimator.from(imageView)
                .anim(duration, new XanimMaker() {
                    @Override
                    public void onAnim(Xanim anim) {
                        anim.translateY(height);
                    }
                }).animInterpolator(new LinearInterpolator())
                .subscribe(new OnSubscribe<View>() {
                    @Override
                    public void onNext(View view) {

                    }

                    @Override
                    public void onComplete() {
                        main_container.removeView(imageView);
                    }

                    @Override
                    public void onError(Throwable t) {

                    }
                });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                main_container.removeView(imageView);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mhandler.removeCallbacks(runnable);
        mhandler = null;
        bitmap.recycle();
    }

    public void getSignature() {
        PackageManager manager = getPackageManager();
        StringBuilder builder = new StringBuilder();
        String pkgname = getPackageName();
        boolean isEmpty = pkgname.isEmpty();
        if (isEmpty) {
            Toast.makeText(this, "应用程序的包名不能为空！", Toast.LENGTH_SHORT);
        } else {
            try {

                PackageInfo packageInfo = manager.getPackageInfo(pkgname, PackageManager.GET_SIGNATURES);

                Signature[] signatures = packageInfo.signatures;
                Signature sign = signatures[0];

                byte[] signByte = sign.toByteArray();
                Log.e("getSingInfo", bytesToHexString(signByte));
                Log.e("getSingInfo hash", bytesToHexString(generateSHA1(signByte)));
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public static byte[] generateSHA1(byte[] data) {
        try {
            // 使用getInstance("算法")来获得消息摘要,这里使用SHA-1的160位算法
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
            // 开始使用算法
            messageDigest.update(data);
            // 输出算法运算结果
            byte[] hashValue = messageDigest.digest(); // 20位字节
            return hashValue;
        } catch (Exception e) {
            Log.e("generateSHA1", e.getMessage());
        }
        return null;

    }


    public static String bytesToHexString(byte[] bytes) {
        if (bytes == null) {
            return "";
        }
        StringBuilder buff = new StringBuilder();
        for (byte aByte : bytes) {
            if ((aByte & 0xff) < 16) {
                buff.append('0');
            }
            buff.append(Integer.toHexString(aByte & 0xff));
        }
        return buff.toString();
    }
}
