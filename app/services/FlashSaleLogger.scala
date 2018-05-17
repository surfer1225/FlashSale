package services

import play.api.Logger

trait FlashSaleLogger {
  val logger: Logger = play.api.Logger(getClass)
}
