package dao

import akka.actor.ActorSystem
import com.google.inject.ImplementedBy
import com.typesafe.scalalogging.LazyLogging
import javax.inject.{Inject, Singleton}
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import play.api.libs.json.{Json, OFormat}
import protocols.RegistrationProtocol._
import slick.jdbc.JdbcProfile
import utils.Date2SqlDate

import scala.concurrent.{ExecutionContext, Future}

trait WorkTypeComponent extends CheckupPeriodComponent with DoctorTypeComponent with LaboratoryComponent {
  self: HasDatabaseConfigProvider[JdbcProfile] =>

  import utils.PostgresDriver.api._

  class WorkTypeTable(tag: Tag) extends Table[WorkType](tag, "Work_type") with Date2SqlDate {
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)

    def workType = column[String]("work_type")

    def * = (id.?, workType) <> (WorkType.tupled, WorkType.unapply _)
  }

}

@ImplementedBy(classOf[WorkTypeDaoImpl])
trait WorkTypeDao {
  def addWorkType(data: WorkType): Future[WorkType]

  def deleteWorkType(id: Int): Future[Int]

  def getWorkTypeWithCheckupPeriod: Future[Seq[(WorkType, CheckupPeriod)]]

  def getWorkType: Future[Seq[WorkType]]

  def getWorkTypeById(id: Int): Future[Option[WorkType]]

  def findWorkTypeIdByWorkTypeName(name: String): Future[Option[WorkType]]

  def findDepartment(name: String): Future[Option[WorkType]]

}

@Singleton
class WorkTypeDaoImpl @Inject()(protected val dbConfigProvider: DatabaseConfigProvider,
                                val actorSystem: ActorSystem)
                               (implicit val ec: ExecutionContext)
  extends WorkTypeDao
    with WorkTypeComponent
    with HasDatabaseConfigProvider[JdbcProfile]
    with Date2SqlDate
    with LazyLogging {

  import utils.PostgresDriver.api._

  val workType = TableQuery[WorkTypeTable]
  val checkupPeriod = TableQuery[CheckupPeriodTable]
  val doctorTypeTable = TableQuery[DoctorTypeTable]
  val laboratoryTable = TableQuery[LaboratoryTable]


  override def addWorkType(data: WorkType): Future[WorkType] = {
    db.run {
      (workType returning workType.map(_.id) into ((t, id) => t.copy(id = Some(id)))) += data.copy(id = data.id)
    }
  }

  override def deleteWorkType(id: Int): Future[Int] = {
    logger.warn(s"id: $id")
    db.run {
      workType.filter(_.id === id).delete
    }
  }

  case class SpecPart(docType: Option[String] = None, labType: Option[String] = None)

  implicit val specPartFormat: OFormat[SpecPart] = Json.format[SpecPart]


  override def getWorkTypeWithCheckupPeriod: Future[Seq[(WorkType, CheckupPeriod)]] = {
    val query = workType.join(checkupPeriod).on(_.id === _.workTypeId)
      .joinLeft(doctorTypeTable).on(_._2.docTypeId === _.id)
      .joinLeft(laboratoryTable).on(_._1._2.labTypeId === _.id)

    db.run(query.result).map { r =>
      r.map { case (((w, ch), d), l) =>
        (w, ch.copy(specPartJson = if (d.isEmpty) {
          Some(Json.toJson(SpecPart(Some(""), Some(l.get.laboratoryName))))
        } else if (l.isEmpty) {
          Some(Json.toJson(SpecPart(Some(d.get.doctorType), Some(""))))
        } else {
          Some(Json.toJson(SpecPart(Some(d.get.doctorType), Some(l.get.laboratoryName))))
        }
        ))
      }
    }
  }

  override def getWorkType: Future[Seq[WorkType]] = {
    db.run(workType.result)
  }

  override def getWorkTypeById(id: Int): Future[Option[WorkType]] = {
    db.run{
      workType.filter(_.id === id).result.headOption
    }
  }

  override def findWorkTypeIdByWorkTypeName(name: String): Future[Option[WorkType]] = {
    db.run {
      workType.filter(_.workType === name).result.headOption
    }
  }

  override def findDepartment(name: String): Future[Option[WorkType]] = {
    db.run(workType.filter(_.workType === name).result.headOption)
  }
}

