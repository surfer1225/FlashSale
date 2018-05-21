package controllers

import javax.inject._
import model.Messages._
import play.api.libs.json._
import play.api.mvc._
import services.PurchaseService

@Singleton
class WalletController @Inject()(cc: ControllerComponents, purchaseService: PurchaseService)
    extends AbstractController(cc) {

  def getWallet(id: Long): Action[AnyContent] = Action {
    purchaseService.getWalletInfo(id) match {
      case Some(wallet) => Ok(Json.toJson(wallet))
      case _            => Ok("Wallet not found")
    }
  }
}
