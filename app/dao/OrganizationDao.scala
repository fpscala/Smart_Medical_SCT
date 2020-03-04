package dao

import akka.actor.ActorSystem
import com.google.inject.ImplementedBy
import com.typesafe.scalalogging.LazyLogging
import javax.inject.{Inject, Singleton}
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import play.api.libs.json.JsValue
import protocols.RegistrationProtocol.Organization
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

    def workType = column[JsValue]("work_type")

    def * = (id.?, organizationName, phoneNumber, address, email, workType) <> (Organization.tupled, Organization.unapply _)
  }

}

@ImplementedBy(classOf[OrganizationDaoImpl])
trait OrganizationDao {
  def addOrganization(data: Organization  ): Future[Int]

  def getOrganization: Future[Seq[Organization]]

  def deleteOrganization(id: Int): Future[Int]

  def updateOrganization(data: Organization): Future[Int]
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

  val organizationsTable = TableQuery[OrganizationTable]

  override def addOrganization(data: Organization): Future[Int] = {
    db.run {
      logger.warn(s"daoga keldi: $data")
      (organizationsTable returning organizationsTable.map(_.id)) += data
    }
  }

  override def getOrganization: Future[Seq[Organization]] = {
    db.run {
      organizationsTable.result
    }
  }

  override def deleteOrganization(id: Int): Future[Int] = {
    db.run{
      organizationsTable.filter(_.id === id).delete
    }
  }

  override def updateOrganization(data: Organization): Future[Int] = {
    db.run{
      organizationsTable.filter(_.id === data.id).update(data)
    }
  }


}

