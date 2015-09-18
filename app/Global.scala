import javax.inject.Inject
import play.api.http.HttpFilters

import custom_filter._

class Filters @Inject() (
  log: LoggingFilter
) extends HttpFilters {

  val filters = Seq(log)
}
