package dao

import akka.actor.ActorSystem
import com.google.inject.ImplementedBy
import com.typesafe.scalalogging.LazyLogging
import javax.inject.{Inject, Singleton}
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import protocols.RegistrationProtocol.{DoctorType}
import slick.jdbc.JdbcProfile
import utils.Date2SqlDate

import scala.concurrent.{ExecutionContext, Future}

trait DoctorTypeComponent {
  self: HasDatabaseConfigProvider[JdbcProfile] =>

  import utils.PostgresDriver.api._

  class DoctorTypeTable(tag: Tag) extends Table[DoctorType](tag, "Doctor_type") with Date2SqlDate {
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)

    def doctorType = column[String]("doctor_type")

    def * = (id.?, doctorType) <> (DoctorType.tupled, DoctorType.unapply _)
  }

}

@ImplementedBy(classOf[DoctorTypeDaoImpl])
trait DoctorTypeDao {
  def addDoctorType(data: DoctorType  ): Future[Int]

  def getDoctorType: Future[Seq[DoctorType]]

  def deleteDoctorType(id: Int): Future[Int]

  def updateDoctorType(data: DoctorType): Future[Int]
}

@Singleton
class DoctorTypeDaoImpl @Inject()(protected val dbConfigProvider: DatabaseConfigProvider,
                               val actorSystem: ActorSystem)
                              (implicit val ec: ExecutionContext)
  extends DoctorTypeDao
    with DoctorTypeComponent
    with HasDatabaseConfigProvider[JdbcProfile]
    with Date2SqlDate
    with LazyLogging {

  import utils.PostgresDriver.api._

  val doctorTypeTable = TableQuery[DoctorTypeTable]

  override def addDoctorType(data: DoctorType): Future[Int] = {
    db.run {
      logger.warn(s"daoga keldi: $data")
      (doctorTypeTable returning doctorTypeTable.map(_.id)) += data
    }
  }

  override def getDoctorType: Future[Seq[DoctorType]] = {
    db.run {
      doctorTypeTable.result
    }
  }

  override def deleteDoctorType(id: Int): Future[Int] = {
    db.run{
      doctorTypeTable.filter(_.id === id).delete
    }
  }

  override def updateDoctorType(data: DoctorType): Future[Int] = {
    db.run{
      doctorTypeTable.filter(_.id === data.id).update(data)
    }
  }


}

