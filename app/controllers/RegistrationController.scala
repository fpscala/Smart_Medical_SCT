package controllers

import akka.actor.ActorRef
import akka.util.Timeout
import akka.pattern.ask
import com.typesafe.scalalogging.LazyLogging
import javax.inject._
import org.webjars.play.WebJarsUtil
import play.api.mvc._
import views.html._
import views.html.patient._

import scala.concurrent.ExecutionContext
import scala.concurrent.duration.DurationInt

@Singleton
class RegistrationController @Inject()(val controllerComponents: ControllerComponents,
                                       implicit val webJarsUtil: WebJarsUtil,
                                       @Named("registration-manager") val registrationManager: ActorRef,
                                       indexTemplate: index,
                                       registrationPatient: registration_patient,
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

}
