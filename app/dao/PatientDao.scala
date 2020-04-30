package dao

import java.util.Date

import akka.actor.ActorSystem
import com.google.inject.ImplementedBy
import com.typesafe.scalalogging.LazyLogging
import javax.inject.{Inject, Singleton}
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import play.api.libs.json.{JsValue, Json, OFormat}
import protocols.RegistrationProtocol.Patient
import slick.jdbc.JdbcProfile
import utils.Date2SqlDate

import scala.concurrent.{ExecutionContext, Future}


trait PatientComponent extends RegionComponent with TownComponent with WorkTypeComponent {
  self: HasDatabaseConfigProvider[JdbcProfile] =>

  import utils.PostgresDriver.api._

  class PatientTable(tag: Tag) extends Table[Patient](tag, "Patient") with Date2SqlDate {
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)

    def firstName = column[String]("first_name")

    def middleName = column[String]("middle_name")

    def lastName = column[String]("last_name")

    def passport_sn = column[Option[String]]("passport_sn")

    def gender = column[Int]("gender")

    def birthday = column[Date]("birthday")

    def region = column[Int]("region")

    def city = column[Int]("city")

    def address = column[String]("address")

    def phone = column[Option[String]]("phone_number")

    def cardNumber = column[String]("card_number")

    def workTypeId = column[Option[Int]]("work_type_id")

    def lastCheckup = column[Date]("last_checkup")

    def photo = column[Option[String]]("photo")

    def organizationName = column[Option[String]]("organization_name")

    def specPartJson = column[Option[JsValue]]("spec_part_json")

    def * = (id.?, firstName, middleName, lastName, passport_sn, gender, birthday, region, city, address, phone, cardNumber, workTypeId, lastCheckup, photo, organizationName, specPartJson) <> (Patient.tupled, Patient.unapply _)
  }

}

@ImplementedBy(classOf[PatientDaoImpl])
trait PatientDao {

  def addPatient(data: Patient): Future[Int]

  def getPatientList: Future[Seq[Patient]]

  def getCountDepartment(workTypeId: Int): Future[Int]

  def getTotalCountWorkers(organizationName: String): Future[Int]

  def delete(id: Option[Int]): Future[Int]
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

  val patient = TableQuery[PatientTable]
  val department = TableQuery[WorkTypeTable]
  val city = TableQuery[TownTable]
  val region = TableQuery[RegionTable]

  override def addPatient(data: Patient): Future[Int] = {
    db.run {
      (patient returning patient.map(_.id)) += data
    }
  }

  case class PatientSpecPart(region: String, city: String, department: Option[String] = None)

  implicit val patientSpecPartFormat: OFormat[PatientSpecPart] = Json.format[PatientSpecPart]

  override def getPatientList: Future[Seq[Patient]] = {
    val query = patient
      .joinLeft(region).on(_.region === _.id)
      .joinLeft(city).on(_._1.city === _.id)
      .joinLeft(department).on(_._1._1.workTypeId.getOrElse(0) === _.id)

    db.run(query.result).map { rerult =>
      rerult.map { case (((patient, region), city), department) =>
        (patient.copy(specPartJson =
          Some(Json.toJson(PatientSpecPart(region.get.name, city.get.name, Some(department.get.workType))))
        ))
      }
    }
  }

  override def getCountDepartment(workTypeId: Int): Future[Int] = {
    db.run{
      patient.filter(_.workTypeId === workTypeId).length.result
    }
  }

  override def getTotalCountWorkers(organizationName: String): Future[Int] = {
    db.run{
      patient.filter(_.organizationName === organizationName).length.result
    }
  }

  override def delete(id: Option[Int]): Future[Int] = {
    db.run {
      patient.filter(_.id === id).delete
    }
  }
}

