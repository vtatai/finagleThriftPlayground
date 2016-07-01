package com.example;

import com.twitter.finagle.Thrift;
import com.twitter.finagle.example.thriftjava.Echo;
import com.twitter.finagle.thrift.ClientId;
import com.twitter.util.Await;
import com.twitter.util.Function0;

public class JavaEchoClient {
    public static void main(String[] args) throws Exception {
//        Echo.FutureIface client = Thrift.client().newIface("localhost:8081", "label", Echo.FutureIface.class);
//        System.out.println(Await.result(client.ping("john")));
    }
}
