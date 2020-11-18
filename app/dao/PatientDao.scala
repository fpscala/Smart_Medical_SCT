package dao

import java.util.Date

import akka.actor.ActorSystem
import com.google.inject.ImplementedBy
import com.typesafe.scalalogging.LazyLogging
import javax.inject.{Inject, Singleton}
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import play.api.libs.json.{JsValue, Json, OFormat}
import protocols.RegistrationProtocol._
import slick.jdbc.JdbcProfile
import slick.lifted.ProvenShape
import utils.Date2SqlDate

import scala.concurrent.{ExecutionContext, Future}

trait PatientComponent extends RegionComponent with TownComponent with WorkTypeComponent {
  self: HasDatabaseConfigProvider[JdbcProfile] =>

  import utils.PostgresDriver.api._

  class PatientTable(tag: Tag) extends Table[Patient](tag, "Patient") with Date2SqlDate {
    def id: Rep[Int] = column[Int]("id", O.PrimaryKey, O.AutoInc)

    def firstName: Rep[String] = column[String]("first_name")

    def middleName: Rep[String] = column[String]("middle_name")

    def lastName: Rep[String] = column[String]("last_name")

    def passport_sn: Rep[Option[String]] = column[Option[String]]("passport_sn")

    def gender: Rep[Int] = column[Int]("gender")

    def birthday: Rep[Date] = column[Date]("birthday")

    def region: Rep[Int] = column[Int]("region")

    def city: Rep[Int] = column[Int]("city")

    def address: Rep[String] = column[String]("address")

    def phone: Rep[Option[String]] = column[Option[String]]("phone_number")

    def cardNumber: Rep[String] = column[String]("card_number")

    def workTypeId: Rep[Int] = column[Int]("work_type_id")

    def lastCheckup: Rep[Date] = column[Date]("last_checkup")

    def photo: Rep[Option[String]] = column[Option[String]]("photo")

    def organizationName: Rep[Option[String]] = column[Option[String]]("organization_name")

    def specPartJson: Rep[Option[JsValue]] = column[Option[JsValue]]("spec_part_json")

    def * : ProvenShape[Patient] = (id.?, firstName, middleName, lastName, passport_sn, gender, birthday, region, city, address, phone, cardNumber, workTypeId.?, lastCheckup, photo, organizationName, specPartJson) <> (Patient.tupled, Patient.unapply)
  }

}

@ImplementedBy(classOf[PatientDaoImpl])
trait PatientDao {

  def addPatient(data: Patient): Future[Int]

  def getPatientList: Future[Seq[Patient]]

  def getPatientsByPassportSn(passport: String): Future[Seq[Patient]]

  def getCountDepartment(workTypeId: Int): Future[Int]

  def getTotalCountWorkers(organizationName: String): Future[Int]

  def delete(id: Option[Int]): Future[Int]

  def updatePatient(data: Patient): Future[Int]

  def getPatientsByOrgNameAndDepartmentId(organizationName: String, departmentId: Int): Future[Seq[Patient]]

  def getPatientsById(id: Int): Future[Option[Patient]]

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
      .joinLeft(department).on(_._1._1.workTypeId === _.id)

    db.run(query.result).map { result =>
      result.map { case (((patient, region), city), department) =>
        patient.copy(specPartJson =
          Some(Json.toJson(PatientSpecPart(region.get.name, city.get.name, Some(department.get.workType))))
        )
      }
    }
  }

  override def getCountDepartment(workTypeId: Int): Future[Int] = {
    db.run {
      patient.filter(_.workTypeId === workTypeId).length.result
    }
  }

  override def getTotalCountWorkers(organizationName: String): Future[Int] = {
    db.run {
      patient.filter(_.organizationName === organizationName).length.result
    }
  }

  override def delete(id: Option[Int]): Future[Int] = {
    db.run {
      patient.filter(_.id === id).delete
    }
  }

  override def updatePatient(data: Patient): Future[Int] = {
    db.run{
      patient.filter(_.id === data.id).update(data)
    }
  }

  override def getPatientsByPassportSn(passport: String): Future[Seq[Patient]] = {
    db.run{
      patient.filter(_.passport_sn === passport).result
    }
  }

  override def getPatientsByOrgNameAndDepartmentId(organizationName: String, department: Int): Future[Seq[Patient]] = {
    db.run{
      patient.filter(p => p.organizationName === organizationName && p.workTypeId === department).result
    }
  }

  override def getPatientsById(id: Int): Future[Option[Patient]] = {
    db.run {
      patient.filter(_.id === id).result.headOption
    }
  }
}