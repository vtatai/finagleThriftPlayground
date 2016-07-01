package com.example

import com.twitter.finagle.example.thriftscala.Hello
import com.twitter.finagle.example.thriftscala.Hello.Hi.{Args, Result}
import com.twitter.finagle.{Service, SimpleFilter, ThriftMux}
import com.twitter.util.{Await, Future}

object ThriftMuxServer {

  def main(args: Array[String]) {
    val filter = new SimpleFilter[Hello.Hi.Args, Hello.Hi.Result] {
      override def apply(request: Args, service: Service[Args, Result]): Future[Result] = {
        println("hahaha")
        service(request)
      }
    }
    val hiService = new Service[Hello.Hi.Args, Hello.Hi.Result] {
      override def apply(request: Hello.Hi.Args): Future[Hello.Hi.Result] = {
        Future(Hello.Hi.Result(Some("hi!")))
      }
    }
    val helloService = new Service[Hello.Hello.Args, Hello.Hello.Result] {
      override def apply(request: Hello.Hello.Args): Future[Hello.Hello.Result] = {
        Future(Hello.Hello.Result(Some("hello!")))
      }
    }
    val noAnswerService = new Service[Hello.NoAnswer.Args, Hello.NoAnswer.Result] {
      override def apply(request: Hello.NoAnswer.Args): Future[Hello.NoAnswer.Result] = {
        Future(Hello.NoAnswer.Result())
      }
    }
    val serviceImpl = Hello.ServiceIface(hi = filter andThen hiService, hello = helloService, noAnswer = noAnswerService)
    val server = ThriftMux.serveIface("localhost:8081", Hello.MethodIfaceBuilder.newMethodIface(serviceImpl))
    Await.ready(server)
  }
}
