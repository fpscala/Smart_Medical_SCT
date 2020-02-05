package actors

import akka.actor.Actor
import akka.util.Timeout
import com.typesafe.scalalogging.LazyLogging
import javax.inject.Inject
import play.api.{Configuration, Environment}

import scala.concurrent.ExecutionContext
import scala.concurrent.duration.DurationInt

class RegistrationManager @Inject()(val environment: Environment,
                                    val configuration: Configuration,
                                   )
                                   (implicit val ec: ExecutionContext)
  extends Actor with LazyLogging {

  implicit val defaultTimeout: Timeout = Timeout(60.seconds)

  def receive = {


    case _ => logger.info(s"received unknown message")
  }


}
