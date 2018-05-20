package services

import dao.{ProductDataService, WalletDataService}
import model.Messages.{ProductSale, Wallet}
import org.specs2.mock.Mockito
import org.specs2.mutable.Specification

class PurchaseServiceImplSpec extends Specification with Mockito {

  private val productDataService: ProductDataService = mock[ProductDataService]
  private val walletDataService: WalletDataService = mock[WalletDataService]
  private val exchangeRateService: ExchangeRateService = mock[ExchangeRateService]

  private val purchaseService: PurchaseService = new PurchaseServiceImpl(productDataService, walletDataService, exchangeRateService)

  exchangeRateService.getExchangeRate(anyString, anyString) returns 1.0

  //scalastyle:off
  "PurchaseServiceImpl#makePurchase" should {

    "PaymentFailure when no wallet found" in {
      walletDataService.getWalletInfo(17) returns None
      productDataService.getProduct(18) returns Some(ProductSale(18, 10.0, "SGD", 10, 10, 10))
      purchaseService.makePurchase(18, 17) mustEqual PaymentFailure
    }

    "PaymentFailure when no product found" in {
      walletDataService.getWalletInfo(27) returns Some(Wallet(27, 20.0, "SGD"))
      productDataService.getProduct(28) returns None
      purchaseService.makePurchase(28, 27) mustEqual PaymentFailure
    }

    "PaymentFailure if debit in customer wallet fails" in {
      walletDataService.getWalletInfo(9) returns Some(Wallet(9, 20.0, "SGD"))
      productDataService.getProduct(8) returns Some(ProductSale(8, 10.0, "SGD", 10, 10, 10))
      walletDataService.debit(9, 10.0) returns false

      purchaseService.makePurchase(8, 9) mustEqual PaymentFailure
    }

    "CreditFailure if credit to company account fails" in {
      walletDataService.getWalletInfo(9) returns Some(Wallet(9, 20.0, "SGD"))
      productDataService.getProduct(8) returns Some(ProductSale(8, 10.0, "SGD", 10, 10, 10))
      walletDataService.debit(9, 10.0) returns true
      walletDataService.credit(1, 10.0) returns false
      walletDataService.credit(9, 10.0) returns true

      purchaseService.makePurchase(8, 9) mustEqual CreditFailure
    }

    "ManualIntervention if credit to company account fails" in {
      walletDataService.getWalletInfo(9) returns Some(Wallet(9, 20.0, "SGD"))
      productDataService.getProduct(8) returns Some(ProductSale(8, 10.0, "SGD", 10, 10, 10))
      walletDataService.debit(9, 10.0) returns true
      walletDataService.credit(1, 10.0) returns true
      walletDataService.credit(9, 10.0) returns false

      purchaseService.makePurchase(8, 9) mustEqual ManualIntervention
    }

    "PurchaseSuccess if transfer successful" in {
      walletDataService.getWalletInfo(9) returns Some(Wallet(9, 20.0, "SGD"))
      productDataService.getProduct(8) returns Some(ProductSale(8, 10.0, "SGD", 10, 10, 10))
      walletDataService.debit(9, 10.0) returns true
      walletDataService.credit(1, 10.0) returns true

      purchaseService.makePurchase(8, 9) mustEqual PurchaseSuccess
      there was exactly(1)(productDataService).updateProductLeft(8)
    }
  }
  //scalastyle:on
}
