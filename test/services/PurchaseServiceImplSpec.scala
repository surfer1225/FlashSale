package services

import dao.{ProductDataService, WalletDataService}
import model.Messages.{ProductSale, Wallet}
import org.specs2.mock.Mockito
import org.specs2.mutable.Specification

class PurchaseServiceImplSpec extends Specification with Mockito {

  private val productDataService: ProductDataService = mock[ProductDataService]
  private val walletDataService: WalletDataService = mock[WalletDataService]
  private val exchangeRateService: ExchangeRateService = mock[ExchangeRateService]

  exchangeRateService.getExchangeRate(anyString, anyString) returns 1.0

  //scalastyle:off
  "PurchaseServiceImpl#makePurchase" should {

    "PaymentFailure when no wallet found" in {
      walletDataService.getWalletInfo(117) returns None
      productDataService.getProduct(118) returns Some(ProductSale(118, 10.0, "SGD", 10, 10, 10))
      val purchaseService: PurchaseService = new PurchaseServiceImpl(productDataService, walletDataService, exchangeRateService)
      purchaseService.makePurchase(118, 117) mustEqual PaymentFailure
    }

    "PaymentFailure when no product found" in {
      walletDataService.getWalletInfo(217) returns Some(Wallet(217, 20.0, "SGD"))
      productDataService.getProduct(218) returns None
      val purchaseService: PurchaseService = new PurchaseServiceImpl(productDataService, walletDataService, exchangeRateService)
      purchaseService.makePurchase(218, 217) mustEqual PaymentFailure
    }

    "PaymentFailure if debit in customer wallet fails" in {
      walletDataService.getWalletInfo(9) returns Some(Wallet(9, 20.0, "SGD"))
      productDataService.getProduct(8) returns Some(ProductSale(8, 10.0, "SGD", 10, 10, 10))
      walletDataService.debit(9, 10.0) returns false
      val purchaseService: PurchaseService = new PurchaseServiceImpl(productDataService, walletDataService, exchangeRateService)
      purchaseService.makePurchase(8, 9) mustEqual PaymentFailure
    }

    "CreditFailure if credit to company account fails" in {
      walletDataService.getWalletInfo(19) returns Some(Wallet(19, 20.0, "SGD"))
      productDataService.getProduct(18) returns Some(ProductSale(18, 10.0, "SGD", 10, 10, 10))
      walletDataService.debit(19, 10.0) returns true
      walletDataService.credit(1, 10.0) returns false
      walletDataService.credit(19, 10.0) returns true
      val purchaseService: PurchaseService = new PurchaseServiceImpl(productDataService, walletDataService, exchangeRateService)
      purchaseService.makePurchase(18, 19) mustEqual CreditFailure
    }

    "ManualIntervention if credit to company account fails" in {
      walletDataService.getWalletInfo(29) returns Some(Wallet(29, 20.0, "SGD"))
      productDataService.getProduct(28) returns Some(ProductSale(28, 10.0, "SGD", 10, 10, 10))
      walletDataService.debit(29, 10.0) returns true
      walletDataService.credit(1, 10.0) returns false
      walletDataService.credit(29, 10.0) returns false
      val purchaseService: PurchaseService = new PurchaseServiceImpl(productDataService, walletDataService, exchangeRateService)
      purchaseService.makePurchase(28, 29) mustEqual ManualIntervention
    }

    "PurchaseSuccess if transfer successful" in {
      walletDataService.getWalletInfo(39) returns Some(Wallet(39, 20.0, "SGD"))
      productDataService.getProduct(38) returns Some(ProductSale(38, 10.0, "SGD", 10, 10, 10))
      walletDataService.debit(39, 10.0) returns true
      walletDataService.credit(1, 10.0) returns true
      val purchaseService: PurchaseService = new PurchaseServiceImpl(productDataService, walletDataService, exchangeRateService)
      purchaseService.makePurchase(38, 39) mustEqual PurchaseSuccess
      there was exactly(1)(productDataService).updateProductLeft(38)
    }
  }
  //scalastyle:on
}
