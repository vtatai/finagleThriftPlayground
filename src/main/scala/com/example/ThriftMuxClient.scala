package com.example

import java.net.SocketAddress

import com.twitter.finagle._
import com.twitter.finagle.example.thriftscala.Hello
import com.twitter.finagle.param.Label
import com.twitter.finagle.thrift.ThriftClientRequest
import com.twitter.util.{Await, Future}

object ThriftMuxClient {
  def main(args: Array[String]) {
    val futureIface = FilteredThriftMux.newIface[Hello.FutureIface]("localhost:8081")
    println(Await.result(futureIface.hi()))
  }
}

object FilteredThriftMux extends Client[ThriftClientRequest, Array[Byte]]
  with ThriftRichClient
  with Server[Array[Byte], Array[Byte]] with ThriftRichServer {
  import ThriftMux._

  val filter = new SimpleFilter[ThriftClientRequest, Array[Byte]] {
    override def apply(request: ThriftClientRequest, service: Service[ThriftClientRequest, Array[Byte]]): Future[Array[Byte]] = {
      println("Filtered")
      service(request)
    }
  }

  override def newService(dest: Name, label: String): Service[ThriftClientRequest, Array[Byte]] = {
    filter andThen ThriftMux.newService(dest, label)
  }

  override def newClient(dest: Name, label: String): ServiceFactory[ThriftClientRequest, Array[Byte]] = ThriftMux.newClient(dest, label)

  override protected def params: Stack.Params = client.params

  override protected lazy val Label(defaultClientName) = Thrift.client.params[Label]

  override def serve(addr: SocketAddress, service: ServiceFactory[Array[Byte], Array[Byte]]): ListeningServer = ThriftMux.serve(addr, service)

  override protected val Thrift.param.ProtocolFactory(protocolFactory) = Thrift.client.params[Thrift.param.ProtocolFactory]
}
