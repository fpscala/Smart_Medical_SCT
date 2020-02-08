package controllers

import akka.pattern.ask
import akka.actor.ActorRef
import akka.util.Timeout
import akka.pattern.ask
import com.typesafe.scalalogging.LazyLogging
import javax.inject._
import org.webjars.play.WebJarsUtil
import play.api.libs.json.{JsValue, Json}
import play.api.mvc._
import protocols.RegistrationProtocol.{AddOrganization, DeleteOrganization, GetOrganizationList, Organization, UpdateOrganization}
import views.html._
import views.html.patient._

import scala.concurrent.ExecutionContext
import scala.concurrent.duration.DurationInt

@Singleton
class RegistrationController @Inject()(val controllerComponents: ControllerComponents,
                                       implicit val webJarsUtil: WebJarsUtil,
                                       @Named("registration-manager") val registrationManager: ActorRef,
                                       @Named("registration-manager") val organizationManager: ActorRef,
                                       indexTemplate: index,
                                       registrationPatient: registration_patient,
                                       registrationOrganization: organization,
                                      )
                                      (implicit val ec: ExecutionContext)
  extends BaseController with LazyLogging {

  implicit val defaultTimeout: Timeout = Timeout(60.seconds)

  def index = Action {
    Ok(indexTemplate())
  }

  def patient = Action {
    Ok(registrationPatient())
  }
  def organization = Action {
    Ok(registrationOrganization())
  }


  def addOrganization: Action[JsValue] = Action.async(parse.json) { implicit request =>
    val organizationName = (request.body \ "organizationName").as[String]
    logger.warn(s"controllerga keldi")
    (organizationManager ? AddOrganization(Organization(None, organizationName ))).mapTo[Int].map { id =>
      Ok(Json.toJson(id))
    }
  }

  def getOrganizationName: Action[AnyContent] = Action.async {
    (organizationManager ? GetOrganizationList).mapTo[Seq[Organization]].map{ organizationName =>
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



}

