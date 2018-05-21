package dao

import java.sql.ResultSet

import db.DBUtils.connection
import model.Messages.ProductSale

import scala.collection.mutable
import scala.concurrent.Future

trait ProductDataService {
  def getProduct(productId: Long): Option[ProductSale]

  def getFlashSale(countryId: String): List[ProductSale]

  def updateProductLeft(productId: Long): Future[Unit]
}

class ProductDataServiceImpl extends ProductDataService {
  override def getProduct(productId: Long): Option[ProductSale] = {
    val rs = connection.prepareStatement(s"SELECT * FROM PRODUCTSALE WHERE ID = $productId LIMIT 1").executeQuery()
    if (rs.next()) {
      Some(resultSetToProductSale(rs))
    } else {
      None
    }
  }

  override def getFlashSale(countryId: String): List[ProductSale] = {
    var productList = new mutable.ListBuffer[ProductSale]()
    val rs          = connection.prepareStatement(s"SELECT * FROM PRODUCTSALE WHERE CTY = '$countryId'").executeQuery()
    while (rs.next()) {
      productList += resultSetToProductSale(rs)
    }
    productList.toList
  }

  private def computeTimeLeft(time: Long): Long = {
    time - (System.currentTimeMillis / 1000)
  }

  private def resultSetToProductSale(rs: ResultSet): ProductSale = {
    ProductSale(
      rs.getInt("id"),
      rs.getDouble("price"),
      rs.getString("currency"),
      rs.getInt("total_items"),
      rs.getInt("items_left"),
      computeTimeLeft(rs.getLong("end_time"))
    )
  }

  override def updateProductLeft(productId: Long): Future[Unit] = {
    val rs = connection.prepareStatement(s"SELECT * FROM PRODUCTSALE WHERE ID = $productId LIMIT 1").executeQuery()
    rs.next()
    val left = rs.getInt("items_left") - 1
    val res =
      connection.prepareStatement(s"UPDATE PRODUCTSALE SET items_left = $left WHERE ID = $productId").executeUpdate()
    if (res == 1) {
      Future.successful()
    } else {
      Future.failed(new Exception("DB Error, failed to update items left"))
    }
  }
}
