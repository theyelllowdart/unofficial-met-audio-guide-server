package com.github.theyelllowdart.unofficialmetaudioguide.server.db

import java.sql.Timestamp

import slick.lifted.{CaseClassShape, ProvenShape, Rep}
import slick.driver.PostgresDriver.api._
import NewStopRowImplicits.NewStopShape

object NewStopRowImplicits {

  implicit object NewStopShape extends CaseClassShape(LiftedNewStop.tupled, NewStop.tupled)

}

case class NewStop(
  id: Int,
  user: String,
  created: Timestamp,
  stopId: Int,
  galleryId: Int,
  x: Float,
  y: Float,
  rotation: Float
)

case class LiftedNewStop(
  id: Rep[Int],
  user: Rep[String],
  created: Rep[Timestamp],
  stopId: Rep[Int],
  galleryId: Rep[Int],
  x: Rep[Float],
  y: Rep[Float],
  rotation: Rep[Float]
)

class NewStopRow(tag: Tag) extends Table[NewStop](tag, "new_stop") {
  def id: Rep[Int] = column[Int]("id", O.AutoInc, O.PrimaryKey)

  def user: Rep[String] = column[String]("user")

  def created: Rep[Timestamp] = column[Timestamp]("created")

  def stopId: Rep[Int] = column[Int]("stop_id")

  def galleryId: Rep[Int] = column[Int]("gallery_id")

  def x: Rep[Float] = column[Float]("x")

  def y: Rep[Float] = column[Float]("y")

  def rotation: Rep[Float] = column[Float]("rotation")

  override def * : ProvenShape[NewStop] = {
    LiftedNewStop(id, user, created, stopId, galleryId, x, y, rotation)
  }
}
