package com.example

import com.twitter.finagle.example.thriftscala.Hello
import com.twitter.finagle.thrift.ClientId
import com.twitter.finagle.{Service, SimpleFilter, Thrift}
import com.twitter.util.{Await, Future}

object ThriftGlobalFilterServer {
  def main(args: Array[String]) {
    val iface = new Hello.FutureIface {
      def hi() = {
        println("received request")
        println(s"Client id: ${ClientId.current}")
        Future("hi there")
      }
      override def hello(): Future[String] = {
        Future("hello there")
      }
    }
    val filter = new SimpleFilter[Array[Byte], Array[Byte]] {
      override def apply(request: Array[Byte], service: Service[Array[Byte], Array[Byte]]): Future[Array[Byte]] = {
        println("filtered")
        service(request)
      }
    }
    val service = new Hello.FinagledService(iface, Thrift.protocolFactory)
    val server = Thrift.serve("localhost:8081", filter andThen service)
    Await.ready(server)
  }
}
