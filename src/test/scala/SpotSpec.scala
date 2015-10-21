import actors.{Spot, SpotActor}
import akka.actor.ActorSystem
import akka.testkit.TestActorRef
import akka.util.Timeout
import com.typesafe.config.ConfigFactory
import org.scalatest.{Matchers, FlatSpec}
import akka.pattern.ask
import scala.concurrent.duration._
import scala.util.Success

/**
 * Simple TestActor
 */
class SpotSpec extends FlatSpec with Matchers {
  implicit val system = ActorSystem("MyActorSystem", ConfigFactory.load())
  implicit val timeout = Timeout(5 seconds)

  "A Spot" should "be reservable, returning a random 20 character string" in {
    val spotRef = TestActorRef(new SpotActor(1))
    val spot = spotRef.underlyingActor

    val future = spotRef ? SpotActor.ReserveMsg
    val Success(result: String) = future.value.get
    result.length should equal(20)
  }

  "A Spot" should "be occupyable." in {
    val spotRef = TestActorRef(new SpotActor(1))
    val spot = spotRef.underlyingActor

    val future = spotRef ? SpotActor.OccupyMsg
    val Success(true) = future.value.get
  }

  "A Spot Actor" should "return a case class representing it's current state." in {
    val spotRef = TestActorRef(new SpotActor(1))
    val spot = spotRef.underlyingActor

    val future = spotRef ? SpotActor.RetrieveMsg
    val Success(result: Spot) = future.value.get
    result.id should equal(1)
    result.occupied should equal(false)
    result.reserved should equal(false)
  }

}
