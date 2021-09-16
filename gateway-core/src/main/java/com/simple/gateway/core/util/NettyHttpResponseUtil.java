package com.simple.gateway.core.util;


import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

/**
 * Netty 的 HttpResponse 处理工具
 */
public class NettyHttpResponseUtil {

    public static  FullHttpResponse success(FullHttpResponse httpResponse) {
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, Unpooled.copiedBuffer(httpResponse.content()));
        response.headers().set(HttpHeaderNames.CONTENT_LENGTH, httpResponse.content().readableBytes());
        return response;
    }

    public static FullHttpResponse success(String msg) {
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, Unpooled.copiedBuffer(msg.getBytes()));
        response.headers().set(HttpHeaderNames.CONTENT_LENGTH, msg.getBytes().length);
        return response;
    }

    /**
     * 超时
     */
    public static FullHttpResponse timeout() {
        String msg = "proxy request timeout";
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, Unpooled.copiedBuffer(msg.getBytes(CharsetUtil.UTF_8)));
        response.headers().set(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());
        return response;
    }

    /**
     * 失败
     */
    public static FullHttpResponse fail(String msg) {
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.INTERNAL_SERVER_ERROR, Unpooled.copiedBuffer(msg.getBytes(CharsetUtil.UTF_8)));
        response.headers().set(HttpHeaderNames.CONTENT_LENGTH.toString(), response.content().readableBytes());
        return response;
    }

}
