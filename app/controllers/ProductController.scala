package controllers

import javax.inject._
import play.api.libs.json._
import play.api.mvc._
import services.{PurchaseService, PurchaseSuccess}

@Singleton
class ProductController @Inject()(cc: ControllerComponents, purchaseService: PurchaseService)
    extends AbstractController(cc) {

  def purchase(productId: Long): Action[AnyContent] = Action { request: Request[AnyContent] =>
    val jsBody: Option[JsValue] = request.body.asJson
    jsBody
      .map { body =>
        (body \ "user_id").validate[Long] match {
          case s: JsSuccess[Long] =>
            if (purchaseService.makePurchase(productId, s.get) == PurchaseSuccess) {
              Ok("Purchase Successful")
            } else {
              Ok("Purchase Failed")
            }
          case e: JsError =>
            BadRequest("Error parsing user_id as a \"Long\" number")
        }
      }
      .getOrElse {
        BadRequest("Expecting application/json request body")
      }
  }

  def getFlashSale(countryId: String): Action[AnyContent] = Action {
    val flashSales = purchaseService.getFlashSale(countryId)
    Ok(Json.prettyPrint(Json.obj("sales" -> Json.toJson(flashSales))))
  }
}
