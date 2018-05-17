package controllers

import javax.inject._
import play.api.libs.json._
import play.api.mvc._
import services.PurchaseService

@Singleton
class ProductController @Inject()(cc: ControllerComponents, purchaseService: PurchaseService)
    extends AbstractController(cc) {

  def purchase(productId: Long): Action[AnyContent] = Action { request: Request[AnyContent] =>
    val jsBody: Option[JsValue] = request.body.asJson
    jsBody
      .map { body =>
        //TODO, add error handling for userId
        val userId = (body \ "user_id").as[Long]

        purchaseService.makePurchase(productId, userId)
        Ok
      }
      .getOrElse {
        BadRequest("Expecting application/json request body")
      }
  }

  // FIXME: change to JSON
  def getFlashSale(countryId: String) = Action {
    val flashSales = purchaseService.getFlashSale(countryId)
    flashSales.foreach(println)
    Ok
  }
}
