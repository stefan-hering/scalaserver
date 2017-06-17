package sh.webserver.request

import java.io.{BufferedReader, InputStream}

import scala.io.Source

/**
  *
  */
object RequestParser {
  def parseFullRequest(inputStream : InputStream): Request = {
    val reader : BufferedReader = new BufferedReader(Source.createBufferedSource(inputStream).reader)
    val firstLine = reader.readLine()
    val requestLines = Iterator.continually(reader.readLine())
      .takeWhile(_ != "")

    val parsedFirstLine = parseFirstLine(firstLine)

    new Request(parsedFirstLine._1, parsedFirstLine._2,parsedFirstLine._3, parseHeaders(requestLines))
  }

  def parseHeaders(lines : Iterator[String]): Map[String,String] = {
    lines
      .map(_.split(":", 2))
      .filter(v => v.length >= 2)
      .map(h => h(0) -> h(1))
      .toMap
  }

  /**
    *
    * @param firstLine The first line of a request
    * @return A tuple containing method, query and parameters
    */
  def parseFirstLine(firstLine : String): (String, String, Map[String,String]) = {
    val splitFirstLine = firstLine.split(' ')
    val query = splitFirstLine(1).split("\\?",2)
    var params : Map[String,String] = null
    if(query.length == 2) {
      params = query(1)
                  .split('&')
                  .map(_.split("=",2))
                  .filter(_.length == 2)
                  .map(h => h(0) -> h(1))
                  .toMap
    } else {
      params = Map.empty
    }

    (splitFirstLine(0),query(0),params)
  }
}
