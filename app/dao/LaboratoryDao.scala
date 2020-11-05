package dao

import akka.actor.ActorSystem
import com.google.inject.ImplementedBy
import com.typesafe.scalalogging.LazyLogging
import javax.inject.{Inject, Singleton}
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import protocols.RegistrationProtocol.{DoctorType, Laboratory}
import slick.jdbc.JdbcProfile
import utils.Date2SqlDate

import scala.concurrent.{ExecutionContext, Future}


trait LaboratoryComponent {
  self: HasDatabaseConfigProvider[JdbcProfile] =>

  import utils.PostgresDriver.api._

  class LaboratoryTable(tag: Tag) extends Table[Laboratory](tag, "Lab_type") with Date2SqlDate {
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)

    def laboratoryName = column[String]("lab_type")

    def * = (id.?, laboratoryName) <> (Laboratory.tupled, Laboratory.unapply _)
  }

}

@ImplementedBy(classOf[LaboratoryDaoImpl])
trait LaboratoryDao {

  def addLaboratory(data: Laboratory): Future[Int]

  def getLaboratory: Future[Seq[Laboratory]]

  def deleteLaboratory(id: Int): Future[Int]

  def updateLaboratory(data: Laboratory): Future[Int]

  def findLabType(laboratory: String): Future[Option[Laboratory]]

}

@Singleton
class LaboratoryDaoImpl @Inject()(protected val dbConfigProvider: DatabaseConfigProvider,
                               val actorSystem: ActorSystem)
                              (implicit val ec: ExecutionContext)
  extends LaboratoryDao
    with LaboratoryComponent
    with HasDatabaseConfigProvider[JdbcProfile]
    with Date2SqlDate
    with LazyLogging {

  import utils.PostgresDriver.api._

  val laboratoryTable = TableQuery[LaboratoryTable]

  override def addLaboratory(data: Laboratory): Future[Int] = {
    db.run {
      logger.debug(s"daoga keldi: $data")
      (laboratoryTable returning laboratoryTable.map(_.id)) += data
    }
  }

  override def getLaboratory: Future[Seq[Laboratory]] = {
    db.run {
      laboratoryTable.result
    }
  }

  override def deleteLaboratory(id: Int): Future[Int] = {
    db.run{
      laboratoryTable.filter(_.id === id).delete
    }
  }

  override def updateLaboratory(data: Laboratory): Future[Int] = {
    db.run{
      laboratoryTable.filter(_.id === data.id).update(data)
    }
  }

  override def findLabType(laboratory: String): Future[Option[Laboratory]] = {
    db.run{
      laboratoryTable.filter(data => data.laboratoryName === laboratory).result.headOption
    }
  }
}

