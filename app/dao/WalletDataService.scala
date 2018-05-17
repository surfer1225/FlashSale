package dao

import model.Messages.Wallet

trait WalletDataService {
  def getWalletInfo(walletId: Long): Option[Wallet]

  def credit(walletId: Long, amt: Double): Boolean

  def debit(walletId: Long, amt: Double): Boolean =
    credit(walletId, (-1.0) * amt)
}

class WalletDataServiceImpl extends WalletDataService {
  override def getWalletInfo(walletId: Long): Option[Wallet] = {
    //Some(Wallet(123, 100.0, "SGD"))
    None
  }

  override def credit(walletId: Long, amt: Double): Boolean = {
    // update money value in db
    true
  }
}
