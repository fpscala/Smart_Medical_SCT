package controllers

import java.net.URLDecoder
import java.nio.file.{Files, Path}
import java.text.SimpleDateFormat
import java.util.Date

import akka.actor.ActorRef
import akka.pattern.ask
import akka.util.Timeout
import com.typesafe.scalalogging.LazyLogging
import javax.inject._
import org.webjars.play.WebJarsUtil
import play.api.Configuration
import play.api.libs.Files.TemporaryFile
import play.api.libs.json.{JsValue, Json}
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
                                       registrationPatient: registration_patient,
                                       dashboard_patient: dashboard,
                                       registrationOrganization: organization,
                                       doctorTypeTemplate: doctor_type,
                                       workTypeTemplate: checkupPeriod,
                                      )
                                      (implicit val ec: ExecutionContext)
  extends BaseController with LazyLogging {

  implicit val defaultTimeout: Timeout = Timeout(60.seconds)
  val language = configuration.get[String]("language")

  def index = Action {
    Ok(indexTemplate(language))
  }

  def workType = Action {
    Ok(workTypeTemplate(language))
  }

  def doctorType = Action {
    Ok(doctorTypeTemplate(language))
  }

  def patient = Action {
    Ok(registrationPatient(language))
  }

  def patient_dashboard = Action {
    Ok(dashboard_patient(language))
  }

  def organization = Action {
    Ok(registrationOrganization(language))
  }


  def addLaboratory: Action[JsValue] = Action.async(parse.json) { implicit request =>
    val laboratoryName = (request.body \ "laboratoryName").as[String]
    logger.warn(s"controllerga keldi")
    (registrationManager ? AddLaboratory(Laboratory(None, laboratoryName))).mapTo[Int].map { id =>
      Ok(Json.toJson(id))
    }
  }

  def getLaboratoryName: Action[AnyContent] = Action.async {
    (registrationManager ? GetLaboratoryList).mapTo[Seq[Laboratory]].map { laboratoryName =>
      Ok(Json.toJson(laboratoryName))
    }
  }

  def deleteLaboratory = Action.async(parse.json) { implicit request =>
    val id = (request.body \ "id").as[Int]
    (registrationManager ? DeleteLaboratory(id)).mapTo[Int].map { i =>
      if (i != 0) {
        Ok(Json.toJson(id + " raqamli foydalanuvchi o`chirildi"))
      }
      else {
        Ok("Bunday raqamli foydalanuvchi topilmadi")
      }
    }
  }

  def updateLaboratory = Action.async(parse.json) { implicit request =>
    val id = (request.body \ "id").as[Int]
    val laboratoryName = (request.body \ "laboratoryName").as[String]
    (registrationManager ? UpdateLaboratory(Laboratory(Some(id), laboratoryName))).mapTo[Int].map { i =>
      if (i != 0) {
        Ok(Json.toJson(id + " raqamli foydalanuvchi yangilandi"))
      }
      else {
        Ok("Bunday raqamli foydalanuvchi topilmadi")
      }
    }
  }

  def addOrganization: Action[JsValue] = Action.async(parse.json) { implicit request =>
    val organizationName = (request.body \ "organizationName").as[String]
    logger.warn(s"controllerga keldi")
    (registrationManager ? AddOrganization(Organization(None, organizationName))).mapTo[Int].map { id =>
      Ok(Json.toJson(id))
    }
  }

  def getOrganizationName: Action[AnyContent] = Action.async {
    (registrationManager ? GetOrganizationList).mapTo[Seq[Organization]].map { organizationName =>
      Ok(Json.toJson(organizationName))
    }
  }

  def deleteOrganization = Action.async(parse.json) { implicit request =>
    val id = (request.body \ "id").as[Int]
    (registrationManager ? DeleteOrganization(id)).mapTo[Int].map { i =>
      if (i != 0) {
        Ok(Json.toJson(id + " raqamli foydalanuvchi o`chirildi"))
      }
      else {
        Ok("Bunday raqamli foydalanuvchi topilmadi")
      }
    }
  }

  def updateOrganization = Action.async(parse.json) { implicit request =>
    val id = (request.body \ "id").as[Int]
    val organizationName = (request.body \ "organizationName").as[String]
    (registrationManager ? UpdateOrganization(Organization(Some(id), organizationName))).mapTo[Int].map { i =>
      if (i != 0) {
        Ok(Json.toJson(id + " raqamli foydalanuvchi yangilandi"))
      }
      else {
        Ok("Bunday raqamli foydalanuvchi topilmadi")
      }
    }
  }

  def addDoctorType: Action[JsValue] = Action.async(parse.json) { implicit request => {
    val doctorTypeName = (request.body \ "doctorType").as[String]
    logger.warn(s"controllerga keldi")
    (registrationManager ? AddDoctorType(DoctorType(None, doctorTypeName))).mapTo[Int].map { id =>
      Ok(Json.toJson(id))
    }
  }
  }

  def getDoctorTypeName: Action[AnyContent] = Action.async {
    (registrationManager ? GetDoctorTypeList).mapTo[Seq[DoctorType]].map { doctorTypeName =>
      Ok(Json.toJson(doctorTypeName))
    }
  }

  def deleteDoctorType = Action.async(parse.json) { implicit request =>
    val id = (request.body \ "id").as[Int]
    (registrationManager ? DeleteDoctorType(id)).mapTo[Int].map { i =>
      if (i != 0) {
        Ok(Json.toJson(id + " raqamli foydalanuvchi o`chirildi"))
      } else {
        Ok("Bunday raqamli foydalanuvchi topilmadi")
      }
    }
  }

  def updateDoctorType = Action.async(parse.json) { implicit request =>
    val id = (request.body \ "id").as[Int]
    val doctorTypeName = (request.body \ "doctorTypeName").as[String]
    (registrationManager ? UpdateDoctorType(DoctorType(Some(id), doctorTypeName))).mapTo[Int].map { i =>
      if (i != 0) {
        Ok(Json.toJson(id + " raqamli foydalanuvchi yangilandi"))
      } else {
        Ok("Bunday raqamli foydalanuvchi topilmadi")
      }
    }
  }

  def addCheckupPeriod: Action[JsValue] = Action.async(parse.json) { implicit request => {
    val numberPerYear = (request.body \ "numberPerYear").as[String].toInt
    val doctorType = (request.body \ "doctorType").as[Array[Int]]
    val labType = (request.body \ "labType").as[Array[Int]]
    val workType = (request.body \ "workType").as[String]
    val result = (for {
      _ <- (registrationManager ? AddWorkType(WorkType(None, workType))).mapTo[Int]
      workTypeId <- (registrationManager ? FindWorkTypeIdByWorkType(workType)).mapTo[Option[Int]]
      result <- (registrationManager ? AddCheckupPeriod(CheckupPeriod(None, numberPerYear, Json.toJson(doctorType), Json.toJson(labType), workTypeId.head))).mapTo[Int]
    } yield result)
    result.map { a =>
      Ok(Json.toJson("OK"))
    }
  }
  }


  def createPatient(): Action[MultipartFormData[TemporaryFile]] = Action.async(parse.multipartFormData) { implicit request: Request[MultipartFormData[TemporaryFile]] => {
    val body = request.body.asFormUrlEncoded
    val firstName = body("firstName").head
    val middleName = body("middleName").head
    val lastName = body("lastName").head
    val passport_sn = body("passport_sn").headOption
    val birthday = parseDate(body("birthday").head)
    val phone = body("phone").headOption
    val address = body("address").head
    val gender = body("gender").head.toInt
    val checkupType = body("checkupType").head
    val organizationId = body("organizationId").headOption match {
      case Some(id) => Some(id.toInt)
      case None => None
    }
    val workerTypeId = body("workerTypeId").headOption match {
      case Some(id) => Some(id.toInt)
      case None => None
    }
    val cardNumber = body("cardNumber").head
    val profession = body("profession").headOption
    request.body.file("attachedFile").map { temp =>
      val fileName = filenameGenerator()
      val imgData = getBytesFromPath(temp.ref.path)
      val result = (for {
        _ <- (registrationManager ? AddImage(fileName, imgData)).mapTo[Unit]
        result <- (registrationManager ? CreatePatient(Patient(None, firstName, middleName, lastName,
          passport_sn, gender, birthday.get, address, phone, cardNumber, profession,
          workerTypeId, new Date, Some(fileName), organizationId))).mapTo[Int]
      } yield result)
      result.map { a =>
        Ok("OK")
      }
    }.getOrElse {
      (registrationManager ? CreatePatient(Patient(None, firstName, middleName, lastName, passport_sn, gender, birthday.get, address, phone, cardNumber, profession, workerTypeId, new Date, organizationId = organizationId))).mapTo[Int].map { pr =>
        Ok("OK")
      }
    }
  }
  }

  def getPatientList: Action[AnyContent] = Action.async {
    (registrationManager ? GetPatient).mapTo[Seq[Patient]].map { p =>
      Ok(Json.toJson(p))
    }
  }

  def deletePatient(): Action[JsValue] = Action.async(parse.json) { implicit request => {
    val id = (request.body \ "id").as[Int]
    (registrationManager ? DeletePatient(Some(id))).mapTo[Int].map { bool =>
      if (bool == 1) {
        Ok(Json.toJson(s"$id reqamli bemor o'chirildi"))
      } else {
        Ok(Json.toJson(s"Bunday raqamli bemor mavjud emas!"))
      }
    }
  }
  }

  def getRegionList = Action {
    Ok(Json.toJson(regionList))
  }

  private def filenameGenerator() = {
    new Date().getTime.toString + ".png"
  }

  private def getBytesFromPath(filePath: Path): Array[Byte] = {
    Files.readAllBytes(filePath)
  }

  def parseDate(dateStr: String) = {
    val dateFormat = new SimpleDateFormat("yyyy-MM-dd")
    util.Try(dateFormat.parse(URLDecoder.decode(dateStr, "UTF-8"))).toOption
  }

}

