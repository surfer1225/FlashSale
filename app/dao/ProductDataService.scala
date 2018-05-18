package dao

import model.Messages.ProductSale

import scala.concurrent.Future

trait ProductDataService {
  def getProduct(productId: Long): Option[ProductSale]

  def getFlashSale(countryId: String): List[ProductSale]

  def updateProductLeft(productId: Long): Future[Unit]
}

class ProductDataServiceImpl extends ProductDataService {
  override def getProduct(productId: Long): Option[ProductSale] = {
    Some(ProductSale(123, 10.0, "SGD", 1000, 100, 100000))
  }

  override def getFlashSale(countryId: String): List[ProductSale] = {
    List(
      ProductSale(1, 10.0, "SGD", 1000, 100, 100000),
      ProductSale(2, 20.0, "SGD", 2000, 200, 200000),
      ProductSale(3, 30.0, "SGD", 3000, 300, 300000)
    )
  }

  override def updateProductLeft(productId: Long): Future[Unit] = ???
}
