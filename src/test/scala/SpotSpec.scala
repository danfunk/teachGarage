import actors.{SpotNotAvailableException, Free, Spot, SpotActor}
import akka.actor.ActorSystem
import akka.testkit.TestActorRef
import akka.util.Timeout
import com.typesafe.config.ConfigFactory
import org.scalatest.{Matchers, FlatSpec}
import akka.pattern.ask
import scala.concurrent.duration._
import scala.util.{Failure, Success}

/**
 * This unit test asserts the basic functions of the Spot.
 */
class SpotSpec extends FlatSpec with Matchers {
  implicit val system = ActorSystem("MyActorSystem", ConfigFactory.load())
  implicit val timeout = Timeout(5 seconds)

  // We need a spotRef in all these tests, so consolidating this code.
  trait SpotRef {
    val spotRef  = TestActorRef(new SpotActor(1))
    val spot = spotRef.underlyingActor
  }

  "A Spot" should "be reservable, returning a random 20 character string" in new SpotRef {
    val future = spotRef ? SpotActor.ReserveMsg
    val Success(result: String) = future.value.get
    result.length should equal(20)
  }

  "A reserved spot " should "not be reserved again." in  new SpotRef {
    // Reserve a spot.
    val future = spotRef ? SpotActor.ReserveMsg
    val Success(result: String) = future.value.get
    result.length should equal(20)

    // Reserve the same spot again, should result in an error
    intercept[SpotNotAvailableException] { spotRef.receive(SpotActor.ReserveMsg) }
  }

  "A Spot" should "be occupy-able, if it isn't reserved" in  new SpotRef {
    val future = spotRef ? SpotActor.OccupyMsg("")
    val Success(true) = future.value.get
  }

  "A Reserved Spot" should "not be occupy-able if you don't have the key" in  new SpotRef {
    // Reserve a spot.
    val future = spotRef ? SpotActor.ReserveMsg
    val Success(result: String) = future.value.get

    // Occupy a spot without a key
    // Reserve the same spot again, should result in an erro
    intercept[SpotNotAvailableException] { spotRef.receive(SpotActor.OccupyMsg("")) }

    // Occupy a spot with a valid key
    // Reserve the same spot again, should result in an erro
    val future2 = spotRef ? SpotActor.OccupyMsg(result)
    val Success(true) = future2.value.get
  }

  "A reserved spot" should "be available after being released." in new SpotRef {
    // Release the spot
    val future = spotRef ? SpotActor.ReleaseMsg
    val Success(true) = future.value.get

    // Reserve the spot
    val future2 = spotRef ? SpotActor.ReserveMsg
    val Success(result:String) = future2.value.get
  }

  "A Spot Actor" should "return a case class representing it's current state." in  new SpotRef {
    val future = spotRef ? SpotActor.RetrieveMsg
    val Success(result: Spot) = future.value.get
    result.id should equal(1)
    result.state should equal(Free)
  }

}
