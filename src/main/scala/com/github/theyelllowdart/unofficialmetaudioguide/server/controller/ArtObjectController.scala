package com.github.theyelllowdart.unofficialmetaudioguide.server.controller

import java.sql.Timestamp
import javax.inject.Inject

import com.github.theyelllowdart.unofficialmetaudioguide.server.db.{ArtObjectLocation, ArtObjectLocationRow, MissingArtObject, MissingArtObjectRow}
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

case class ListRequest(@QueryParam offset: Int = 0, @Max(100) @QueryParam take: Int = 100)

case class ArtObjectLocationReportRequest(
  user: String,
  created: DateTime,
  @RouteParam artObjectId: String,
  x: Float,
  y: Float,
  rotation: Float
)

case class ArtObjectMissingReportRequest(@RouteParam artObjectId: String, user: String, created: DateTime)

class ArtObjectController @Inject()(db: DatabaseDef) extends Controller {

  post("/art-object/:artObjectId/location-report")((request: ArtObjectLocationReportRequest) => {
    val newRow = {
      ArtObjectLocation(
        0,
        request.user,
        new Timestamp(request.created.getMillis),
        request.artObjectId,
        request.x,
        request.y,
        request.rotation
      )
    }
    val insert = TableQuery[ArtObjectLocationRow] += newRow
    db.run(insert).as[TwitterFuture[Int]].unit
  })

  get("/art-object/location-reports")((request: ListRequest) => {
    val query = {
      TableQuery[ArtObjectLocationRow]
        .sortBy(_.id.desc)
        .drop(request.offset)
        .take(request.take)
        .result
    }
    db.run(query).as[TwitterFuture[Seq[ArtObjectLocation]]]
  })

  post("/art-object/:artObjectId/missing-report")((request: ArtObjectMissingReportRequest) => {
    val newRow = MissingArtObject(0, request.user, new Timestamp(request.created.getMillis), request.artObjectId)
    val insert = TableQuery[MissingArtObjectRow] += newRow
    db.run(insert).as[TwitterFuture[Int]].unit
  })

  get("/art-object/missing-reports")((request: ListRequest) => {
    val query = {
      TableQuery[MissingArtObjectRow]
        .sortBy(_.id.desc)
        .drop(request.offset)
        .take(request.take)
        .result
    }
    db.run(query).as[TwitterFuture[Seq[MissingArtObject]]]
  })
}
