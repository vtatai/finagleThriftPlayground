package com.example

import com.twitter.finagle.{Service, SimpleFilter, Thrift}
import com.twitter.finagle.example.thriftscala.Hello.{FinagledService, FutureIface}
import com.twitter.finagle.example.thriftscala.{Hello, Hello$FinagleService}
import com.twitter.finagle.thrift.ClientId
import com.twitter.util.{Await, Future}
import org.apache.thrift.protocol.{TProtocol, TProtocolFactory}

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

    finagledService.addFunction("hi", new WrapperFunction(finagledService.createHiFunction()).apply)
    val server = Thrift.server.serve("localhost:8081", finagledService)
    Await.ready(server)
  }
}

class WrapperFunction(wrapped: ((TProtocol, Int) => Future[Array[Byte]])) {
  def apply(prot: TProtocol, v: Int): Future[Array[Byte]] = {
    println("FOOOOOO")
    wrapped(prot, v)
  }
}

class MyFinagledService(
                         iface: FutureIface,
                         protocolFactory: TProtocolFactory)
  extends Hello$FinagleService(
    iface,
    protocolFactory) {

  override def addFunction(name: String, f: (TProtocol, Int) => Future[Array[Byte]]) {
    functionMap(name) = f
  }
}
