import actors._
import akka.actor.ActorSystem
import akka.pattern.ask
import akka.testkit.TestActorRef
import akka.util.Timeout
import com.typesafe.config.ConfigFactory
import org.scalatest.{FlatSpec, Matchers}

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.util.Success

/**
 * Simple TestActor
 */
class LevelSpec extends FlatSpec with Matchers {
  implicit val system = ActorSystem("MyActorSystem", ConfigFactory.load())
  implicit val timeout = Timeout(5 seconds)

  "A Level" should "return all of its spaces" in {
    val levelRef = TestActorRef(new LevelActor(5))
    val level = levelRef.underlyingActor

    val future = levelRef ? LevelActor.ListSpotsMsg
    Await.result(future, Duration.Inf)  // Had to add this or I would get an exception.
    val Success(result: List[Spot]) = future.value.get
    result.length should equal(5)
  }

}
