package model

import play.api.libs.functional.syntax._
import play.api.libs.json.{JsPath, Writes}

object Messages {

  // id in user and wallet inner join to identify the wallet for the user
  case class Wallet(id: Long, balance: Double, currency: String)
  case class User(id: Long)

  // this case class is not used yet, it joins with ProductSale on product_id
  case class ProductInfo(product_id: Long, name: String, description: String, country: String)

  //FIXME: check the implications for total items and remaining items, the way to compute time
  case class ProductSale(
      product_id: Long,
      price: Double,
      currency: String,
      total_items: Int,
      items_left: Int,
      time_left: Long
  )

  // all the implicit reads

  // all the implicit writes
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
