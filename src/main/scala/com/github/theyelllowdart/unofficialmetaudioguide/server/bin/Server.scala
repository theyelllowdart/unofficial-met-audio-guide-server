package com.github.theyelllowdart.unofficialmetaudioguide.server.bin

import com.github.theyelllowdart.unofficialmetaudioguide.server.controller.{ArtObjectController, StopController}
import com.google.inject.{Binder, Module}
import com.twitter.finagle.http.{Request, Response}
import com.twitter.finatra.http.HttpServer
import com.twitter.finatra.http.filters.{CommonFilters, LoggingMDCFilter, TraceIdMDCFilter}
import com.twitter.finatra.http.routing.HttpRouter
import com.twitter.finatra.json.modules.FinatraJacksonModule
import com.twitter.finatra.json.utils.CamelCasePropertyNamingStrategy
import slick.driver.PostgresDriver.api._
import slick.driver.PostgresDriver.backend.DatabaseDef

import scala.util.Properties

object Main extends Server

class Server extends HttpServer {

  override val modules: Seq[Module] = {
    val db: DatabaseDef = Database.forURL(
      url = Properties.envOrNone("db.url").get,
      user = Properties.envOrNone("db.user").get,
      password = Properties.envOrNone("db.password").get
    )

    val module = new Module {
      override def configure(binder: Binder): Unit = {
        binder.bind(classOf[DatabaseDef]).toInstance(db)
      }
    }
    Vector(module)
  }


  override protected def jacksonModule: Module = new FinatraJacksonModule {
    override val propertyNamingStrategy = CamelCasePropertyNamingStrategy
  }

  override def configureHttp(router: HttpRouter) {
    router
      .filter[LoggingMDCFilter[Request, Response]]
      .filter[TraceIdMDCFilter[Request, Response]]
      .filter[CommonFilters]
      .add[ArtObjectController]
      .add[StopController]
  }
}
