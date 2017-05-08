package sh.webserver

import java.net.Socket

class RequestHandler(socket: Socket) extends Runnable {
  def run() {
    socket.getOutputStream.write("HTTP/1.x 200 OK\nCache-Control: no-cache\n\nJunge".getBytes);
    socket.getOutputStream.write(System.currentTimeMillis.toString.getBytes);
    socket.getOutputStream.close();
  }
}