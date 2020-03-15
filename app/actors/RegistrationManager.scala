package actors

import java.nio.file.{Files, Path, Paths}

import akka.actor.Actor
import akka.pattern.pipe
import akka.util.Timeout
import com.typesafe.scalalogging.LazyLogging
import dao._
import javax.inject.Inject
import play.api.{Configuration, Environment}
import protocols.RegistrationProtocol.{GetCheckupId, _}

import scala.concurrent.duration.DurationInt
import scala.concurrent.{ExecutionContext, Future}

class RegistrationManager @Inject()(val environment: Environment,
                                    val configuration: Configuration,
                                    val organizationDao: OrganizationDao,
                                    val laboratoryDao: LaboratoryDao,
                                    val patientDao: PatientDao,
                                    val doctorTypeDao: DoctorTypeDao,
                                    val workTypeDao: WorkTypeDao,
                                    val checkupPeriodDao: CheckupPeriodDao
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

    case AddDoctorType(data) =>
      addDoctorType(data).pipeTo(sender())

    case UpdateDoctorType(data) =>
      updateDoctorType(data).pipeTo(sender())

    case DeleteDoctorType(id) =>
      deleteDoctorType(id).pipeTo(sender())

    case DeleteWorkType(id) =>
      deleteWorkTypeAndCheckupPeriod(id).pipeTo(sender())

    case GetDoctorTypeList =>
      getDoctorTypeList.pipeTo(sender())

    case GetPatient =>
      getPatientList.pipeTo(sender())

    case CreatePatient(data) =>
      createPatient(data).pipeTo(sender())

    case DeletePatient(id) =>
      deletePatient(id).pipeTo(sender())

    case AddImage(fileName, imgData) =>
      addImage(fileName, imgData).pipeTo(sender())

    case AddCheckupPeriod(data) =>
      addCheckupPeriod(data).pipeTo(sender())

    case AddWorkType(data) =>
      addWorkType(data).pipeTo(sender())

    case GetWorkTypeWithCheckupPeriod =>
      getWorkTypeWithCheckupPeriod.pipeTo(sender())

    case GetDepartmentList =>
      getWorkTypeList.pipeTo(sender())

    case FindWorkTypeIdByWorkType(workType) =>
      findWorkTypeIdByWorkType(workType).pipeTo(sender())

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

  def deletePatient(id: Option[Int]) = {
    patientDao.delete(id)
  }

  def getPatientList = {
    patientDao.getPatientList
  }

  private def addLaboratory(data: Laboratory): Future[Either[String, String]] = {
    (for {
      response <- laboratoryDao.findLabType(data.laboratoryName)
    } yield response match {
      case Some(laboratoryCount) =>
        Future.successful(Left(laboratoryCount.laboratoryName + " bunday laboratory type avval kiritilgan!"))
      case None =>
        laboratoryDao.addLaboratory(data)
        Future.successful(Right(data.laboratoryName + " nomli laboratory type muvoffaqiyatli qo'shildi!"))
    }).flatten
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

  private def addDoctorType(data: DoctorType): Future[Either[String, String]] = {
    (for {
      response <- doctorTypeDao.findDoctorType(data.doctorType)
    } yield response match {
      case Some(doctorCount) =>
        Future.successful(Left(doctorCount.doctorType + " bunday doctorType avval kiritilgan!"))
      case None =>
        doctorTypeDao.addDoctorType(data)
        Future.successful(Right(data.doctorType + " nomli doctorType muvoffaqiyatli qo'shildi!"))
    }).flatten
  }

  private def getDoctorTypeList: Future[Seq[DoctorType]] = {
    doctorTypeDao.getDoctorType
  }

  private def deleteDoctorType(id: Int): Future[Int] = {
    doctorTypeDao.deleteDoctorType(id)
  }

  private def deleteWorkTypeAndCheckupPeriod(id: Int): Future[Int] = {
    workTypeDao.deleteWorkType(id)
  }

  private def updateDoctorType(data: DoctorType): Future[Int] = {
    doctorTypeDao.updateDoctorType(data)
  }

  private def addCheckupPeriod(data: CheckupPeriod): Future[Int] = {
      checkupPeriodDao.addCheckupPeriod(data)
  }

  private def addWorkType(data: WorkType): Future[WorkType] = {
    workTypeDao.addWorkType(data)
  }

  private def getWorkTypeWithCheckupPeriod = {
    workTypeDao.getWorkTypeWithCheckupPeriod
  }

  private def getWorkTypeList = {
    workTypeDao.getWorkType
  }

  private def findWorkTypeIdByWorkType(workType: String): Future[Option[Int]] = {
    workTypeDao.findWorkTypeIdByWorkTypeName(workType).mapTo[Option[WorkType]].map {
      case Some(workType) => workType.id
      case None => None
    }
  }

}
