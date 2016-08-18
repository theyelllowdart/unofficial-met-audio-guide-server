package com.github.theyelllowdart.unofficialmetaudioguide.server.controller

import java.sql.Timestamp
import javax.inject.Inject

import com.github.theyelllowdart.unofficialmetaudioguide.server.db.{MissingArtObject, MissingArtObjectRow, NewStop, NewStopRow}
import com.twitter.finatra.http.Controller
import slick.driver.PostgresDriver.api._
import slick.driver.PostgresDriver.backend.DatabaseDef
import com.twitter.bijection.Conversion.asMethod
import com.twitter.util.{Future => TwitterFuture}
import com.twitter.bijection.twitter_util.UtilBijections.twitter2ScalaFuture
import com.twitter.finatra.request.{QueryParam, RouteParam}
import com.twitter.finatra.validation.Max
import org.joda.time.DateTime

import scala.concurrent.ExecutionContext.Implicits.global

case class AddStopRequest(
  user: String,
  created: DateTime,
  stopId: Int,
  galleryId: Int,
  x: Float,
  y: Float,
  rotation: Float
)

class StopController @Inject()(db: DatabaseDef) extends Controller {

  post("/stops")((request: AddStopRequest) => {
    val newRow = {
      NewStop(
        0,
        request.user,
        new Timestamp(request.created.getMillis),
        request.stopId,
        request.galleryId,
        request.x,
        request.y,
        request.rotation
      )
    }
    val insert = TableQuery[NewStopRow] += newRow
    db.run(insert).as[TwitterFuture[Int]].unit
  })

  get("/stops")((request: ListRequest) => {
    val query = {
      TableQuery[NewStopRow]
        .sortBy(_.id.desc)
        .drop(request.offset)
        .take(request.take)
        .result
    }
    db.run(query).as[TwitterFuture[Seq[NewStop]]]
  })
}
