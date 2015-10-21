package actors

import akka.actor._
import scala.util.Random

case class Spot(id:Int, reserved:Boolean, occupied:Boolean)

object SpotActor {
  case object ReserveMsg
  case object OccupyMsg
  case object RetrieveMsg

  def props(id: Int): Props = Props(new SpotActor(id))
}

/**
 * A "spot" is a place to park a car.
 * It has three states:  Free, Reserved, Occupied
  * Maybe this should be a finite state machine?
 */
class SpotActor(id:Int) extends Actor {

  private var reservation :String  = ""
  private var reserved    :Boolean = false
  private var occupied    :Boolean = false

  override def receive: Receive = {
    case SpotActor.ReserveMsg => {
      reservation = Random.nextString(20)
      reserved    = true
      occupied    = false
      sender ! reservation
    }
    case SpotActor.OccupyMsg => {
      occupied    = true
      sender ! true
    }
    case SpotActor.RetrieveMsg => {
        sender ! Spot(id, reserved, occupied)
    }

  }
}
