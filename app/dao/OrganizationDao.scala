package dao

import akka.actor.ActorSystem
import com.google.inject.ImplementedBy
import com.typesafe.scalalogging.LazyLogging
import javax.inject.{Inject, Singleton}
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import play.api.libs.json.JsValue
import protocols.RegistrationProtocol.{Organization, WorkType}
import slick.jdbc.JdbcProfile
import utils.Date2SqlDate

import scala.concurrent.{ExecutionContext, Future}


trait OrganizationComponent {
  self: HasDatabaseConfigProvider[JdbcProfile] =>

  import utils.PostgresDriver.api._

  class OrganizationTable(tag: Tag) extends Table[Organization](tag, "Organization") with Date2SqlDate {
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)

    def organizationName = column[String]("organization_name")

    def phoneNumber = column[String]("phone_number")

    def address = column[String]("address")

    def email = column[String]("email")

    def countWorkers = column[Int]("workers_number")

    def workType = column[Int]("work_type")

    def * = (id.?, organizationName, phoneNumber, address, email, countWorkers.?, workType) <> (Organization.tupled, Organization.unapply _)
  }

}

@ImplementedBy(classOf[OrganizationDaoImpl])
trait OrganizationDao {
  def addOrganization(data: Organization): Future[Int]

  def findOrganizationByName(name: String): Future[Option[Organization]]

  def getOrganization: Future[Seq[Organization]]

  def getOrganizationName: Future[Seq[String]]

  def deleteOrganization(id: Int): Future[Int]

  def updateOrganization(data: Organization): Future[Int]

  def getWorkTypeId(name: String): Future[Seq[Int]]
}

@Singleton
class OrganizationDaoImpl @Inject()(protected val dbConfigProvider: DatabaseConfigProvider,
                               val actorSystem: ActorSystem)
                              (implicit val ec: ExecutionContext)
  extends OrganizationDao
    with OrganizationComponent
    with HasDatabaseConfigProvider[JdbcProfile]
    with Date2SqlDate
    with LazyLogging {

  import utils.PostgresDriver.api._

  val organization = TableQuery[OrganizationTable]

  override def addOrganization(data: Organization): Future[Int] = {
    db.run {
      (organization returning organization.map(_.id)) += data
    }
  }

  override def findOrganizationByName(name: String): Future[Option[Organization]] = {
    db.run{
      organization.filter(_.organizationName === name).result.headOption
    }
  }

  override def getOrganization: Future[Seq[Organization]] = {
    db.run {
      organization.result
    }
  }

  override def getOrganizationName: Future[Seq[String]] = {
    db.run{
      organization.map(_.organizationName).result
    }
  }

  override def deleteOrganization(id: Int): Future[Int] = {
    db.run{
      organization.filter(_.id === id).delete
    }
  }

  override def updateOrganization(data: Organization): Future[Int] = {
    db.run{
      organization.filter(_.id === data.id).update(data)
    }
  }

  override def getWorkTypeId(name: String): Future[Seq[Int]] = {
    db.run{
      organization.filter(_.organizationName === name).map(_.workType).result
    }
  }

}

