package actors

import akka.actor._
import akka.actor.Actor.Receive

/**
 * A parking lot can have many levels.
 */
class Level extends Actor {
  override def receive: Receive = ???
}
