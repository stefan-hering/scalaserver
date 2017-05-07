package sh.webserver

import java.net.ServerSocket
import java.util.concurrent.Executors

class Server(port: Int) extends Runnable {
  def run() {
    val server = new ServerSocket(port);
    val pool = Executors.newFixedThreadPool(8);
    while(true){
      val socket = server.accept();
      pool.execute(new RequestHandler(socket));
      
    }
  }
}
