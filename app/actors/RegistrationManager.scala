package actors

import akka.actor.Actor
import akka.pattern.pipe
import akka.util.Timeout
import com.typesafe.scalalogging.LazyLogging
import javax.inject.Inject
import play.api.{Configuration, Environment}
import protocols.RegistrationProtocol._
import akka.actor.{Actor, ActorLogging}

import scala.concurrent.ExecutionContext
import scala.concurrent.duration.DurationInt

class RegistrationManager @Inject()(val environment: Environment,
                                    val configuration: Configuration,
                                   )
                                   (implicit val ec: ExecutionContext)
  extends Actor with LazyLogging {

  implicit val defaultTimeout: Timeout = Timeout(60.seconds)

  def receive = {
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

}
