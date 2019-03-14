package com.na.net;

import android.content.Context;
import android.text.TextUtils;

import com.na.utils.LogUtil;
import com.na.utils.NetUtil;
import com.na.utils.SchedulerProviderUtil;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiUtil {
    private final static String DefaultHttpCacheName = "http_cache";
    //默认缓存大小为50mb
    private final static int DefaultCacheMaxSize = 1024 * 1024 * 50;
    // 无网络时，默认设置缓存超时时间为30天
    private final static int DefaultMaxStale = 60 * 60 * 24 * 30;
    private final static int DefaultConnectTimeout = 30;
    private final static int DefaultWriteTimeout = 30;
    private final static int DefaultReadTimeout = 30;
    private final static int RetryCount = 0;
    private static ApiUtil instance;
    private Context context;
    OkHttpClient okHttpClient;
    Retrofit retrofit;
    private String baseUrl;
    private String httpCacheName;

    public static ApiUtil getInstance() {
        if (instance == null) {
            synchronized (ApiUtil.class) {
                if (instance == null) {
                    instance = new ApiUtil();
                }
            }
        }
        return instance;
    }

    public void init(Context context, String baseUrl) {
        init(context, baseUrl, DefaultHttpCacheName);
    }

    public void init(Context context, String baseUrl, String httpCacheName) {
        setContext(context.getApplicationContext());
        setBaseUrl(baseUrl);
        setHttpCacheName(httpCacheName);
        initOkhttoClient();
        initRetrofit();
    }

    private void initRetrofit() {
        retrofit = new Retrofit.Builder()
                .client(getOkHttpClient())
                .baseUrl(getBaseUrl())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

    }

    private void initOkhttoClient() {
        OkHttpClient.Builder okHttpBuilder = new OkHttpClient.Builder();

        /**
         * 设置缓存
         */
        File cacheFile = new File(context.getExternalCacheDir(), getHttpCacheName());
        int maxSize = DefaultCacheMaxSize;
        Cache cache = new Cache(cacheFile, maxSize);
        Interceptor cacheInterceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                if (!NetUtil.isNetworkConnected(getContext())) {
                    request = request.newBuilder()
                            .cacheControl(CacheControl.FORCE_CACHE)
                            .build();
                }
                Response response = chain.proceed(request);
                if (!NetUtil.isNetworkConnected(getContext())) {
                    int maxAge = 0;
                    // 有网络时 设置缓存超时时间0个小时
                    response.newBuilder()
                            .header("Cache-Control", "public, max-age=" + maxAge)
                            .removeHeader(getHttpCacheName())// 清除头信息，因为服务器如果不支持，会返回一些干扰信息，不清除下面无法生效
                            .build();
                } else {
                    int maxStale = DefaultMaxStale;
                    response.newBuilder()
                            .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                            .removeHeader(getHttpCacheName())
                            .build();
                }
                return response;
            }
        };
        okHttpBuilder.cache(cache).addInterceptor(cacheInterceptor);

        /**
         * 设置头信息
         */
        Interceptor headerInterceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request originalRequest = chain.request();
                Request.Builder requestBuilder = originalRequest.newBuilder()
                        .addHeader("Accept-Encoding", "gzip")
                        .addHeader("Accept", "application/json")
                        .addHeader("Content-Type", "application/json; charset=utf-8")
                        .method(originalRequest.method(), originalRequest.body());
                Request request = requestBuilder.build();
                return chain.proceed(request);
            }
        };
        okHttpBuilder.addInterceptor(headerInterceptor);

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                LogUtil.d(message);
            }
        });

        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        //设置 Debug Log 模式
        okHttpBuilder.addInterceptor(loggingInterceptor);

        /**
         * 设置超时和重新连接
         */
        okHttpBuilder.connectTimeout(DefaultConnectTimeout, TimeUnit.SECONDS);
        okHttpBuilder.readTimeout(DefaultReadTimeout, TimeUnit.SECONDS);
        okHttpBuilder.writeTimeout(DefaultWriteTimeout, TimeUnit.SECONDS);
        //错误重连
        okHttpBuilder.retryOnConnectionFailure(true);

        //https
        try {
            HttpsUtil.SSLParams sslParams = HttpsUtil.getSslSocketFactory();
            okHttpBuilder.sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager);
        } catch (Exception e) {
            e.printStackTrace();
        }

        okHttpClient = okHttpBuilder.build();
    }

    public Context getContext() {
        return context;
    }

    private void setContext(Context context) {
        this.context = context;
    }

    public String getHttpCacheName() {
        return httpCacheName;
    }

    private void setHttpCacheName(String httpCacheName) {
        this.httpCacheName = httpCacheName;
    }

    private OkHttpClient getOkHttpClient() {
        return okHttpClient;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    private void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public <T> T createApi(final Class<T> apiClass) {
        return retrofit.create(apiClass);
    }

    /**
     * 设置订阅 和 所在的线程环境
     */
    public <T> void toSubscribe(Observable<T> o, DisposableObserver<T> s) {
        o.subscribeOn(SchedulerProviderUtil.io())
                .unsubscribeOn(SchedulerProviderUtil.io())
                .observeOn(SchedulerProviderUtil.ui())
                .retry(RetryCount)//请求失败重连次数
                .subscribe(s);
    }
}
