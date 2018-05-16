package dao

import model.Messages.Wallet

trait WalletDataService {
  def getWalletInfo(userId: Long): Wallet
}

class WalletDataServiceImpl extends WalletDataService {
  override def getWalletInfo(userId: Long): Wallet = {
    Wallet(123, 100.0, "SGD")
  }
}
