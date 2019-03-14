package com.na.net;

/**
 * Created by oneal23 on 2018/6/26.
 */
public interface IHttpHeaderManager {

    IHttpHeader getDefaultHeader();

    IHttpHeader getPrivateHeader();
}
