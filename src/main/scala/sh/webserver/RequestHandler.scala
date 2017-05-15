package sh.webserver

import java.io.BufferedReader
import java.net.Socket
import java.nio.channels.{Channel, Channels, ReadableByteChannel}

import scala.io.Source

class RequestHandler(socket: Socket) extends Runnable {
  def run() {
    val reader : BufferedReader = new BufferedReader(Source.createBufferedSource(socket.getInputStream).reader)

    val httpFields = Iterator.continually(reader.readLine())
      .takeWhile(_ != "")
      .map(_.split(":", 2))
      .filter(_.length >= 2)
      .map(arr => arr(0) -> arr(1))
      .toMap

    socket.getOutputStream.write("HTTP/1.1 200 OK\nCache-Control: no-cache\n\nJunge".getBytes)
    socket.getOutputStream.write(System.currentTimeMillis.toString.getBytes)
    socket.getOutputStream.close
  }
}