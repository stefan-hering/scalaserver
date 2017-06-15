package sh.webserver.railway

import sh.webserver.request.Request
import sh.webserver.railway.Result._

object Switch {
  /**
    * Binds a function that returns a @Result to a 2-track input
    * @param switchFunction The function to be bound
    * @return The same function accepting a 2-track input
    */
  //TODO should make it generic, so Request can be anything
  def bind(switchFunction : Request => (Result.Value, Request)) : (((Result.Value, Request)) => (Result.Value, Request)) = {
    ((a : Result, b: Request) => {
      a match {
        case SUCCESS => switchFunction(b)
        case FAILURE => (FAILURE, null)
      }
    }).tupled
  }
}
