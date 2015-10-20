import actors.{Spot, ReserveMsg}
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
    val spotRef = TestActorRef(new Spot)
    val spot = spotRef.underlyingActor

    val future = spotRef ? ReserveMsg
    val Success(result: String) = future.value.get
    result.length should equal(20)
  }


}
