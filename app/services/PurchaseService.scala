package services

import dao.{ProductDataService, WalletDataService}
import javax.inject._
import model.Messages.{ProductSale, Wallet}

sealed trait PurchaseStatus
case object PurchaseSuccess    extends PurchaseStatus
case object PaymentFailure     extends PurchaseStatus
case object CreditFailure      extends PurchaseStatus
case object ManualIntervention extends PurchaseStatus

trait PurchaseService {
  final val companyWalletId = 1
  final val companyCurrency = "SGD"

  def makePurchase(productId: Long, userId: Long): PurchaseStatus

  def getFlashSale(countryId: String): List[ProductSale]

  def getWalletInfo(id: Long): Option[Wallet]
}

class PurchaseServiceImpl @Inject()(
    productDataService: ProductDataService,
    walletDataService: WalletDataService,
    exchangeRateService: ExchangeRateService
) extends PurchaseService
    with FlashSaleLogger {

  override def makePurchase(productId: Long, userId: Long): PurchaseStatus = {
    val productSaleOpt = productDataService.getProduct(productId)
    val walletInfoOpt  = walletDataService.getWalletInfo(userId)
    (productSaleOpt, walletInfoOpt) match {
      case (Some(productSale), Some(walletInfo)) => transferFund(walletInfo, productSale)
      case _                                     => PaymentFailure
    }
  }

  override def getFlashSale(countryId: String): List[ProductSale] = {
    productDataService.getFlashSale(countryId)
  }

  private[services] def toCompanyCurrency(currency: String, amount: Double): Double =
    amount / exchangeRateService.getExchangeRate(companyCurrency, currency)

  private[services] def transferFund(customerWallet: Wallet, productSale: ProductSale): PurchaseStatus = {
    val amtInCustomerCurrency = productSale.price / exchangeRateService.getExchangeRate(
      customerWallet.currency,
      productSale.currency
    )
    if (walletDataService.debit(customerWallet.id, amtInCustomerCurrency)) {
      val companyEarning = toCompanyCurrency(productSale.currency, productSale.price)
      if (walletDataService.credit(companyWalletId, companyEarning)) {
        logger.debug(s"${productSale.price} credit to company from ${customerWallet.id}")
        productDataService.updateProductLeft(productSale.product_id)
        PurchaseSuccess
      } else {
        logger.warn(
          s"crediting to company account failed, refunding ${productSale.price} to customer (wallet: ${customerWallet.id})"
        )
        if (!walletDataService.credit(customerWallet.id, amtInCustomerCurrency)) {
          // inform customer service etc.
          logger.error("human intervention needed to do refund")
          ManualIntervention
        } else {
          CreditFailure
        }
      }
    } else {
      logger.error("failed to collect money from customer")
      PaymentFailure
    }
  }

  override def getWalletInfo(id: Long): Option[Wallet] = walletDataService.getWalletInfo(id)
}
