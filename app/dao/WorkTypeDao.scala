package dao

import akka.actor.ActorSystem
import com.google.inject.ImplementedBy
import com.typesafe.scalalogging.LazyLogging
import javax.inject.{Inject, Singleton}
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import protocols.RegistrationProtocol._
import slick.jdbc.JdbcProfile
import utils.Date2SqlDate

import scala.concurrent.{ExecutionContext, Future}

trait WorkTypeComponent extends CheckupPeriodComponent {
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
  def addWorkType(data: WorkType  ): Future[Int]

//  def getWorkTypeWithCheckupPeriod: Future[Seq[(WorkType, CheckupPeriod)]]

  def findWorkTypeIdByWorkTypeName(name: String): Future[Option[WorkType]]

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

  override def addWorkType(data: WorkType): Future[Int] = {
    db.run {
      (workType returning workType.map(_.id)) += data
    }
  }
//
//  override def getWorkTypeWithCheckupPeriod: Future[Seq[(WorkType, CheckupPeriod)]] = {
////    val query = workType.join(checkupPeriod).on(_.id === _.workType)
//    db.run(workType.result)
//  }

  override def findWorkTypeIdByWorkTypeName(name: String): Future[Option[WorkType]] = {
    db.run {
      workType.filter(_.workType === name).result.headOption
    }
  }
}

