package sh.webserver

import java.net.Socket

import sh.webserver.railway.Rail
import sh.webserver.railway.Result
import sh.webserver.railway.Result._
import sh.webserver.request.{Request, RequestParser}

class RequestHandler(socket: Socket) extends Runnable {
  def validateHTTPMethod : Request => Result.Value = (request: Request) => {
    request.method match {
      case "GET" => SUCCESS
      case _ => FAILURE
    }
  }
  def validatePath : Request => Result.Value = (request: Request) => {
    request.path match {
      case "fail" => FAILURE
      case _ => SUCCESS
    }
  }
  def validateParameters : Request => Result.Value = (request: Request) => {
    request.parameters("fail") match {
      case "true" => FAILURE
      case _ => SUCCESS
    }
  }
  def validateHeaders : Request => Result.Value = (request: Request) => {
    request.headers("Host") match {
      case "localhost" => SUCCESS
      case _ => FAILURE
    }
  }

  def validateEverything : Request => Result.Value =
    Rail.begin(validateHTTPMethod) andThen
      Rail.switch(validatePath) andThen
      Rail.switch(validateHeaders) andThen
      Rail.end(validateParameters)

  def validatePathAndQuery : Request => Result.Value = Rail.begin(validatePath) andThen Rail.end(validateHeaders)
  def validateMethodAndQuery : Request => Result.Value = Rail.begin(validateHTTPMethod) andThen Rail.end(validateHeaders)



  def run() : Unit= {
    val request = RequestParser.parseFullRequest(socket.getInputStream)

    validateEverything(request)

    socket.getOutputStream.write("HTTP/1.1 200 OK\nCache-Control: no-cache\n\nJunge".getBytes)
    socket.getOutputStream.write(System.currentTimeMillis.toString.getBytes)
    socket.getOutputStream.close()
  }

}