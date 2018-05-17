package dao

import model.Messages.ProductSale

trait ProductDataService {
  def getProductInfo(productId: Long): ProductSale

  def getFlashSale(countryId: String): List[ProductSale]

  def updateProductLeft(productId: Long, itemsLeft: Int): Boolean
}

class ProductDataServiceImpl extends ProductDataService {
  override def getProductInfo(productId: Long): ProductSale = {
    ProductSale(123, 10.0, "SGD", 1000, 100, 100000)
  }

  override def getFlashSale(countryId: String): List[ProductSale] = {
    List(
      ProductSale(1, 10.0, "SGD", 1000, 100, 100000),
      ProductSale(2, 20.0, "SGD", 2000, 200, 200000),
      ProductSale(3, 30.0, "SGD", 3000, 300, 300000)
    )
  }

  override def updateProductLeft(productId: Long, itemsLeft: Int): Boolean = ???
}
