import model.Messages.{ProductSale, _}
import play.api.http.HeaderNames
import play.api.libs.json.Json
import play.api.test._

class IntegrationSpec extends PlaySpecification {
  "Application" should {
    // Test application readiness
    "be reachable" in new WithApplication {
      private val home = route(app, FakeRequest(GET, "/")).get
      status(home) must equalTo(OK)
      contentAsString(home) must contain ("Your new application is ready.")
    }

    "be reachable from browser" in new WithBrowser {
      browser.goTo("http://localhost:" + port + "/")
      browser.pageSource() must contain ("Your new application is ready.")
    }

    // Test User Wallet API
    // 0 is not a valid wallet id
    "Get 'wallet not found' response with unknown wallet" in new WithApplication {
      private val wallet = route(app, FakeRequest(GET, "/wallet/0")).get
      status(wallet) must equalTo(OK)
      contentAsString(wallet) mustEqual "Wallet not found"
    }

    // 1 is chosen since company id always exists
    "Get the right wallet with the right id" in new WithApplication {
      private val wallet = route(app, FakeRequest(GET, "/wallet/1")).get
      status(wallet) must equalTo(OK)
      private val walletJson = contentAsJson(wallet)
      (walletJson \ "id").get.toString mustEqual "1"
      (walletJson \ "balance").isDefined must beTrue
      (walletJson \ "currency").isDefined must beTrue
    }

    // Test purchase product API
    "Successfully make purchase with product id 3" in new WithApplication {
      private val purchaseStatus =
        route(app, FakeRequest.apply(POST, "/products/3/purchase", FakeHeaders(Seq(HeaderNames.HOST -> "localhost")), Json.toJson(User(3)))).get
      status(purchaseStatus) must equalTo(OK)
      contentAsString(purchaseStatus) mustEqual "Purchase Successful"
    }

    "Failure to make purchase with wrong wallet id" in new WithApplication {
      private val purchaseStatus =
        route(app, FakeRequest.apply(POST, "/products/3/purchase", FakeHeaders(Seq(HeaderNames.HOST -> "localhost")), Json.toJson(User(0)))).get
      status(purchaseStatus) must equalTo(OK)
      contentAsString(purchaseStatus) mustEqual "Purchase Failed"
    }
    
    // Test Get Current Flash Sale API
    "Get some flash sales data" in new WithApplication {
      private val sales = route(app, FakeRequest(GET, "/sales/current?country=SG")).get
      status(sales) must equalTo(OK)
      (contentAsJson(sales) \ "sales").as[Seq[ProductSale]].nonEmpty must beTrue
    }
  }
}
