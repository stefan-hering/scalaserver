package sh.webserver.request

import sh.webserver.RequestMethod


class Request(val method : RequestMethod,
              val path: String,
              val parameters: Map[String,String],
              val headers: Map[String,String]) {
}
