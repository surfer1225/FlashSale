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
    if (walletId == 1)
      Some(Wallet(1, 100.0, "SGD"))
    else if (walletId == 3)
      Some(Wallet(3, 130.0, "SGD"))
    else None
  }

  override def credit(walletId: Long, amt: Double): Boolean = {
    // update money value in db
    true
  }
}
