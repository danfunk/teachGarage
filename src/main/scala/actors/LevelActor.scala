package actors

import akka.actor._

object LevelActor {
  case object ListSpotsMsg
}

/**
 * A parking lot can have many levels, each level is made up of a series
 * of spaces.
 */
class LevelActor(spaces:Int) extends Actor {

  // Create SpotActors for every spot in the parking lot.
  val spotsRefs : List[ActorRef] =
     List.range(1, spaces + 1) map (i => context.actorOf(SpotActor.props(i)))

  var running = false
  var spotList :List[Spot] = Nil
  var listSpotsSender: Option[ActorRef] = None

  override def receive: Receive = {
    case LevelActor.ListSpotsMsg =>
      if (running) {
        println("Duplicate message received.")
      } else {
        listSpotsSender = Some(sender)
        // Convert spotActors to Spot classes
        for (spotRef <- spotsRefs) spotRef ! SpotActor.RetrieveMsg
      }
    case s:Spot => {
      spotList = s :: spotList
      println("Received a spot, we now have " + spotList.size)
      // If we receive all the spots back, send them to the caller.
      if(spotList.size == spotsRefs.size) {
          println("Sending back: " + spotList)
          listSpotsSender.map(_ ! spotList)
          running = false
      }
    }
  }
}
