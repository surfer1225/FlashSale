import java.time.Clock

import com.google.inject.{AbstractModule, Provides}
import dao.{ProductDataService, ProductDataServiceImpl, WalletDataServiceImpl}
import services.{PurchaseService, PurchaseServiceImpl}

/**
  * This class is a Guice module that tells Guice how to bind several
  * different types. This Guice module is created when the Play
  * application starts.

  * Play will automatically use any class called `Module` that is in
  * the root package. You can create modules in other locations by
  * adding `play.modules.enabled` settings to the `application.conf`
  * configuration file.
  */
class Module extends AbstractModule {

  override def configure(): Unit = {
    // For real implementation, data layer service should also be injected via DI
    bind(classOf[PurchaseService])
      .toInstance(new PurchaseServiceImpl(new ProductDataServiceImpl, new WalletDataServiceImpl))
  }
}
