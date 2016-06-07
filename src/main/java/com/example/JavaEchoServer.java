package com.example;

import com.twitter.finagle.Service;
import com.twitter.finagle.Thrift;
import com.twitter.finagle.example.thriftjava.Echo;
import com.twitter.util.Await;
import com.twitter.util.Future;
import com.twitter.util.TimeoutException;


public class JavaEchoServer {
    public static void main(String[] args) throws InterruptedException, TimeoutException {
//        Await.ready(Thrift.server()
//                .serveIface("localhost:8081", (Echo.ServiceIface) message -> Future.value("Java: " + message)));
        Service<Echo.Ping.Args, Echo.Ping.Result> ping = new Service<Echo.Ping.Args, Echo.Ping.Result>() {
            @Override
            public Future<Echo.Ping.Result> apply(Echo.Ping.Args request) {
                return Future.value(new Echo.Ping.Result("Ping: " + request.getMessage()));
            }
        };
        Await.ready(Thrift.server().serveIface("localhost:8081", new Echo.MethodIface(new Echo.ServiceIface(ping))));
    }
}
