package services

import dao.{ProductDataService, WalletDataService}
import javax.inject._

trait PurchaseService {
  //TODO: consider changing the return type to PurchaseStatus
  def makePurchase(productId: Long, userId: Long): Boolean
}

class PurchaseServiceImpl @Inject()(productDataService: ProductDataService, walletDataService: WalletDataService)
    extends PurchaseService {
  override def makePurchase(productId: Long, userId: Long): Boolean = {
    //TODO: get data layer data for that product
    val productInfo = productDataService.getProductInfo(productId)
    println(productInfo)
    val walletInfo = walletDataService.getWalletInfo(userId)
    println(walletInfo)
    true
  }
}
