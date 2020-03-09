package dao

import akka.actor.ActorSystem
import com.google.inject.ImplementedBy
import com.typesafe.scalalogging.LazyLogging
import javax.inject.{Inject, Singleton}
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import protocols.RegistrationProtocol._
import slick.jdbc.JdbcProfile
import utils.Date2SqlDate
import play.api.libs.json.JsValue

import scala.concurrent.{ExecutionContext, Future}


trait CheckupPeriodComponent {
  self: HasDatabaseConfigProvider[JdbcProfile] =>

  import utils.PostgresDriver.api._

  class CheckupPeriodTable(tag: Tag) extends Table[CheckupPeriod](tag, "Checkup_period") with Date2SqlDate {
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)

    def numberPerYear = column[Int]("number_per_year")

    def * = (id.?, numberPerYear) <> (CheckupPeriod.tupled, CheckupPeriod.unapply _)
  }

}

@ImplementedBy(classOf[CheckupPeriodDaoImpl])
trait CheckupPeriodDao {

  def addCheckupPeriod(data: CheckupPeriod): Future[CheckupPeriod]
}

@Singleton
class CheckupPeriodDaoImpl @Inject()(protected val dbConfigProvider: DatabaseConfigProvider,
                               val actorSystem: ActorSystem)
                              (implicit val ec: ExecutionContext)
  extends CheckupPeriodDao
    with CheckupPeriodComponent
    with HasDatabaseConfigProvider[JdbcProfile]
    with Date2SqlDate
    with LazyLogging {

  import utils.PostgresDriver.api._

  val checkupPeriod = TableQuery[CheckupPeriodTable]

  override def addCheckupPeriod(data: CheckupPeriod): Future[CheckupPeriod] = {
    db.run {
      (checkupPeriod returning checkupPeriod.map(_.id) into ((t, id) => t.copy(id = Some(id)))) += data.copy(id = data.id)
    }
  }
}

