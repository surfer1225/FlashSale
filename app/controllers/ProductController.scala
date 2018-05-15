package controllers

import javax.inject._
import play.api.libs.json._
import play.api.mvc._

@Singleton
class ProductController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  def purchase(productId: Long) = Action { request =>
    Ok(Json.toJson(productId))
  }
}
