package controllers

import javax.inject._
import model.Messages._
import play.api.libs.json._
import play.api.mvc._

@Singleton
class WalletController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  /**
    * Create an Action to render an HTML page with a welcome message.
    * The configuration in the `routes` file means that this method
    * will be called when the application receives a `GET` request with
    * a path of `/`.
    */
  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  def getWallet(id: Long) = Action {
    Ok(Json.toJson(Wallet(id, 1.0, "SGD")))
  }
}
