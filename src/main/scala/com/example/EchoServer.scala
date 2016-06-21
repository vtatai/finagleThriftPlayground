package com.example

import com.twitter.finagle.example.thriftscala.Echo
import com.twitter.finagle.example.thriftscala.Echo.FutureIface
import com.twitter.finagle.example.thriftscala.Echo.Ping.{Args, Result}
import com.twitter.finagle.{Service, SimpleFilter, Thrift}
import com.twitter.util.{Await, Future}

object EchoServer {

  def main(args: Array[String]) {
//    val filter = new SimpleFilter[Echo.Ping.Args, Echo.Ping.Result] {
//      override def apply(request: Args, service: Service[Args, Result]): Future[Result] = {
//        println(s"filtered ${request.message}")
//        service(request)
//      }
//    }
    val pingService = new Service[Echo.Ping.Args, Echo.Ping.Result] {
      override def apply(request: Echo.Ping.Args): Future[Echo.Ping.Result] = {
        println(request.message)
        Future(Echo.Ping.Result(Some(s"ping: ${request.message}")))
      }
    }
    val serviceImpl = Echo.ServiceIface(ping = pingService)
    val server = Thrift.serveIface("localhost:8081", Echo.MethodIfaceBuilder.newMethodIface(serviceImpl))
//    val server = Thrift.serveIface("localhost:8081", new FutureIface {
//        override def ping(message: String): Future[String] = Future(s"ping: ${message}")
//      }
//    )
    Await.ready(server)
  }
}
