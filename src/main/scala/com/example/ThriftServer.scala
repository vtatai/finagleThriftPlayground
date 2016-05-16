package com.example

import com.twitter.finagle.Thrift
import com.twitter.finagle.example.thriftscala.Hello
import com.twitter.finagle.thrift.ClientId
import com.twitter.util.{Await, Future}

object ThriftServer {

  def main(args: Array[String]) {
//    Thrift.server.serve("localhost:8081", new Hello)
    val server = Thrift.serveIface("localhost:8081", new Hello[Future] {
      def hi() = {
        println("received request")
        println(s"Client id: ${ClientId.current}")
        Future("hi there")
      }

      override def hello(): Future[String] = {
        Future("hello there")
      }
    })
    Await.ready(server)
  }
}
