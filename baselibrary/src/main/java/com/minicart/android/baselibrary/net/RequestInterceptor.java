package com.minicart.android.baselibrary.net;


import android.support.annotation.Nullable;

import com.minicart.android.baselibrary.support.CharactorHandler;
import com.minicart.android.baselibrary.support.ZipHelper;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Singleton;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import timber.log.Timber;

/**
 * @类名：BaseInterceptor
 * @描述：
 * @创建人：54506
 * @创建时间：2017/1/13 10:05
 * @版本：
 */
@Singleton
public class RequestInterceptor implements Interceptor {
    private String TAG = getClass().getSimpleName();
    private GlobalHttpHandler mHandler;

    @Inject
    public RequestInterceptor(@Nullable GlobalHttpHandler handler) {
        this.mHandler = handler;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
//                .addHeader("Content-Type", "application/json")  okhttp会默认添加这个头
        long t1 = System.nanoTime();
        Response originalResponse = chain.proceed(request);
        long t2 = System.nanoTime();

        boolean hasRequestBody = request.body() != null;
        String bodySize = originalResponse.body().contentLength() != -1 ? originalResponse.body().contentLength() + "-byte" : "unknown-length";

        //打印请求信息
        Timber.tag(getTag(request, "Request_Info"))
                .w("「 %s 」%nParams : 「 %s 」%nConnection : 「 %s 」%nHeaders : %n「 %s 」",
                        request.url(),
                        hasRequestBody ? parseParams(request.newBuilder().build().body()) : "Null",
                        chain.connection(),
                        request.headers());

        //打印响应时间以及响应头
        Timber.tag(getTag(request, "Response_Info")).w("「 %s 」%nReceived response in [ %d-ms ] , [ %s ]%n%s",
                request.url(),
                TimeUnit.NANOSECONDS.toMillis(t2 - t1),
                bodySize,
                originalResponse.headers());
        //打印响应结果
        String bodyString = printResult(request, originalResponse);
        if (mHandler != null) {//这里可以比客户端提前一步拿到服务器返回的结果,可以做一些操作,比如token超时,重新获取
            return mHandler.onHttpResultResponse(bodyString, chain, originalResponse);
        }
        return originalResponse;
    }

    /**
     * 打印响应结果
     *
     * @param request
     * @param originalResponse
     * @return
     * @throws IOException
     */
    @Nullable
    private String printResult(Request request, Response originalResponse) throws IOException {
        //读取服务器返回的结果
        ResponseBody responseBody = originalResponse.body();
        String bodyString = null;
        if (isParseable(responseBody.contentType())) {
            BufferedSource source = responseBody.source();
            source.request(Long.MAX_VALUE); // Buffer the entire body.
            Buffer buffer = source.buffer();

            //获取content的压缩类型
            String encoding = originalResponse
                    .headers()
                    .get("Content-Encoding");

            Buffer clone = buffer.clone();
            //解析response content
            bodyString = parseContent(responseBody, encoding, clone);

            Timber.tag(getTag(request, "Response_Result"))
                    .w(isJson(responseBody.contentType()) ? "「 %s 」%n" + CharactorHandler.jsonFormat(bodyString) : "「 %s 」%n" + bodyString,
                            request.url());

        } else {
            Timber.tag(getTag(request, "Response_Result"))
                    .w("「 %s 」%nThis result isn't parsed",
                            request.url());
        }
        return bodyString;
    }

    private String getTag(Request request, String tag) {
//        return String.format(" [%s] 「 %s 」>>> %s", request.method(), request.url().toString(), tag);
        return String.format(" [%s] >>> %s", request.method(), tag);
    }

    /**
     * 解析服务器响应的内容
     *
     * @param responseBody
     * @param encoding
     * @param clone
     * @return
     */
    private String parseContent(ResponseBody responseBody, String encoding, Buffer clone) {
        Charset charset = Charset.forName("UTF-8");
        MediaType contentType = responseBody.contentType();
        if (contentType != null) {
            charset = contentType.charset(charset);
        }
        if (encoding != null && encoding.equalsIgnoreCase("gzip")) {//content使用gzip压缩
            return ZipHelper.decompressForGzip(clone.readByteArray(), convertCharset(charset));//解压
        } else if (encoding != null && encoding.equalsIgnoreCase("zlib")) {//content使用zlib压缩
            return ZipHelper.decompressToStringForZlib(clone.readByteArray(), convertCharset(charset));//解压
        } else {//content没有被压缩
            return clone.readString(charset);
        }
    }

    public static String parseParams(RequestBody body) throws UnsupportedEncodingException {
        if (isParseable(body.contentType())) {
            try {
                Buffer requestbuffer = new Buffer();
                body.writeTo(requestbuffer);
                Charset charset = Charset.forName("UTF-8");
                MediaType contentType = body.contentType();
                if (contentType != null) {
                    charset = contentType.charset(charset);
                }
                return URLDecoder.decode(requestbuffer.readString(charset), convertCharset(charset));

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "This params isn't parsed";
    }

    public static boolean isParseable(MediaType mediaType) {
        if (mediaType == null) return false;
        return mediaType.toString().toLowerCase().contains("text")
                || isJson(mediaType) || isForm(mediaType)
                || isHtml(mediaType) || isXml(mediaType);
    }

    public static boolean isJson(MediaType mediaType) {
        return mediaType.toString().toLowerCase().contains("json");
    }

    public static boolean isXml(MediaType mediaType) {
        return mediaType.toString().toLowerCase().contains("xml");
    }

    public static boolean isHtml(MediaType mediaType) {
        return mediaType.toString().toLowerCase().contains("html");
    }

    public static boolean isForm(MediaType mediaType) {
        return mediaType.toString().toLowerCase().contains("x-www-form-urlencoded");
    }

    public static String convertCharset(Charset charset) {
        String s = charset.toString();
        int i = s.indexOf("[");
        if (i == -1)
            return s;
        return s.substring(i + 1, s.length() - 1);
    }
}
