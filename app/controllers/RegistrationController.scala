package controllers

import java.net.URLDecoder
import java.nio.file.{Files, Path}
import java.text.SimpleDateFormat
import java.util.Date

import akka.actor.ActorRef
import akka.pattern.ask
import akka.util.Timeout
import cats.implicits._
import com.typesafe.scalalogging.LazyLogging
import javax.inject._
import org.webjars.play.WebJarsUtil
import play.api.Configuration
import play.api.libs.Files.TemporaryFile
import play.api.libs.json.{JsValue, Json, OWrites}
import play.api.mvc.{Action, _}
import protocols.RegistrationProtocol._
import views.html._
import views.html.checkupPeriod._
import views.html.patient._
import views.html.settings._

import scala.concurrent.ExecutionContext
import scala.concurrent.duration.DurationInt

@Singleton
class RegistrationController @Inject()(val controllerComponents: ControllerComponents,
                                       val configuration: Configuration,
                                       implicit val webJarsUtil: WebJarsUtil,
                                       @Named("registration-manager") val registrationManager: ActorRef,
                                       indexTemplate: index,
                                       loginTemplate: auth.login,
                                       registrationPatient: registration_patient,
                                       dashboard_patient: dashboard,
                                       registrationOrganization: organization,
                                       doctorTypeTemplate: doctor_type,
                                       workTypeTemplate: checkupPeriod,
                                       registrationTemplate: registration,
                                      )
                                      (implicit val ec: ExecutionContext)
  extends BaseController with LazyLogging {

  implicit val defaultTimeout: Timeout = Timeout(60.seconds)
  val language: String = configuration.get[String]("language")

  def index: Action[AnyContent] = Action {
    Ok(indexTemplate(language))
  }

  def login: Action[AnyContent] = Action {
    Ok(loginTemplate())
  }

  def workType: Action[AnyContent] = Action {
    Ok(workTypeTemplate(language))
  }

  def doctorType: Action[AnyContent] = Action {
    Ok(doctorTypeTemplate(language))
  }

  def patient: Action[AnyContent] = Action {
    Ok(registrationPatient(language))
  }

  def patient_dashboard: Action[AnyContent] = Action {
    Ok(dashboard_patient(language))
  }

  def organization: Action[AnyContent] = Action {
    Ok(registrationOrganization(language))
  }

  def registration: Action[AnyContent] = Action {
    Ok(registrationTemplate(language))
  }

  def addLaboratory(): Action[JsValue] = Action.async(parse.json) { implicit request => {
    val laboratoryName = (request.body \ "laboratoryName").as[String]
    (registrationManager ? AddLaboratory(Laboratory(None, laboratoryName))).mapTo[Either[String, String]].map {
      case Right(str) =>
        Ok(Json.toJson(str))
      case Left(err) =>
        Ok(err)
    }
  }
  }


  def getLaboratoryName: Action[AnyContent] = Action.async {
    (registrationManager ? GetLaboratoryList).mapTo[Seq[Laboratory]].map { laboratoryName =>
      Ok(Json.toJson(laboratoryName.sortBy(_.id)))
    }
  }

  def deleteLaboratory(): Action[JsValue] = Action.async(parse.json) { implicit request =>
    val id = (request.body \ "id").as[Int]
    (registrationManager ? DeleteLaboratory(id)).mapTo[Int].map { i =>
      if (i != 0) {
        Ok(Json.toJson(id + " raqamli laboratory type o`chirildi"))
      }
      else {
        Ok("Bunday raqamli laboratory type topilmadi")
      }
    }
  }

  def updateLaboratory(): Action[JsValue] = Action.async(parse.json) { implicit request =>
    val id = (request.body \ "id").as[Int]
    val laboratoryName = (request.body \ "laboratoryName").as[String]
    (registrationManager ? UpdateLaboratory(Laboratory(Some(id), laboratoryName))).mapTo[Int].map { i =>
      if (i != 0) {
        Ok(Json.toJson(id + " raqamli laboratory type yangilandi"))
      }
      else {
        Ok("Bunday raqamli laboratory type topilmadi")
      }
    }
  }

  def addOrganization(): Action[JsValue] = Action.async(parse.json) { implicit request => {
    val organizationName = (request.body \ "organizationName").as[String]
    val phoneNumber = (request.body \ "phoneNumber").as[String]
    val address = (request.body \ "address").as[String]
    val email = (request.body \ "email").as[String]
    val workTypeList = (request.body \ "department").as[Array[Int]]
    (registrationManager ? AddOrganization(OrganizationReader(None, organizationName, phoneNumber, address, email, workTypeList))).mapTo[Either[String, String]].map {
      case Right(str) =>
        Ok(Json.toJson(str))
      case Left(err) =>
        Ok(err)
    }.recover {
      case err =>
        logger.error(s"err: $err")
        BadRequest("error")
    }
  }
  }

  def getOrganization: Action[AnyContent] = Action.async {
    (registrationManager ? GetOrganizationList).mapTo[Seq[Organization]].map { organizations =>
      val grouped = organizations.groupBy(data => OrganizationData(data.organizationName, data.address, data.phoneNumber, data.email, data.totalWorkers)).toSeq
      Ok(Json.toJson(grouped))
    }
  }

  def getOrganizationName: Action[AnyContent] = Action.async {
    (registrationManager ? GetOrganizationDist).mapTo[Seq[OrganizationName]].map { organization =>
      Ok(Json.toJson(organization.sortBy(_.organizationName)))
    }
  }

  def deleteOrganization(): Action[JsValue] = Action.async(parse.json) { implicit request =>
    val id = (request.body \ "id").as[Int]
    (registrationManager ? DeleteOrganization(id)).mapTo[Int].map { i =>
      if (i != 0) {
        Ok(Json.toJson(id + " raqamli organization o`chirildi"))
      }
      else {
        Ok("Bunday raqamli organization topilmadi")
      }
    }
  }

  def addDoctorType(): Action[JsValue] = Action.async(parse.json) { implicit request => {
    val doctorTypeName = (request.body \ "doctorType").as[String]
    logger.debug(s"doctor Type: $doctorTypeName")
    (registrationManager ? AddDoctorType(DoctorType(None, doctorTypeName))).mapTo[Either[String, String]].map {
      case Right(str) =>
        Ok(Json.toJson(str))
      case Left(err) =>
        Ok(err)
    }
  }
  }

  def getDoctorTypeName: Action[AnyContent] = Action.async {
    (registrationManager ? GetDoctorTypeList).mapTo[Seq[DoctorType]].map { doctorTypeName =>
      Ok(Json.toJson(doctorTypeName.sortBy(_.id)))
    }
  }

  def getWorkType: Action[AnyContent] = Action.async {
    (registrationManager ? GetDepartmentList).mapTo[Seq[WorkType]].map { department =>
      Ok(Json.toJson(department.sortBy(_.id)))
    }
  }

  def deleteDoctorType(): Action[JsValue] = Action.async(parse.json) { implicit request =>
    val id = (request.body \ "id").as[Int]
    (registrationManager ? DeleteDoctorType(id)).mapTo[Int].map { i =>
      if (i != 0) {
        Ok(Json.toJson(id + " raqamli doctor type o`chirildi"))
      } else {
        Ok("Bunday raqamli doctor type topilmadi")
      }
    }
  }

  def deleteWorkType(): Action[JsValue] = Action.async(parse.json) { implicit request =>
    val id = (request.body \ "id").as[Int]
    (registrationManager ? DeleteWorkType(id)).mapTo[Int].map { i =>
      if (i != 0) {
        Ok(Json.toJson(id + " raqamli doctor type o`chirildi"))
      } else {
        Ok("Bunday raqamli doctor type topilmadi")
      }
    }
  }

  def updateDoctorType(): Action[JsValue] = Action.async(parse.json) { implicit request =>
    val id = (request.body \ "id").as[Int]
    val doctorTypeName = (request.body \ "doctorTypeName").as[String]
    (registrationManager ? UpdateDoctorType(DoctorType(Some(id), doctorTypeName))).mapTo[Int].map { i =>
      if (i != 0) {
        Ok(Json.toJson(id + " raqamli doctor type yangilandi"))
      } else {
        Ok("Bunday raqamli doctor type topilmadi")
      }
    }
  }

  def addCheckupPeriod(): Action[JsValue] = Action.async(parse.json) { implicit request => {
    val department = (request.body \ "workType").as[String]
    val checkupPeriodForm = (request.body \ "form").as[Array[CheckupPeriodForm]]
    (registrationManager ? AddDepartmentAndCheckupPeriod(department, checkupPeriodForm)).mapTo[Either[String, String]].map {
      case Right(str) =>
        Ok(Json.toJson(str))
      case Left(err) =>
        Ok(err)
    }.recover {
      case err =>
        logger.error(s"errorWork: $err")
        BadRequest("error")
    }
  }
  }

  def createPatient(): Action[MultipartFormData[TemporaryFile]] = Action.async(parse.multipartFormData) {
    implicit request: Request[MultipartFormData[TemporaryFile]] =>
      val body = request.body.asFormUrlEncoded
      val firstName = body("firstName").head
      val middleName = body("middleName").head
      val lastName = body("lastName").head
      val passport_sn = body("passport_sn").headOption
      val birthday = parseDate(body("birthday").head)
      val gender = body("gender").head.toInt
      val phone = body("phone").headOption
      val region = body("region").head.toInt
      val city = body("city").head.toInt
      val address = body("address").head
      val organizationName = body("organizationName").headOption
      val workerTypeId = body("department").headOption.map(_.toInt)
      val cardNumber = body("cardNumber").head
      val patient = Patient(None, firstName, middleName, lastName, passport_sn, gender, birthday, region, city,
        address, phone, cardNumber, workerTypeId, new Date, organizationName = organizationName)
      request.body.file("attachedFile").map { temp =>
        val fileName = filenameGenerator()
        val imgData = getBytesFromPath(temp.ref.path)
        for {
          _ <- (registrationManager ? AddImage(fileName, imgData)).mapTo[Unit]
          _ <- (registrationManager ? CreatePatient(patient.copy(photo = fileName.some))).mapTo[Int]
        } yield {
          Ok("OK")
        }
      }.getOrElse {
        (registrationManager ? CreatePatient(patient)).mapTo[Int].map { _ =>
          Ok("OK")
        }
      }
  }

  def getPatientList: Action[AnyContent] = Action.async {
    (registrationManager ? GetPatient).mapTo[Seq[Patient]].map { p =>
      Ok(Json.toJson(p))
    }.recover {
      case err =>
        logger.error(s"error: $err")
        BadRequest
    }
  }

  case class GroupedWorkType(workType: String, tables: Seq[(WorkType, CheckupPeriod)])

  implicit val groupedWorkTypeWrites: OWrites[GroupedWorkType] = Json.writes[GroupedWorkType]

  def getWorkTypeAndCheckupPeriod: Action[AnyContent] = Action.async {
    (registrationManager ? GetWorkTypeWithCheckupPeriod).mapTo[Seq[(WorkType, CheckupPeriod)]].map { seqCheckup =>
      val grouped = seqCheckup.groupBy(_._1).view.mapValues(v => v.groupBy(_._2.numberPerYear))
      Ok(Json.toJson(grouped))
    }.recover {
      case err =>
        logger.warn(s"error: $err")
        BadRequest("ddd")
    }
  }

  def deletePatient(): Action[JsValue] = Action.async(parse.json) { implicit request =>
    val id = (request.body \ "id").as[Int]
    (registrationManager ? DeletePatient(Some(id))).mapTo[Int].map { bool =>
      if (bool == 1) {
        Ok(Json.toJson(s"$id reqamli bemor o'chirildi"))
      } else {
        Ok(Json.toJson(s"Bunday raqamli bemor mavjud emas!"))
      }
    }
  }

  def getRegion: Action[AnyContent] = Action.async {
    (registrationManager ? GetRegion).mapTo[Seq[Region]].map { region =>
      Ok(Json.toJson(region.sortBy(_.id)))
    }.recover {
      case err =>
        logger.error(s"Get Regions error: $err")
        BadRequest("Read Regions failed")
    }
  }

  def getTown: Action[JsValue] = Action.async(parse.json) { implicit request =>
    val id = (request.body \ "id").as[Int]
    (registrationManager ? GetTown(id)).mapTo[Seq[Town]].map { towns =>
      Ok(Json.toJson(towns.sortBy(_.id)))
    }.recover {
      case err =>
        logger.error(s"Get Towns error: $err")
        BadRequest("Read Towns failed")
    }
  }

  def getWorkTypeForOrganization: Action[JsValue] = Action.async(parse.json) { implicit request =>
    val name = (request.body \ "name").as[String]
    (registrationManager ? GetWorkTypeByOrganizationName(name)).mapTo[Seq[WorkType]].map { workType =>
      Ok(Json.toJson(workType.sortBy(_.id)))
    }.recover {
      case err =>
        logger.error(s"Get Department error: $err")
        BadRequest("Get Department failed")
    }
  }

  def searchByName: Action[JsValue] = Action.async(parse.json) { implicit request =>
    val fullName = (request.body \ "fullName").as[String]
    (registrationManager ? GetPatientsByFullName(fullName)).mapTo[Seq[Patient]].map { patients =>
      Ok(Json.toJson(patients.sortBy(_.lastCheckup)))
    }.recover {
      case err =>
        logger.error(s"Search Patient by name error: $err")
        BadRequest("Search Patient by name failed")
    }
  }

  def searchByPassportSn: Action[JsValue] = Action.async(parse.json) { implicit request =>
    val passport = (request.body \ "passportSn").as[String]
    (registrationManager ? GetPatientsByPassportSn(passport)).mapTo[Seq[Patient]].map { patients =>
      Ok(Json.toJson(patients.sortBy(_.id)))
    }.recover {
      case err =>
        logger.error(s"Search Patient by passport error: $err")
        BadRequest("Search Patient by passport failed")
    }
  }

  def getPatientByDepartment: Action[JsValue] = Action.async(parse.json) { implicit request =>

    val organizationName = (request.body \ "organizationName").as[String]
    val departmentId = (request.body \ "departmentId").as[Int]
    (registrationManager ? GetPatientsByOrgNameAndDepartmentId(organizationName, departmentId)).mapTo[Seq[Patient]].map { department =>
      Ok(Json.toJson(department.sortBy(_.id)))
    }.recover {
      case err =>
        logger.error(s"Search Patient by department error: $err")
        BadRequest("Search Patient by department failed")
    }
  }


  def updatePatient(): Action[JsValue] = Action.async(parse.json) { implicit request =>
    val id = (request.body \ "id").asOpt[Int]
    val firstName = (request.body \ "firstName").as[String]
    val lastName = (request.body \ "lastName").as[String]
    val middleName = (request.body \ "middleName").as[String]
    val gender = (request.body \ "gender").as[Int]
    val passport_sn = (request.body \ "passport_sn").asOpt[String]
    val phone = (request.body \ "phone").asOpt[String]
    val cardNumber = (request.body \ "cardNumber").as[String]
    val birthday = (request.body \ "birthday").as[Date]
    val address = (request.body \ "address").as[String]
    (registrationManager ? UpdatePatient(Patient(id, firstName, lastName, middleName, passport_sn, gender,
      birthday, 0, 0, address, phone, cardNumber, None, new Date(), None, None, None))).mapTo[Int].map { i =>
      if (i != 0) {
        Ok(Json.toJson(id + " raqamli bemor malumotlari yangilandi"))
      } else {
        Ok("Bunday raqamli bemor topilmadi")
      }
    }.recover{
      case err =>
        logger.error(s"Update Patient Info error: $err")
        BadRequest("error")
    }
  }

  def patientsRegistration: Action[JsValue] = Action.async(parse.json) { implicit request =>
    val patientIds = (request.body \ "ids").as[List[Int]]
    (registrationManager ? UpdateLastCheckupDatePatients(patientIds)).mapTo[Int].map { _ =>
      Ok(Json.toJson("Ok"))
    }
  }

  private def filenameGenerator(): String = {
    new Date().getTime.toString + ".png"
  }

  private def getBytesFromPath(filePath: Path): Array[Byte] = {
    Files.readAllBytes(filePath)
  }

  private def parseDate(dateStr: String): Date = {
    val dateFormat = new SimpleDateFormat("dd/MM/yyyy")
    util.Try(dateFormat.parse(URLDecoder.decode(dateStr, "UTF-8"))).toOption.get
  }
}

