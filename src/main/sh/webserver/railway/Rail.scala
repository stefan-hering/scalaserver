package sh.webserver.railway

import sh.webserver.request.Request
import sh.webserver.railway.Result._

/**
  * A take on the railway oriented programming idea as seen here:
  * https://fsharpforfunandprofit.com/posts/recipe-part2/
  *
  * Used to chain functions that operate on the same Type together. Once one of the functions returns a Result.FAILURE, the remaining functions in the chain will not be called.
  */
object Rail {
  /**
    * Starts a rail in order to chain functions that return an @Result
    * @param startFunction The first function to be called
    * @tparam A The type the to be chained functions operate on
    * @return The same function returning a 2-track output start the rail
    */
  def begin[A](startFunction : A => Result.Value) : (A => (Result.Value, A)) = {
    (input: A) => {
      (startFunction(input), input)
    }
  }
  /**
    * Middle piece to continue a rail-chain
    * @param switchFunction The function to be bound between other functions
    * @tparam A The type the to be chained functions operate on
    * @return The same function accepting a 2-track input and returning a 2-track output
    */
  def switch[A](switchFunction : A => Result.Value) : (((Result.Value, A)) => (Result.Value, A)) = {
    ((lastResult : Result, input: A) => {
      lastResult match {
        case SUCCESS => (switchFunction(input), input)
        case FAILURE => (FAILURE, input)
      }
    }).tupled
  }

  /**
    * End piece to a rail-chain
    * @param endFunction The last function to be called
    * @tparam A The type the to be chained functions operate on
    * @return The same function accepting a 2 track input
    */
  def end[A](endFunction : A => Result.Value) : (((Result.Value, A)) => Result.Value) = {
    ((lastResult : Result, input: A) => {
      lastResult match {
        case SUCCESS => endFunction(input)
        case FAILURE => FAILURE
      }
    }).tupled
  }
}
