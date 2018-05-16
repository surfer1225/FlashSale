package dao

import model.Messages.ProductSale

trait ProductDataService {
  def getProductInfo(productId: Long): ProductSale
}

class ProductDataServiceImpl extends ProductDataService {
  override def getProductInfo(productId: Long): ProductSale = {
    ProductSale(123, 10.0, "SGD", 1000, 100, 100000)
  }
}
