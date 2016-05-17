package com.example

import com.twitter.finagle.example.thriftscala.Hello
import com.twitter.finagle.thrift.ClientId
import com.twitter.finagle.{Service, SimpleFilter, Thrift}
import com.twitter.util.{Await, Future}
import org.apache.thrift.transport.TMemoryInputTransport

object ThriftServer {

  def main(args: Array[String]) {
    val thriftServiceImpl = new Hello.FutureIface {
      def hi() = {
        println("received request")
        println(s"Client id: ${ClientId.current}")
        Future("hi there")
      }

      override def hello(): Future[String] = {
        Future("hello there")
      }
    }
    val finagledService: Hello.FinagledService = new Hello.FinagledService(thriftServiceImpl, Thrift.protocolFactory)

    val protocolFactory = Thrift.protocolFactory
    val filter = new SimpleFilter[Array[Byte], Array[Byte]] {
      def apply(req: Array[Byte], service: Service[Array[Byte], Array[Byte]]): Future[Array[Byte]] = {
        val inputTransport = new TMemoryInputTransport(req)
        val iprot = protocolFactory.getProtocol(inputTransport)
        try {
          val msg = iprot.readMessageBegin()
          val func = msg.name
          println(s"Message name: $func")
        } catch {
          case e: Exception => Future.exception(e)
        }
        println("filter executed!!!!")
        service(req)
      }
    }
    val server = Thrift.server.serve("localhost:8081", filter andThen finagledService)
    Await.ready(server)
  }
}
