package com.github.theyelllowdart.unofficialmetaudioguide.server.db

import java.sql.Timestamp

import slick.lifted.{CaseClassShape, ProvenShape, Rep}
import slick.driver.PostgresDriver.api._
import MissingArtObjectRowImplicits.MissingArtObjectShape

object MissingArtObjectRowImplicits {

  implicit object MissingArtObjectShape extends CaseClassShape(LiftedMissingArtObject.tupled, MissingArtObject.tupled)

}

case class MissingArtObject(id: Int, user: String, created: Timestamp, artObjectId: String)

case class LiftedMissingArtObject(id: Rep[Int], user: Rep[String], created: Rep[Timestamp], artObjectId: Rep[String])

class MissingArtObjectRow(tag: Tag) extends Table[MissingArtObject](tag, "missing_art_object") {
  def id: Rep[Int] = column[Int]("id", O.AutoInc, O.PrimaryKey)

  def user: Rep[String] = column[String]("user")

  def created: Rep[Timestamp] = column[Timestamp]("created")

  def artObjectId: Rep[String] = column[String]("art_object_id")


  override def * : ProvenShape[MissingArtObject] = LiftedMissingArtObject(id, user, created, artObjectId)
}
