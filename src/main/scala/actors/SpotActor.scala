package actors

import akka.actor._
import scala.util.Random

/**
 * The state of a Spot
 */
sealed trait SpotState
case object Free      extends SpotState
case object Reserved  extends SpotState
case object Occupied  extends SpotState

/**
 * Possible exceptions
 * @param mesg
 */
case class SpotNotAvailableException(mesg: String) extends Exception

/**
 * The data model we use to wrap up and return information about the Spot.
 */
case class Spot(id:Int, state: SpotState)

/**
 * Companion object to the Actor, defines messages that can be received.
 */
object SpotActor {
  case object ReserveMsg
  case class OccupyMsg(reservation: String)
  case object RetrieveMsg
  case object ReleaseMsg

  // props Pattern that makes it a bit cleaner and safer to construct actors
  def props(id: Int): Props = Props(new SpotActor(id))
}

/**
 * A "spot" is a place to park a car.
 * It has three states:  Free, Reserved, Occupied
 */
class SpotActor(id:Int) extends Actor {

  //FIXME: should this be a Finite State Machine?
  private var reservation: String = ""
  private var reserved: Boolean = false
  private var occupied: Boolean = false

  private def state(): SpotState = {
    if (occupied) Occupied
    else if (reserved) Reserved
    else Free
  }

  override def receive: Receive = {
    case SpotActor.ReserveMsg => {
      println("Received Reserve Message on Spot #" + id)
      if (state == Free) {
        reservation = Random.nextString(20)
        reserved = true
        occupied = false
        println("All good, returning reservation '" + reservation + "' string to Sender: " + sender().toString())
        sender ! reservation
      } else {
        throw SpotNotAvailableException("This Spot is already reserved or occupied.")
      }
    }
    case SpotActor.OccupyMsg(key: String) => {
      def occupy(): Unit = {
        this.occupied = true
      }

      state match {
        case Free => occupy(); sender ! true
        case Reserved if key == reservation => occupy(); sender ! true
        case Reserved => throw SpotNotAvailableException("This space is reserved and you don't have a valid key.")
        case _ => throw SpotNotAvailableException("This spot is not available.")
      }
    }

    case SpotActor.RetrieveMsg => {
      sender ! Spot(id, state)
    }

    case SpotActor.ReleaseMsg => {
      this.reserved = false
      this.occupied = false
      this.reservation = ""
      sender ! true
    }

  }
}
