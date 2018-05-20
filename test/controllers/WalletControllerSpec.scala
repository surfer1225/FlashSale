package controllers

import model.Messages.Wallet
import org.specs2.mock.Mockito
import play.api.mvc._
import play.api.test._
import services.PurchaseService

import scala.concurrent.Future

class WalletControllerSpec extends PlaySpecification with Results with Mockito {
  "WalletController#getWallet" should {
    "give the right wallet when wallet exists" in {
      val purchaseService = mock[PurchaseService]
      val controller = new WalletController(Helpers.stubControllerComponents(), purchaseService)
      purchaseService.getWalletInfo(100) returns Some(Wallet(100, 10.0, "USD"))
      val result: Future[Result] = controller.getWallet(100).apply(FakeRequest())
      contentAsJson(result).as[Wallet] mustEqual Wallet(100, 10.0, "USD")
    }

    "show wallet not found when wallet does not exist" in {
      val purchaseService = mock[PurchaseService]
      val controller = new WalletController(Helpers.stubControllerComponents(), purchaseService)
      purchaseService.getWalletInfo(110) returns None
      val result: Future[Result] = controller.getWallet(110).apply(FakeRequest())
      contentAsString(result) mustEqual "Wallet not found"
    }
  }
}
