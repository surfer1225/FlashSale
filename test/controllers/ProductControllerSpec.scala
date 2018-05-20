package controllers

import model.Messages.ProductSale
import org.specs2.mock.Mockito
import play.api.mvc._
import play.api.test._
import services.PurchaseService

import scala.concurrent.Future


class ProductControllerSpec extends PlaySpecification with Results with Mockito {

  "ProductController#getFlashSale" should {
    "give the right products" in {
      val purchaseService = mock[PurchaseService]
      val controller = new ProductController(Helpers.stubControllerComponents(), purchaseService)
      purchaseService.getFlashSale("SG") returns List(ProductSale(1, 10.0, "SGD", 1000, 100, 100000))
      val result: Future[Result] = controller.getFlashSale("SG").apply(FakeRequest())
      (contentAsJson(result) \ "sales").as[Seq[ProductSale]] mustEqual Seq(ProductSale(1, 10.0, "SGD", 1000, 100, 100000))
    }
  }
}