package sh.webserver.request

import java.io.{InputStream, OutputStream}
import java.net.Socket

class Request(socket: Socket) {

  def inputStream():InputStream = {
    return socket.getInputStream
  }

  def outputStream():OutputStream = {
    return socket.getOutputStream
  }
}
