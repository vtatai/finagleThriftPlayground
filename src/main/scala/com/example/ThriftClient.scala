package com.example

import com.twitter.finagle.Thrift
import com.twitter.finagle.example.thriftscala.Hello
import com.twitter.finagle.thrift.ClientId
import com.twitter.util.Await

object ThriftClient {
  def main(args: Array[String]) {
    val client = Thrift.client
      .withClientId(ClientId("CLIENT ID HARD TO MISS"))
      .newIface[Hello.FutureIface]("localhost:8081", "SPECIAL LABEL HARD TO MISS")
    println(Await.result(client.hi()))
  }
}
