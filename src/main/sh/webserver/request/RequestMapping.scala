package sh.webserver.request

import sh.webserver.RequestMethod
import sh.webserver.railway.{Rail, Result}
import sh.webserver.validation.ValidationMethods

class RequestMapping(val path: String,
                     val methods: List[RequestMethod],
                     private val requestValidations: List[Request => Result.Value],
                     val f: Request => String) {

  // TODO there is probably a nicer way to plug everything together
  private def composeValidation(): Request => Result.Value = {
    val begin = Rail.begin(ValidationMethods.validateHTTPMethod(methods, _:Request))
    val end = Rail.end(ValidationMethods.validatePath(path, _:Request))

    var rail = begin andThen Rail.switch(requestValidations(0))
    var isFirst = true
    requestValidations.foreach(validation => {
      if(isFirst){
        isFirst = false
      } else {
        rail = rail andThen Rail.switch(validation)
      }
    })

    return rail andThen end
  }

  def fullValidation = composeValidation()
}
