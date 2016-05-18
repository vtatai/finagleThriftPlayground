package com.example

import com.twitter.finagle.example.thriftscala.Hello
import com.twitter.finagle.thrift.{ClientId, ThriftClientRequest}
import com.twitter.finagle._
import com.twitter.finagle.example.thriftscala.Hello.FinagledClient
import com.twitter.util.{Await, Future}

object ThriftMuxClient {
  def main(args: Array[String]) {
    val filter = new SimpleFilter[ThriftClientRequest, Array[Byte]] {
      override def apply(request: ThriftClientRequest, service: Service[ThriftClientRequest, Array[Byte]]): Future[Array[Byte]] = {
        println("Filtered")
        service(request)
      }
    }
    val thriftService = ThriftMux.client.newService("localhost:8081", "service")
    val client = new FinagledClient(filter andThen thriftService)
    println(Await.result(client.hi()))
  }
}
