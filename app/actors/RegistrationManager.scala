package actors

import java.nio.file.{Files, Path, Paths}
import akka.actor.Actor
import akka.pattern.pipe
import akka.util.Timeout
import com.typesafe.scalalogging.LazyLogging
import dao._
import javax.inject.Inject
import play.api.{Configuration, Environment}
import protocols.RegistrationProtocol._

import scala.concurrent.duration.DurationInt
import scala.concurrent.{ExecutionContext, Future}

class RegistrationManager @Inject()(val environment: Environment,
                                    val organizationDao: OrganizationDao,
                                    val laboratoryDao: LaboratoryDao
                                    val configuration: Configuration,
                                    val patientDao: PatientDao,
                                    val doctorTypeDao: DoctorTypeDao
                                    //                                    val laboratoryDao: LaboratoryDao
                                   )
                                   (implicit val ec: ExecutionContext)
  extends Actor with LazyLogging {
  val config: Configuration = configuration.get[Configuration]("server")
  val imagesPath: String = config.get[String]("images-files")
  val imagesDir: Path = Paths.get(imagesPath)


  implicit val defaultTimeout: Timeout = Timeout(60.seconds)

  def receive = {

    case AddLaboratory(data) =>
      addLaboratory(data).pipeTo(sender())

    case GetLaboratoryList =>
      getLaboratoryList.pipeTo(sender())

    case DeleteLaboratory(id) =>
      deleteLaboratory(id).pipeTo(sender())

    case UpdateLaboratory(data) =>
      updateLaboratory(data).pipeTo(sender())

    case AddOrganization(data) =>
      addOrganization(data).pipeTo(sender())

    case UpdateOrganization(data) =>
      updateOrganization(data).pipeTo(sender())

    case DeleteOrganization(id) =>
      deleteOrganization(id).pipeTo(sender())

    case GetOrganizationList =>
      getOrganizationList.pipeTo(sender())

    case AddDoctorType(data) =>
      addDoctorType(data).pipeTo(sender())

    case UpdateDoctorType(data) =>
      updateDoctorType(data).pipeTo(sender())

    case DeleteDoctorType(id) =>
      deleteDoctorType(id).pipeTo(sender())

    case GetDoctorTypeList =>
      getDoctorTypeList.pipeTo(sender())

    case CreatePatient(data) =>
      createPatient(data).pipeTo(sender())

    case AddImage(fileName, imgData) =>
      addImage(fileName, imgData).pipeTo(sender())

    //    case GetRegistrationList =>
    //      getRegistrationList.pipeTo(sender())
    //
    case _ => logger.info(s"received unknown message")
  }


  def addImage(filename: String, imageData: Array[Byte]): Future[Unit] = {
    Future {
      Files.write(imagesDir.resolve(filename), imageData)
    }
  }
  def createPatient(patientData: Patient) = {
    patientDao.addPatient(patientData)
  }

  private def addLaboratory(data: Laboratory): Future[Int] = {
    laboratoryDao.addLaboratory(data)
  }


  private def getLaboratoryList: Future[Seq[Laboratory]] = {
    laboratoryDao.getLaboratory
  }

  private def deleteLaboratory(id: Int): Future[Int] = {
    laboratoryDao.deleteLaboratory(id)
  }

  private def updateLaboratory(data: Laboratory): Future[Int] = {
    laboratoryDao.updateLaboratory(data)
  }

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

  private def addDoctorType(data: DoctorType): Future[Int] = {
    doctorTypeDao.addDoctorType(data)
  }

  private def getDoctorTypeList: Future[Seq[DoctorType]] = {
    doctorTypeDao.getDoctorType
  }

  private def deleteDoctorType(id: Int): Future[Int] = {
    doctorTypeDao.deleteDoctorType(id)
  }

  private def updateDoctorType(data: DoctorType): Future[Int] = {
    doctorTypeDao.updateDoctorType(data)
  }

}
