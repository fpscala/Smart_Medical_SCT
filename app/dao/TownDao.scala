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

trait TownComponent {
  self: HasDatabaseConfigProvider[JdbcProfile] =>

  import utils.PostgresDriver.api._

  class TownTable(tag: Tag) extends Table[Town](tag, "Towns") with Date2SqlDate {
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)

    def name = column[String]("town")

    def regionId = column[Int]("region_id")

    def * = (id.?, name, regionId) <> (Town.tupled, Town.unapply _)
  }

}

@ImplementedBy(classOf[TownDaoImpl])
trait TownDao {

  def getTown: Future[Seq[Town]]

}

@Singleton
class TownDaoImpl @Inject()(protected val dbConfigProvider: DatabaseConfigProvider,
                                val actorSystem: ActorSystem)
                               (implicit val ec: ExecutionContext)
  extends TownDao
    with TownComponent
    with HasDatabaseConfigProvider[JdbcProfile]
    with Date2SqlDate
    with LazyLogging {

  import utils.PostgresDriver.api._

  val townTable = TableQuery[TownTable]

  override def getTown: Future[Seq[Town]] = {
    db.run{
      townTable.result
    }
  }

}

