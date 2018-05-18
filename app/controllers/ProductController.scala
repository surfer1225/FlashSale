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

        if (purchaseService.makePurchase(productId, userId)) {
          Ok("Purchase Successful")
        } else {
          Ok("Purchase Failed")
        }
      }
      .getOrElse {
        BadRequest("Expecting application/json request body")
      }
  }

  def getFlashSale(countryId: String): Action[AnyContent] = Action {
    val flashSales = purchaseService.getFlashSale(countryId)
    Ok(Json.toJson(flashSales))
  }
}
