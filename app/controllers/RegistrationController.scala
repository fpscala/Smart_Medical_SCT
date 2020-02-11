package controllers

import java.net.URLDecoder
import java.nio.file.{Files, Path}
import java.text.SimpleDateFormat
import java.util.Date

import akka.pattern.ask
import akka.actor.ActorRef
import akka.util.Timeout
import akka.pattern.ask
import com.typesafe.scalalogging.LazyLogging
import javax.inject._
import org.webjars.play.WebJarsUtil
import play.api.libs.Files.TemporaryFile
import play.api.libs.json.{JsValue, Json, __}
import play.api.mvc.{Action, _}
import protocols.RegistrationProtocol._
import views.html._
import views.html.patient._
import views.html.settings._

import scala.concurrent.{ExecutionContext, Future}
import scala.concurrent.duration.DurationInt
import scala.util.Try

@Singleton
class RegistrationController @Inject()(val controllerComponents: ControllerComponents,
                                       implicit val webJarsUtil: WebJarsUtil,
                                       @Named("registration-manager") val registrationManager: ActorRef,
                                       @Named("registration-manager") val organizationManager: ActorRef,
                                       indexTemplate: index,
                                       registrationPatient: registration_patient,
                                       dashboard_patient: dashboard,
                                       registrationOrganization: organization,
                                       registrationLaboratory: laboratory,
                                       checkupTemplate: checkupPeriod,
                                      )
                                      (implicit val ec: ExecutionContext)
  extends BaseController with LazyLogging {

  implicit val defaultTimeout: Timeout = Timeout(60.seconds)

  def index = Action {
    Ok(indexTemplate())
  }

  def checkupPeriod = Action {
    Ok(checkupTemplate())
  }

  def patient = Action {
    Ok(registrationPatient())
  }

  def patient_dashboard = Action {
    Ok(dashboard_patient())
  }

  def organization = Action {
    Ok(registrationOrganization())
  }

  def laboratory = Action {
    Ok(registrationLaboratory())
  }

  def addLaboratory: Action[JsValue] = Action.async(parse.json) { implicit request =>
    val laboratoryName = (request.body \ "laboratoryName").as[String]
    logger.warn(s"controllerga keldi")
    (registrationManager ? AddLaboratory(Laboratory(None, laboratoryName))).mapTo[Int].map { id =>
      Ok(Json.toJson(id))
    }
  }

  def addOrganization: Action[JsValue] = Action.async(parse.json) { implicit request =>
    val organizationName = (request.body \ "organizationName").as[String]
    logger.warn(s"controllerga keldi")
    (organizationManager ? AddOrganization(Organization(None, organizationName))).mapTo[Int].map { id =>
      Ok(Json.toJson(id))
    }
  }

  def getOrganizationName: Action[AnyContent] = Action.async {
    (organizationManager ? GetOrganizationList).mapTo[Seq[Organization]].map { organizationName =>
      Ok(Json.toJson(organizationName))
    }
  }

  def deleteOrganization = Action.async(parse.json) { implicit request =>
    val id = (request.body \ "id").as[Int]
    (organizationManager ? DeleteOrganization(id)).mapTo[Int].map { i =>
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
    (organizationManager ? UpdateOrganization(Organization(Some(id), organizationName))).mapTo[Int].map { i =>
      if (i != 0) {
        Ok(Json.toJson(id + " raqamli foydalanuvchi yangilandi"))
      }
      else {
        Ok("Bunday raqamli foydalanuvchi topilmadi")
      }
    }
  }

  def addCheckupPeriod: Action[JsValue] = Action.async(parse.json) { implicit request =>
    val numberPerYear = (request.body \ "numberPerYear").as[Int]
    val doctorTypeId = (request.body \ "doctorTypeId").as[JsValue]
    val labTypeId = (request.body \ "labTypeId").as[JsValue]
    val workTypeId = (request.body \ "workTypeId").as[Int]
    logger.warn(s"controllerga keldi")
    (registrationManager ? AddCheckupPeriod(CheckupPeriod(None, numberPerYear, doctorTypeId, labTypeId, workTypeId))).mapTo[Int].map { id =>
      Ok(Json.toJson(id))
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

  private def filenameGenerator() = {
    new Date().getTime.toString + ".png"
  }

  private def getBytesFromPath(filePath: Path): Array[Byte] = {
    Files.readAllBytes(filePath)
  }

  def parseDate(dateStr: String) = {
    val dateFormat = new SimpleDateFormat("yyyy-MM-dd")
    logger.warn(s"dateF: $dateStr")
    util.Try(dateFormat.parse(URLDecoder.decode(dateStr, "UTF-8"))).toOption
  }

}

