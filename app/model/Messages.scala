package model

import play.api.libs.functional.syntax._
import play.api.libs.json._

object Messages {

  // id in user and wallet inner join to identify the wallet for the user
  case class Wallet(id: Long, balance: Double, currency: String)
  case class User(user_id: Long)

  // this case class is not used yet, it joins with ProductSale on product_id
  case class ProductInfo(product_id: Long, name: String, description: String, country: String)

  case class ProductSale(
      product_id: Long,
      price: Double,
      currency: String,
      total_items: Int,
      items_left: Int,
      time_left: Long
  )

  // all the implicit reads
  implicit val productSaleRead: Reads[ProductSale] = (
    (JsPath \ "product_id").read[Long] and
      (JsPath \ "price").read[Double] and
      (JsPath \ "currency").read[String] and
      (JsPath \ "total_items").read[Int] and
      (JsPath \ "items_left").read[Int] and
      (JsPath \ "time_left").read[Long]
  )(ProductSale.apply _)

  implicit val walletRead: Reads[Wallet] = (
    (JsPath \ "id").read[Long] and
      (JsPath \ "balance").read[Double] and
      (JsPath \ "currency").read[String]
  )(Wallet.apply _)

  // all the implicit writes
  implicit val userWrite: Writes[User] = (o: User) => Json.obj("user_id" -> o.user_id)

  implicit val walletWrite: Writes[Wallet] = (
    (JsPath \ "id").write[Long] and
      (JsPath \ "balance").write[Double] and
      (JsPath \ "currency").write[String]
  )(unlift(Wallet.unapply))

  implicit val productSaleWrite: Writes[ProductSale] = (
    (JsPath \ "product_id").write[Long] and
      (JsPath \ "price").write[Double] and
      (JsPath \ "currency").write[String] and
      (JsPath \ "total_items").write[Int] and
      (JsPath \ "items_left").write[Int] and
      (JsPath \ "time_left").write[Long]
  )(unlift(ProductSale.unapply))
}
