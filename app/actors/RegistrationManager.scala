package actors

import akka.actor.Actor
import akka.pattern.pipe
import akka.util.Timeout
import com.typesafe.scalalogging.LazyLogging
import dao._
import javax.inject.Inject
import play.api.Environment
import protocols.RegistrationProtocol._

import scala.concurrent.duration.DurationInt
import scala.concurrent.{ExecutionContext, Future}

class RegistrationManager @Inject()(val environment: Environment,
                                    val organizationDao: OrganizationDao,
                                    //                                    val laboratoryDao: LaboratoryDao
                                   )
                                   (implicit val ec: ExecutionContext)
  extends Actor with LazyLogging {

  implicit val defaultTimeout: Timeout = Timeout(60.seconds)

  def receive = {

//    case AddLaboratory(data) =>
//      addLaboratory(data).pipeTo(sender())

    case AddOrganization(data) =>
      addOrganization(data).pipeTo(sender())

    case UpdateOrganization(data) =>
      updateOrganization(data).pipeTo(sender())

    case DeleteOrganization(id) =>
      deleteOrganization(id).pipeTo(sender())

    case GetOrganizationList =>
      getOrganizationList.pipeTo(sender())

    //    case AddRegistration(data) =>
    //      addRegistration(data).pipeTo(sender())
    //
    //    case GetRegistrationList =>
    //      getRegistrationList.pipeTo(sender())
    //
    case _ => logger.info(s"received unknown message")
  }

  //
  //  private def addRegistration(data: Registration) = {
  //
  //  }
  //
  //  private def getRegistrationList = {
  //
  //  }

//  private def addLaboratory(data: Laboratory): Future[Int] = {
//    laboratoryDao.addLaboratory(data)
//  }


  private def addOrganization(data: Organization): Future[Int] = {
    organizationDao.addOrganization(data)
  }

  private def getOrganizationList: Future[Seq[Organization]] = {
    organizationDao.getOrganization
  }

  private def deleteOrganization(id: Int): Future[Int] = {
    organizationDao.deleteOrganization(id)
  }

  private def updateOrganization(data: Organization): Future[Int] = {
    organizationDao.updateOrganization(data)
  }

}
