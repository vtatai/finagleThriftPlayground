package com.example

import com.twitter.finagle.example.thriftscala.Echo
import com.twitter.finagle.thrift.{ClientId, ThriftClientRequest}
import com.twitter.finagle.{Service, SimpleFilter, Thrift}
import com.twitter.util.{Await, Future}

object EchoClient {
  def main(args: Array[String]) {
    val client = Thrift.client
      .withClientId(ClientId("CLIENT ID HARD TO MISS"))
      .filtered(new SimpleFilter[ThriftClientRequest, Array[Byte]] {
        override def apply(request: ThriftClientRequest, service: Service[ThriftClientRequest, Array[Byte]]): Future[Array[Byte]] = {
          println("Filtered")
          service(request)
        }
      })
      .newIface[Echo.FutureIface]("localhost:8081", "SPECIAL LABEL HARD TO MISS")
    println(Await.result(client.ping("john")))
  }
}
