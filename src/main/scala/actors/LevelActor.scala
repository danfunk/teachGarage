package actors

import akka.actor._

/**
 * The data model we use to wrap up and return information about the Level.
 */
case class Level(id:Int, spots: List[Spot])

/**
 * Companion object to the Actor, defines messages that can be received.
 */
object LevelActor {
  case object ListSpotsMsg
  case class  ReserveMsg(id:Int)
  case class  OccupyMsg(id:Int, reservation: String)
  case class  ReleaseMsg(id:Int)

  // props Pattern that makes it a bit cleaner and safer to construct actors
  def props(id: Int): Props = Props(new SpotActor(id))
}

/**
 * Possible exceptions
 * @param mesg
 */
case class NoSuchSpotException(mesg: String) extends Exception



/**
 * A parking lot can have many levels, each level is made up of a series
 * of spaces or "Spots".  A Level mainly just rolls up it's spots.
 */
class LevelActor(spaces:Int) extends Actor {

  // Create SpotActors for every spot in the parking lot.
  private val spotsRefs : Map[Int,ActorRef] =
     List.range(1, spaces + 1) map (i => i -> context.actorOf(SpotActor.props(i))) toMap
  private var running = false
  private var spotList :List[Spot] = Nil
  private var listSpotsSender: Option[ActorRef] = None

  /**
   * Sends a message to the requested Spot, relaying whatever comes back.
   */
  private def sendMessageToSpot(sender:ActorRef, id: Int, message: Any): Any = {
    println("\n\nSending " +  message.getClass + " to Spot #" + id)
    spotsRefs get id match {
      case Some(spotRef) =>
        spotRef.forward(message)
      case None => throw NoSuchSpotException("No spot exists on this level with id " + id)
    }
  }

  override def receive: Receive = {
    case LevelActor.ReserveMsg(id:Int) => sendMessageToSpot(sender(), id, SpotActor.ReserveMsg)
    case LevelActor.OccupyMsg(id:Int, key: String) => sendMessageToSpot(sender(),id, SpotActor.OccupyMsg(key))
    case LevelActor.ReleaseMsg(id:Int) => sendMessageToSpot(sender(), id, SpotActor.ReleaseMsg)
    case LevelActor.ListSpotsMsg =>
      if (running) {
        println("Duplicate message received.")
      } else {
        listSpotsSender = Some(sender)
        // Convert spotActors to Spot classes
        for (spotRef <- spotsRefs.values) spotRef ! SpotActor.RetrieveMsg
      }
    // For accepting spot objects back from the spot actors.
    case s:Spot => {
      spotList = s :: spotList
      println("Received a spot, we now have " + spotList.size)
      // If we receive all the spots back, send them to the caller.
      if(spotList.size == spotsRefs.size) {
          println("Sending back: " + spotList)
          listSpotsSender.map(_ ! spotList)
          running = false
          spotList = Nil
      }
    }
  }
}
