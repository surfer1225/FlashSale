package dao

import java.sql.ResultSet

import db.DBUtils.connection
import model.Messages.Wallet
import services.FlashSaleLogger

trait WalletDataService {
  def getWalletInfo(walletId: Long): Option[Wallet]

  def credit(walletId: Long, amt: Double): Boolean

  def debit(walletId: Long, amt: Double): Boolean =
    credit(walletId, (-1.0) * amt)
}

class WalletDataServiceImpl extends WalletDataService with FlashSaleLogger {

  override def getWalletInfo(walletId: Long): Option[Wallet] = {
    val rs: ResultSet = connection.prepareStatement(s"SELECT * FROM WALLET WHERE ID = $walletId LIMIT 1").executeQuery()
    if (!rs.next()) {
      None
    } else {
      Some(rsToWallet(rs))
    }
  }

  override def credit(walletId: Long, amt: Double): Boolean = {
    val rs: ResultSet = connection.prepareStatement(s"SELECT * FROM WALLET WHERE ID = $walletId LIMIT 1").executeQuery()
    rs.next()
    val newAmt = rs.getDouble("balance") + amt
    connection.prepareStatement(s"UPDATE WALLET SET BALANCE = $newAmt WHERE ID = $walletId").executeUpdate() == 1
  }

  private def rsToWallet(rs: ResultSet): Wallet = {
    Wallet(
      rs.getInt("id"),
      rs.getDouble("balance"),
      rs.getString("currency")
    )
  }
}
