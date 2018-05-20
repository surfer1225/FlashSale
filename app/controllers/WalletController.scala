package controllers

import javax.inject._
import model.Messages._
import play.api.libs.json._
import play.api.mvc._
import services.PurchaseService

@Singleton
class WalletController @Inject()(cc: ControllerComponents, purchaseService: PurchaseService)
    extends AbstractController(cc) {

  /**
    * Create an Action to render an HTML page with a welcome message.
    * The configuration in the `routes` file means that this method
    * will be called when the application receives a `GET` request with
    * a path of `/`.
    *
    * This method is to be removed in the future when front-end is implemented
    */
  def index: Action[AnyContent] = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  def getWallet(id: Long): Action[AnyContent] = Action {
    purchaseService.getWalletInfo(id) match {
      case Some(wallet) => Ok(Json.toJson(wallet))
      case _            => Ok("Wallet not found")
    }
  }
}
