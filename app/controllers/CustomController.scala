package controllers

import play.api.mvc._

class MyResource extends ResourceRouter[Long] {
  def index = Action(Ok("index"))

  def newScreen = Action(Ok("new my Resource"))

  def create = Action {
    Redirect(reverseRoutes.index())
  }

  def show(id: Long) = Action(Ok("Show " + id))

  def edit(id: Long) = Action(Ok("Edit " + id))

  def update(id: Long) = Action(Ok("Update " + id))

  def destroy(id: Long) = Action(Ok("Destroy " + id))

  def custom(id: Long) = Action(Ok("Custom " + id))
}

class MyManagedResource extends ResourceController[String] {
  def index = Action(Ok("index"))

  def newScreen = Action(Ok("new"))

  def create = Action {
    // Redirect(MyInjectableResource.reverseRoutes.index())
    Ok("Create")
  }

  def show(id: String) = Action(Ok("Show " + id))

  def edit(id: String) = Action(Ok("Edit " + id))

  def update(id: String) = Action(Ok("Update " + id))

  def destroy(id: String) = Action(Ok("Destroy " + id))

  def custom(id: String) = Action(Ok("Custom " + id))
}

object MyManagedResource extends ManagedResourceRouter[String, MyManagedResource]
