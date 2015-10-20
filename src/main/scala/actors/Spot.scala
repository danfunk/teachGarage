package actors

import akka.actor._
import akka.actor.Actor.Receive

import scala.util.Random

case object ReserveMsg
case class ReserveMsg(id: String)
case class isReservedMsg(id: String)

/**
 * A "spot" is a place to park a car.
 * It has three states:  Free, Reserved, Occupied
  * Maybe this should be a finite state machine?
 */
class Spot() extends Actor {

  private var reservation :String  = ""
  private var reserved    :Boolean = false
  private val randomGenerator = new Random

  override def receive: Receive = {
    case ReserveMsg => {
      reservation = randomGenerator.nextString(20)
      reserved    = true
      sender ! reservation
    }
  }
}
