package services

trait ExchangeRateService {
  def getExchangeRate(baseCurrency: String, toCurrency: String): Double
}

class ExchangeRateServiceImpl extends ExchangeRateService {
  override def getExchangeRate(baseCurrency: String, toCurrency: String): Double = {

    /**
      * In real application, this should be a data service
      * A scheduler application feeds data to DB on a daily basis
      * This function go through data layer to fetch exchange rate
      *
      * Third party exchange rate API can also be used
      */
    1.0
  }
}
