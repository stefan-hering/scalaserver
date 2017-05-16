package sh.webserver

import java.io.{BufferedReader, InputStream}
import java.net.Socket
import java.nio.channels.{Channel, Channels, ReadableByteChannel}

import scala.io.Source

class RequestHandler(socket: Socket) extends Runnable {
  def run() {
    val (request, requestHeaders) = readHeaders(socket.getInputStream)

    socket.getOutputStream.write("HTTP/1.1 200 OK\nCache-Control: no-cache\n\nJunge".getBytes)
    socket.getOutputStream.write(System.currentTimeMillis.toString.getBytes)
    socket.getOutputStream.close
  }

  def readHeaders(inputStream : InputStream): (String, Iterator[String]) = {
    val reader : BufferedReader = new BufferedReader(Source.createBufferedSource(inputStream).reader)
    val request = reader.readLine();
    val requestLines = Iterator.continually(reader.readLine())
      .takeWhile(_ != "")
    (request, requestLines)
  }

  def parseHeader(lines : Iterator[String]): Map[String,String] = {
    lines
      .map(_.split(":", 2))
      .filter(v => v.length >= 2)
      .map(h => h(0) -> h(1))
      .toMap
  }
}