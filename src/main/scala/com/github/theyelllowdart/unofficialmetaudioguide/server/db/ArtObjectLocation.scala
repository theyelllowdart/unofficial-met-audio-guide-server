package com.github.theyelllowdart.unofficialmetaudioguide.server.db

import java.sql.Timestamp

import slick.lifted.{CaseClassShape, ProvenShape, Rep}
import slick.driver.PostgresDriver.api._
import ArtObjectLocationRowImplicits.ArtObjectLocationShape

object ArtObjectLocationRowImplicits {

  implicit object ArtObjectLocationShape extends CaseClassShape(LiftedArtObjectLocation.tupled, ArtObjectLocation.tupled)

}

case class ArtObjectLocation(
  id: Int,
  user: String,
  created: Timestamp,
  artObjectId: String,
  x: Float,
  y: Float,
  rotation: Float
)

case class LiftedArtObjectLocation(
  id: Rep[Int],
  user: Rep[String],
  created: Rep[Timestamp],
  artObjectId: Rep[String],
  x: Rep[Float],
  y: Rep[Float],
  rotation: Rep[Float]
)

class ArtObjectLocationRow(tag: Tag) extends Table[ArtObjectLocation](tag, "art_object_location") {
  def id: Rep[Int] = column[Int]("id", O.AutoInc, O.PrimaryKey)

  def user: Rep[String] = column[String]("user")

  def created: Rep[Timestamp] = column[Timestamp]("created")

  def artObjectId: Rep[String] = column[String]("art_object_id")

  def x: Rep[Float] = column[Float]("x")

  def y: Rep[Float] = column[Float]("y")

  def rotation: Rep[Float] = column[Float]("rotation")

  override def * : ProvenShape[ArtObjectLocation] = {
    LiftedArtObjectLocation(id, user, created, artObjectId, x, y, rotation)
  }
}
