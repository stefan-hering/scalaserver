package sh.webserver

import java.io.{BufferedReader, InputStream}
import java.net.Socket
import java.nio.channels.{Channel, Channels, ReadableByteChannel}

import sh.webserver.railway.Switch
import sh.webserver.railway.Result._
import sh.webserver.request.Request

import scala.io.Source

class RequestHandler(request: Request) extends Runnable {
  def validateHTTPMethod = (request: Request) => {
    //TODO do something
    (SUCCESS, request)
  }
  def validatePath = Switch.bind((request: Request) => {
    //TODO do something
    (FAILURE, request)
  })
  def validateQuery = Switch.bind((request: Request) => {
    //TODO do something
    (SUCCESS, request)
  })

  def validationRailyway = validateHTTPMethod andThen validatePath andThen validateQuery;

  def readHeaders(inputStream : InputStream): (String, Iterator[String]) = {
    val reader : BufferedReader = new BufferedReader(Source.createBufferedSource(inputStream).reader)
    val firstLine = reader.readLine();
    val requestLines = Iterator.continually(reader.readLine())
      .takeWhile(_ != "")
    (firstLine, requestLines)
  }

  def parseHeader(lines : Iterator[String]): Map[String,String] = {
    lines
      .map(_.split(":", 2))
      .filter(v => v.length >= 2)
      .map(h => h(0) -> h(1))
      .toMap
  }

  def run() = {
    val (firstLine, requestHeaders) = readHeaders(request.inputStream)

    validationRailyway(request);

    request.outputStream.write("HTTP/1.1 200 OK\nCache-Control: no-cache\n\nJunge".getBytes)
    request.outputStream.write(System.currentTimeMillis.toString.getBytes)
    request.outputStream.close
  }

}