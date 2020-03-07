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


trait TmpTableComponent {
  self: HasDatabaseConfigProvider[JdbcProfile] =>

  import utils.PostgresDriver.api._

  class TmpTableTable(tag: Tag) extends Table[TmpTable](tag, "Tmp_table") with Date2SqlDate {
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)

    def workTypeId = column[Int]("work_type_id")

    def checkupId = column[Int]("checkup_period_id")

    def docTypeId = column[Int]("doctor_type_id")

    def labTypeId = column[Int]("lab_type_id")

    def * = (workTypeId, checkupId, docTypeId, labTypeId) <> (TmpTable.tupled, TmpTable.unapply _)
  }

}

@ImplementedBy(classOf[TmpTableDaoImpl])
trait TmpTableDao {

  def addTmpTable(data: TmpTable): Future[Int]

}

@Singleton
class TmpTableDaoImpl @Inject()(protected val dbConfigProvider: DatabaseConfigProvider,
                                val actorSystem: ActorSystem)
                               (implicit val ec: ExecutionContext)
  extends TmpTableDao
    with TmpTableComponent
    with HasDatabaseConfigProvider[JdbcProfile]
    with Date2SqlDate
    with LazyLogging {

  import utils.PostgresDriver.api._

  val tmpTable = TableQuery[TmpTableTable]

  override def addTmpTable(data: TmpTable): Future[Int] = {
    db.run {
      (tmpTable returning tmpTable.map(_.id)) += data
    }
  }
}

