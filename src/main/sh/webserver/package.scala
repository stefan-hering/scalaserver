package sh

package object webserver {

  sealed trait RequestMethod

  case object GET extends RequestMethod
  case object POST extends RequestMethod
  case object PUT extends RequestMethod
  case object PATCH extends RequestMethod
  case object DELETE extends RequestMethod
}