package sh.webserver.railway

import sh.webserver.request.Request
import sh.webserver.railway.Result._

object Rail {

  def begin[A](startFunction : A => Result.Value) : (A => (Result.Value, A)) = {
    (input: A) => {
      (startFunction(input), input)
    }
  }
  /**
    * Binds a function that returns a @Result to a 2-track input
    * @param switchFunction The function to be bound
    * @return The same function accepting a 2-track input
    */
  def switch[A](switchFunction : A => Result.Value) : (((Result.Value, A)) => (Result.Value, A)) = {
    ((lastResult : Result, input: A) => {
      lastResult match {
        case SUCCESS => (switchFunction(input), input)
        case FAILURE => (FAILURE, input)
      }
    }).tupled
  }

  def end[A](endFunction : A => Result.Value) : (((Result.Value, A)) => Result.Value) = {
    ((lastResult : Result, input: A) => {
      lastResult match {
        case SUCCESS => endFunction(input)
        case FAILURE => FAILURE
      }
    }).tupled
  }
}
