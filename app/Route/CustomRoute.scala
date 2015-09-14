package play.api.mvc

import play.api.mvc._
import play.core.Router
import scala.runtime.AbstractPartialFunction
import scala.reflect.ClassTag
import play.api.Play
import play.api.routing._
import play.api.Logger

trait ResourceController[T] extends Controller {

  /**
   * Get the index for the resource.
   *
   * Typically this might be a list of interesting resources.
   *
   * This is invoked for GET requests on /myresource.
   */
  def index: EssentialAction

  /**
   * Render the new screen page.
   *
   * Typically this will mean rendering an HTML form.
   *
   * This is invoked for GET requests on /myresource/new.
   */
  def newScreen: EssentialAction

  /**
   * Create a new resource.
   *
   * This is invoked for POST requests on /myresource.
   */
  def create: EssentialAction

  /**
   * Get a given resource.
   *
   * This is invoked for GET requests on /myresource/id.
   *
   * @param id The id of the resource to get.
   */
  def show(id: T): EssentialAction

  /**
   * Render the edit page for a given resource.
   *
   * Typically this will mean rendering an HTML form, prepopulated with the details of the resource.
   *
   * This is invoked for GET requests on /myresource/edit.
   *
   * @param id The id of the resource to edit.
   */
  def edit(id: T): EssentialAction

  /**
   * Update the resource with the given id.
   *
   * This is invoked for PUT requests on /myresource/id.
   *
   * @param id The id of the resource to update.
   */
  def update(id: T): EssentialAction

  /**
   * Delete the resource with the given id.
   *
   * This is invoked for DELETE requests on /myresource/id
   *
   * @param id The id of the resource to delete.
   */
  def destroy(id: T): EssentialAction
}

trait DefaultResourceController[T] extends ResourceController[T] {
  def index = ???
  def newScreen = ???
  def create = ???
  def show(id: T) = ???
  def edit(id: T) = ???
  def update(id: T) = ???
  def destroy(id: T) = ???
}

/**
 * A resource router. Typically it will be implemented by an object, like so:
 *
 * {{{
 *   object MyResource extends ResourceRouter[Long] {
 *     def index = Action {
 *       ...
 *     }
 *     def create(id: Long) = Action {
 *       ...
 *     }
 *
 *     ...
 *   }
 * }}}
 *
 * Then from a routes file can be used like so:
 *
 * {{{
 *   ->   /myresource     controllers.MyResource
 * }}}
 *
 * @param name A custom name for the resource controller.  This is used by the Javascript router.
 * @param idBindable The bindable for converting the id to the expected type.
 * @tparam T
 */
abstract class ResourceRouter[T](name: Option[String] = None)(implicit idBindable: PathBindable[T]) extends play.api.routing.Router with ResourceController[T] {
  self =>

  def withPrefix(prefix: String): Router = { this }
  
  private val controllerName = name.getOrElse {
    val name = self.getClass.getName
    if (name.endsWith("$")) {
      name.dropRight(1)
    } else {
      name
    }
  }

  private var path: String = ""

  private val MaybeSlash = "/?".r
  private val Id = "/([^/]+)".r
  private val Edit = "/([^/]+)/edit".r

  def routes = new AbstractPartialFunction[RequestHeader, Handler] {
  //  def withId(id: String, action: T => EssentialAction) = idBindable.bind("id", id).fold(badRequest, action)

    override def applyOrElse[A <: RequestHeader, B >: Handler](rh: A, default: A => B) = {
      if (rh.path.startsWith(path)) {
          Logger.info("path is :" + path)
          Logger.info("rh path is :" + rh.path)
        (rh.method, rh.path.drop(path.length)) match {
          case ("GET", MaybeSlash()) => index
          case ("GET", "/new") => newScreen
          case ("POST", MaybeSlash()) => create
        //  case ("GET", Id(id)) => index
        //   case ("GET", Edit(id)) => withId(id, edit)
        //   case ("PUT", Id(id)) => withId(id, update)
        //   case ("DELETE", Id(id)) => withId(id, destroy)
          case _ => default(rh)
        }
      } else {
        default(rh)
      }
    }

    def isDefinedAt(rh: RequestHeader) = if (rh.path.startsWith(path)) {
      (rh.method, rh.path.drop(path.length)) match {
        case ("GET", MaybeSlash()) => true
        case ("GET", "/new") => true
        case ("POST", MaybeSlash()) => true
        case ("GET", Id(id)) => true
        case ("GET", Edit(id)) => true
        case ("PUT", Id(id)) => true
        case ("DELETE", Id(id)) => true
        case _ => false
      }
    } else {
      false
    }

  }

