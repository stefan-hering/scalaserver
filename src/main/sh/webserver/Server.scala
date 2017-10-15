package sh.webserver

import java.net.ServerSocket
import java.util.concurrent.{ExecutorService, Executors}

import sh.webserver.request.{Request, RequestMapping}

import scala.annotation.tailrec

class Server(val port: Int,
             val mappings: List[RequestMapping]) {
  def start() {
    val server = new ServerSocket(port)
    val pool = Executors.newFixedThreadPool(8)
    listen(server, pool)
  }

  @tailrec
  private def listen(server : ServerSocket,pool : ExecutorService) {
    val socket = server.accept()
    pool.execute(new RequestHandler(socket, List()))
    listen(server, pool)
  }
}
