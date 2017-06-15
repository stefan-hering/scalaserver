package sh.webserver.test

import org.scalatest._
import sh.webserver.Server

class ServerSpec extends FlatSpec with Matchers {
  "A Server" should "connect to a port" in {
    val server = new Server(1234);
  }
}
