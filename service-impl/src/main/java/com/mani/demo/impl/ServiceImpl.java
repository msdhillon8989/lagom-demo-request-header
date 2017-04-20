package com.mani.demo.impl;

import akka.NotUsed;
import akka.japi.Function;
import com.lightbend.lagom.javadsl.api.ServiceCall;
import com.lightbend.lagom.javadsl.api.transport.Forbidden;
import com.lightbend.lagom.javadsl.api.transport.RequestHeader;
import com.lightbend.lagom.javadsl.server.HeaderServiceCall;
import com.lightbend.lagom.javadsl.server.ServerServiceCall;
import com.mani.demo.ServiceAPI;
import com.mani.demo.Utilities;


import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.completedFuture;


public class ServiceImpl implements ServiceAPI {

    public static Map<String, String> parseQueryString(String qs) {
        Map<String, String> result = new HashMap<>();
        if (qs == null)
            return result;

        int last = 0, next, l = qs.length();
        while (last < l) {
            next = qs.indexOf('&', last);
            if (next == -1)
                next = l;

            if (next > last) {
                int eqPos = qs.indexOf('=', last);
                try {
                    if (eqPos < 0 || eqPos > next)
                        result.put(URLDecoder.decode(qs.substring(last, next), "utf-8"), "");
                    else
                        result.put(URLDecoder.decode(qs.substring(last, eqPos), "utf-8"), URLDecoder.decode(qs.substring(eqPos + 1, next), "utf-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
            last = next + 1;
        }
        return result;
    }

    @Override
    public ServiceCall<NotUsed, String> method1() {

        return readHeader(
            new Function<String, ServerServiceCall<NotUsed, String>>() {
                @Override
                public ServerServiceCall<NotUsed, String> apply(String param) throws Exception {
                    return request -> {
                            return completedFuture(Utilities.ok(null, parseQueryString(param).toString()));
                    };
                }
            });
    }




    public <Request, Response> ServerServiceCall<Request, Response> readHeader(Function<String, ServerServiceCall<Request, Response>> serviceCall) {
        return HeaderServiceCall.composeAsync(new java.util.function.Function<RequestHeader, CompletionStage<? extends ServerServiceCall<Request, Response>>>() {
            @Override
            public CompletionStage<? extends ServerServiceCall<Request , Response>> apply(RequestHeader requestHeader) {
                CompletableFuture<String> uri =  CompletableFuture.supplyAsync(()->requestHeader.uri().getRawQuery().toString());
                return uri.thenApply(query->
                {
                    try {
                        return serviceCall.apply(query);
                    } catch (Exception e) {
                        e.printStackTrace();
                        throw new Forbidden("Bad request "+e.getMessage());
                    }
                }
                );
            }
        });
    }




}
