package controllers

import db.DBUtils
import javax.inject.Inject
import play.api.mvc.{AbstractController, Action, AnyContent, ControllerComponents}
import services.PurchaseService

class HomeController @Inject()(cc: ControllerComponents, purchaseService: PurchaseService)
    extends AbstractController(cc) {

  /**
    * Create an Action to render an HTML page with a welcome message.
    * The configuration in the `routes` file means that this method
    * will be called when the application receives a `GET` request with
    * a path of `/`.
    *
    * Seed data is also injected when application starts
    *
    * This method is to be removed in the future when front-end is implemented
    */
  def index: Action[AnyContent] = Action {
    DBUtils.supplySeedData
    Ok(views.html.index("Your new application is ready."))
  }

  def seed: Action[AnyContent] = Action {
    DBUtils.supplySeedData
    Ok(views.html.index("Data seeded"))
  }
}