  def setPrefix(prefix: String) {
    path = prefix
  }

  def prefix = path

  def documentation = Seq(
    ("GET", path, name + ".index"),
    ("GET", path + "/new", name + ".newScreen"),
    ("POST", path, name + ".create"),
    ("GET", path + "/$id<[^/]+>", name + ".show(id: T)"),
    ("GET", path + "/$id<[^/]+>/edit", name + ".edit(id: T)"),
    ("PUT", path + "/$id<[^/]+>", name + ".update(id: T)"),
    ("DELETE", path + "/$id<[^/]+>", name + ".destroy(id: T)")
  )

  def reverseRoutes: ResourceReverseRouter[T] = new ResourceReverseRouter[T](path)

// def jsRoutes: ResourceJavascriptReverseRouter[T] = new ResourceJavascriptReverseRouter[T](controllerName, path)
}

/**
 * A resource router that uses managed controllers.
 *
 * Typically it will be implemented by an object, supplying a controller class that should be managed by the
 * Global.getControllerInstance method.  For example:
 *
 * {{{
 *   class MyResource(dbService: DbService) extends ResourceController[Long] {
 *     def index = Action {
 *       ...
 *     }
 *     def create(id: Long) = Action {
 *       ...
 *     }
 *     ...
 *   }
 *   object MyResource extends ManagedResourceRouter[Long, MyResource]
 * }}}
 *
 * Then from a routes file can be used like so:
 *
 * {{{
 *   ->   /myresource     controllers.MyResource
 * }}}
 *
 * @param name A custom name for the resource controller.  This is used by the Javascript router.
 * @param idBindable The bindable for converting the id to the expected type.
 */
class ManagedResourceRouter[T, R <: ResourceController[T]](name: Option[String] = None)(implicit idBindable: PathBindable[T], ct: ClassTag[R]) extends ResourceRouter[T] {

  private def invoke(action: R => EssentialAction) = {
    Play.maybeApplication.map { app =>
      //action(app.global.getControllerInstance(ct.runtimeClass.asInstanceOf[Class[R]]))
      action(ct.runtimeClass.asInstanceOf[Class[R]].newInstance())
    } getOrElse {
      Action(Results.InternalServerError("No application"))
    }
  }
  
//   def withPrefix(prefix: String): Router = { this }

   def index = invoke(_.index)

   def newScreen = invoke(_.newScreen)

   def create = invoke(_.create)

   def show(id: T) = invoke(_.show(id))

   def edit(id: T) = invoke(_.edit(id))

   def update(id: T) = invoke(_.update(id))

   def destroy(id: T) = invoke(_.destroy(id))
}

class ResourceReverseRouter[T](path: String)(implicit idBindable: PathBindable[T]) {
  private def route(method: String, p: String = "") = Call("GET", path + p)
  private def routeWithId(method: String, id: T, p: String = "") = Call(method, path + "/" + idBindable.unbind("id", id) + p)

  def index() = route("GET")
  def newScreen() = route("GET", "/new")
  def create() = route("POST")
  def show(id: T) = routeWithId("GET", id)
  def edit(id: T) = routeWithId ("GET", id, "/edit")
  def update(id: T) = routeWithId("PUT", id)
  def destroy(id: T) = routeWithId("DELETE", id)
}

//  class ResourceJavascriptReverseRouter[T](name: String, path: String)(implicit idBindable: PathBindable[T]) {
//   private def route(action: String, method: String, p: String = "") = JavascriptReverseRoute(name + "." + action,
//     s"""
//       function() {
//         return _wA({method: "$method", url: "$path$p"});
//       }
//     """
//   )
//   private def routeWithId(action: String, method: String, p: String = "") = JavascriptReverseRoute(name + "." + action,
//     s"""
//       function(id) {
//         return _wA({method: "$method",
//           url: "$path/" + (${idBindable.javascriptUnbind})("id", encodeURIComponent(id)) + "$p"});
//       }
//     """
//   )

//   def index = route("index", "GET")
//   def newScreen = route("newScreen", "GET", "/new")
//   def create = route("create", "POST")
//   def show = routeWithId("show", "GET")
//   def edit = routeWithId("edit", "GET", "/edit")
//   def update = routeWithId("update", "PUT")
//   def destroy = routeWithId("destroy", "DELETE")

//   def all = Seq(index, newScreen, create, show, edit, update, destroy)
// }
