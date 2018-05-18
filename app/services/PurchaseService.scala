package services

import dao.{ProductDataService, WalletDataService}
import javax.inject._
import model.Messages.{ProductSale, Wallet}

trait PurchaseService {
  final val companyWalletId = 1
  final val companyCurrency = "SGD"
  //TODO: consider changing the return type to PurchaseStatus
  def makePurchase(productId: Long, userId: Long): Boolean

  def getFlashSale(countryId: String): List[ProductSale]

  def getWalletInfo(id: Long): Option[Wallet]
}

class PurchaseServiceImpl @Inject()(
    productDataService: ProductDataService,
    walletDataService: WalletDataService,
    exchangeRateService: ExchangeRateService
) extends PurchaseService
    with FlashSaleLogger {

  override def makePurchase(productId: Long, userId: Long): Boolean = {
    val productInfoOpt = productDataService.getProduct(productId)
    val walletInfoOpt  = walletDataService.getWalletInfo(userId)
    (productInfoOpt, walletInfoOpt) match {
      case (Some(productInfo), Some(walletInfo)) => transferFund(walletInfo, productInfo.price, productInfo.currency)
      case _                                     => false
    }
  }

  override def getFlashSale(countryId: String): List[ProductSale] = {
    productDataService.getFlashSale(countryId)
  }

  private[services] def toCompanyCurrency(currency: String, amount: Double): Double =
    amount / exchangeRateService.getExchangeRate(companyCurrency, currency)

  private[services] def transferFund(customerWallet: Wallet, amt: Double, currency: String): Boolean = {
    val amtInCustomerCurrency = amt / exchangeRateService.getExchangeRate(customerWallet.currency, currency)
    if (walletDataService.debit(customerWallet.id, amtInCustomerCurrency)) {
      if (walletDataService.credit(companyWalletId, toCompanyCurrency(currency, amt))) {
        logger.debug(s"$amt credit to company from ${customerWallet.id}")
        true
      } else {
        logger.warn(s"crediting to company account failed, refunding $amt to customer (wallet: ${customerWallet.id})")
        if (!walletDataService.credit(customerWallet.id, amtInCustomerCurrency)) {
          // inform customer service etc.
          logger.error("human intervention needed to do refund")
        }
        false
      }
    } else {
      logger.error("failed to collect money from customer")
      false
    }
  }

  override def getWalletInfo(id: Long): Option[Wallet] = walletDataService.getWalletInfo(id)
}
