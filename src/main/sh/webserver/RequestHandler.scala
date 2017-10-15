package sh.webserver

import java.net.Socket

import sh.webserver.railway.Result._
import sh.webserver.request.{RequestMapping, RequestParser}

class RequestHandler(socket: Socket,
                     mapping: List[RequestMapping]) extends Runnable {

  def run() : Unit = {
    val request = RequestParser.parseFullRequest(socket.getInputStream)

    mapping.foreach((m) => {
      if(m.path.equals(request.path)){
        m.fullValidation(request) match {
          case SUCCESS => success(m.f(request))
          case FAILURE => failure()
        }
      }
    })
  }

  def success(out: String) : Unit = {
    socket.getOutputStream.write("HTTP/1.1 200 OK\nCache-Control: no-cache\n\n".getBytes)
    socket.getOutputStream.write(out.getBytes)
    socket.getOutputStream.close()
  }

  def failure() : Unit = {
    socket.getOutputStream.write("HTTP/1.1 500 INTERNAL SERVER ERROR\nCache-Control: no-cache\n\n".getBytes)
    socket.getOutputStream.write("It broke".getBytes)
    socket.getOutputStream.close()
  }
}