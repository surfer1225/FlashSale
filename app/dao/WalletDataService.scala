package dao

import model.Messages.Wallet

trait WalletDataService {
  def getWalletInfo(userId: Long): Wallet

  def credit(walletId: Long, amt: Double): Boolean

  def debit(walletId: Long, amt: Double): Boolean =
    credit(walletId, (-1.0) * amt)
}

class WalletDataServiceImpl extends WalletDataService {
  override def getWalletInfo(userId: Long): Wallet = {
    Wallet(123, 100.0, "SGD")
  }

  override def credit(walletId: Long, amt: Double): Boolean = {
    // update money value in db
    true
  }
}
