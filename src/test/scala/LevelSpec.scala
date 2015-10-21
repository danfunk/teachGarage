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
 * Basic testing of the LevelSpec
 */
class LevelSpec extends FlatSpec with Matchers {
  implicit val system = ActorSystem("MyActorSystem", ConfigFactory.load())
  implicit val timeout = Timeout(5 seconds)

  // We need a levelRef in all these tests, so consolidating this code.
  trait LevelRef {
    val levelRef = TestActorRef(new LevelActor(5))
    val level = levelRef.underlyingActor
  }

  /**
   * Going to assume that if one passthrough works, others will work too, so not
   * testing everything already tested in SpotTest
   */
  "A Level" should "allow reserving of spots, returning a 20 character string" in new LevelRef {
    val future = levelRef ? LevelActor.ReserveMsg(1)
    Await.result(future, Duration.Inf)  // Had to add this or I would get an exception.
    val Success(result: String) = future.value.get
    result.length should equal(20)
  }

  "A Level" should "return all of its spaces" in new LevelRef {
    val future = levelRef ? LevelActor.ListSpotsMsg
    Await.result(future, Duration.Inf)  // Had to add this or I would get an exception.
    val Success(result: List[Spot]) = future.value.get
    result.length should equal(5)
  }

}
