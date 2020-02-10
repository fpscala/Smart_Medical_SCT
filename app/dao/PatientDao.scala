package dao

import java.util.Date

import akka.actor.ActorSystem
import com.google.inject.ImplementedBy
import com.typesafe.scalalogging.LazyLogging
import javax.inject.{Inject, Singleton}
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import protocols.RegistrationProtocol.Patient
import slick.jdbc.JdbcProfile
import utils.Date2SqlDate

import scala.concurrent.{ExecutionContext, Future}


trait PatientComponent {
  self: HasDatabaseConfigProvider[JdbcProfile] =>

  import utils.PostgresDriver.api._

  class PatientTable(tag: Tag) extends Table[Patient](tag, "Patient") with Date2SqlDate {
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)

    def firstName = column[String]("first_name")

    def middleName = column[String]("middle_name")

    def lastName = column[String]("last_name")

    def passport_sn = column[Option[String]]("passport_sn")

    def gender = column[String]("gender")

    def birthday = column[Date]("birthday")

    def address = column[String]("address")

    def phone = column[Option[String]]("phone_number")

    def cardNumber = column[String]("card_number")

    def profession = column[Option[String]]("profession")

    def workTypeId = column[Option[Int]]("work_type_id")

    def lastCheckup = column[Date]("last_checkup")

    def photo = column[Option[String]]("photo")

    def organizationId = column[Option[Int]]("organization_id")

    def * = (id.?, firstName, middleName, lastName, passport_sn, gender, birthday, address, phone, cardNumber, profession, workTypeId, lastCheckup, photo, organizationId) <> (Patient.tupled, Patient.unapply _)
  }

}

@ImplementedBy(classOf[PatientDaoImpl])
trait PatientDao {

  def addPatient(data: Patient): Future[Int]

}

@Singleton
class PatientDaoImpl @Inject()(protected val dbConfigProvider: DatabaseConfigProvider,
                               val actorSystem: ActorSystem)
                              (implicit val ec: ExecutionContext)
  extends PatientDao
    with PatientComponent
    with HasDatabaseConfigProvider[JdbcProfile]
    with Date2SqlDate
    with LazyLogging {

  import utils.PostgresDriver.api._

  val PatientTable = TableQuery[PatientTable]

  override def addPatient(data: Patient): Future[Int] = {
    logger.warn(s"dwa")
    db.run {
      (PatientTable returning PatientTable.map(_.id)) += data
    }
  }

}

