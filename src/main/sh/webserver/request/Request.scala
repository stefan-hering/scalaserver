package sh.webserver.request


class Request(val method : String, val path: String, val parameters: Map[String,String], val headers: Map[String,String]) {

}
