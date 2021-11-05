package de.lightningpayments.app

import com.google.inject.AbstractModule

class Module extends AbstractModule {

  override def configure(): Unit = {
    bind(classOf[SimpleApp]).asEagerSingleton()
  }
}
