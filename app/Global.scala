import javax.inject.Inject
import play.api.http.HttpFilters

import customfilter._

class Filters @Inject() (
  log: LoggingFilter
) extends HttpFilters {

  val filters = Seq(log)
}
