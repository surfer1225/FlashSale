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
        val userId = (body \ "user_id").as[Long]

        //TODO, add service/data layer to user userId and productId for processing
        //TODO, add error handling for userId
        purchaseService.makePurchase(productId, userId)
        Ok
      }
      .getOrElse {
        BadRequest("Expecting application/json request body")
      }
  }

  def getFlashSale(countryId: String) = Action {
    val flashSales = purchaseService.getFlashSale(countryId)
    flashSales.foreach(println)
    Ok
  }
}
