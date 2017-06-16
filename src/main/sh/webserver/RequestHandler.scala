package sh.webserver

import java.io.{BufferedReader, InputStream}

import sh.webserver.railway.Rail
import sh.webserver.railway.Result
import sh.webserver.railway.Result._
import sh.webserver.request.Request

import scala.io.Source

class RequestHandler(request: Request) extends Runnable {
  def validateHTTPMethod : Request => Result.Value = (request: Request) => {
    //TODO do something
    SUCCESS
  }
  def validatePath : Request => Result.Value = (request: Request) => {
    //TODO do something
    SUCCESS
  }
  def validateParameters : Request => Result.Value = (request: Request) => {
    //TODO do something
    FAILURE
  }
  def validateQuery : Request => Result.Value = (request: Request) => {
    //TODO do something
    SUCCESS
  }

  def validateEverything : Request => Result.Value =
    Rail.begin(validateHTTPMethod) andThen
      Rail.switch(validatePath) andThen
      Rail.switch(validateQuery) andThen
      Rail.end(validateParameters)

  def validatePathAndQuery : Request => Result.Value = Rail.begin(validatePath) andThen Rail.end(validateQuery)
  def validateMethodAndQuery : Request => Result.Value = Rail.begin(validateHTTPMethod) andThen Rail.end(validateQuery)

  def readHeaders(inputStream : InputStream): (String, Iterator[String]) = {
    val reader : BufferedReader = new BufferedReader(Source.createBufferedSource(inputStream).reader)
    val firstLine = reader.readLine()
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

  def run() : Unit= {
    val (firstLine, requestHeaders) = readHeaders(request.inputStream())

    validateEverything(request)

    request.outputStream().write("HTTP/1.1 200 OK\nCache-Control: no-cache\n\nJunge".getBytes)
    request.outputStream().write(System.currentTimeMillis.toString.getBytes)
    request.outputStream().close()
  }

}