package sh.webserver.request

import java.io.{InputStream, OutputStream}
import java.net.Socket

class Request(socket: Socket) {

  def inputStream():InputStream = {
    socket.getInputStream
  }

  def outputStream():OutputStream = {
    socket.getOutputStream
  }
}
