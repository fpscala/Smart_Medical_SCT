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

trait RegionComponent {
  self: HasDatabaseConfigProvider[JdbcProfile] =>

  import utils.PostgresDriver.api._

  class RegionTable(tag: Tag) extends Table[Region](tag, "Regions") with Date2SqlDate {
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)

    def name = column[String]("region")

    def * = (id.?, name) <> (Region.tupled, Region.unapply _)
  }

}

@ImplementedBy(classOf[RegionDaoImpl])
trait RegionDao {

  def getRegion: Future[Seq[Region]]

}

@Singleton
class RegionDaoImpl @Inject()(protected val dbConfigProvider: DatabaseConfigProvider,
                                val actorSystem: ActorSystem)
                               (implicit val ec: ExecutionContext)
  extends RegionDao
    with RegionComponent
    with HasDatabaseConfigProvider[JdbcProfile]
    with Date2SqlDate
    with LazyLogging {

  import utils.PostgresDriver.api._

  val regionTable = TableQuery[RegionTable]

  override def getRegion: Future[Seq[Region]] = {
    db.run{
      regionTable.result
    }
  }

}

