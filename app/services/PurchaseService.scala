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
    val productSaleOpt = productDataService.getProduct(productId)
    val walletInfoOpt  = walletDataService.getWalletInfo(userId)
    (productSaleOpt, walletInfoOpt) match {
      case (Some(productSale), Some(walletInfo)) => transferFund(walletInfo, productSale)
      case _                                     => false
    }
  }

  override def getFlashSale(countryId: String): List[ProductSale] = {
    productDataService.getFlashSale(countryId)
  }

  private[services] def toCompanyCurrency(currency: String, amount: Double): Double =
    amount / exchangeRateService.getExchangeRate(companyCurrency, currency)

  private[services] def transferFund(customerWallet: Wallet, productSale: ProductSale): Boolean = {
    val amtInCustomerCurrency = productSale.price / exchangeRateService.getExchangeRate(
      customerWallet.currency,
      productSale.currency
    )
    if (walletDataService.debit(customerWallet.id, amtInCustomerCurrency)) {
      if (walletDataService.credit(companyWalletId, toCompanyCurrency(productSale.currency, productSale.price))) {
        logger.debug(s"${productSale.price} credit to company from ${customerWallet.id}")
        productDataService.updateProductLeft(productSale.product_id)
        true
      } else {
        logger.warn(
          s"crediting to company account failed, refunding ${productSale.price} to customer (wallet: ${customerWallet.id})"
        )
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
