package sh.webserver.validation

import sh.webserver.RequestMethod
import sh.webserver.railway.Result
import sh.webserver.railway.Result.{FAILURE, SUCCESS}
import sh.webserver.request.Request

object ValidationMethods {
  def validateHTTPMethod : (List[RequestMethod],Request) => Result.Value =
      (methods: List[RequestMethod],request: Request) => {
    if(request.method == null){
      FAILURE
    } else if(! methods.contains(request.method)){
      FAILURE
    } else {
      SUCCESS
    }
  }

  def validatePath : (String, Request) => Result.Value =
      (path: String, request:Request) => {
    if(request.path.equals(path)){
      SUCCESS
    } else {
      FAILURE
    }
  }
}
