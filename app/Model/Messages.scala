package Model

import play.api.libs.functional.syntax._
import play.api.libs.json.{JsPath, Writes}

object Messages {

  // id in user and wallet inner join to identify the wallet for the user
  case class Wallet(id: Long, balance: Double, currency: String)
  case class User(id: Long)

  //FIXME: check the implications for total items and remaining items, the way to compute time
  case class ProductSale(product_id: Double, price: Double, currency: String, total_items: Int, items_left: Int, time_left: Long)

  implicit val walletWrite : Writes[Wallet] = (
    (JsPath \ "id").write[Long] and
      (JsPath \ "balance").write[Double] and
      (JsPath \ "currency").write[String]
    )(unlift(Wallet.unapply))
}
