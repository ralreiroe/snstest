package example

import java.net.URI
import java.util.concurrent.{TimeUnit, TimeoutException}

import akka.Done
import akka.actor.{ActorSystem}
import akka.stream.alpakka.sns.scaladsl.SnsPublisher
import akka.stream.scaladsl.{Flow, Keep, Sink, Source}
import akka.stream.{ActorMaterializer, Materializer}
import com.github.matsluni.akkahttpspi.AkkaHttpClient
import software.amazon.awssdk.services.sns.SnsAsyncClient
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.sns.model.PublishResponse

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}
import scala.concurrent.duration._

/**
 * Test for alpakka's SnsPublisher, a factory of akka streams Flows/Sinks to publish messages to an SNS topic
 */
object SNSTest extends App {

  implicit val system: ActorSystem = ActorSystem()
  implicit val mat: Materializer = ActorMaterializer()


  val credentialsProvider = StaticCredentialsProvider.create(AwsBasicCredentials.create("x", "x"))
  implicit val awsSnsClient: SnsAsyncClient =
    SnsAsyncClient
      .builder()
      .credentialsProvider(credentialsProvider)
      .region(Region.EU_WEST_1)
      .endpointOverride(URI.create("http://localhost:4575"))
      .httpClient(AkkaHttpClient.builder().withActorSystem(system).build())
      // Possibility to configure the retry policy
      // see https://doc.akka.io/docs/alpakka/current/aws-shared-configuration.html
      // .overrideConfiguration(...)
      .build()

  system.registerOnTermination(awsSnsClient.close())


  val topicArn = "arn:aws:sns:eu-west-1:000000000000:order-returned-topic"

  private val eventualDone: Future[Done] = {
    Source
    .single("message 12345")      //Source[String, Notused]
    .via(SnsPublisher.flow(topicArn))       //Flow[String, PublishResponse, NotUsed]
//    .runWith(Sink.foreach(publishResponse => println(publishResponse.messageId())))
      .runWith(Flow[PublishResponse].map(publishResponse => println(publishResponse.messageId())).toMat(Sink.ignore)(Keep.right))
  }


  Await.ready(eventualDone, Duration.apply(1, TimeUnit.MINUTES))

  val duration = 10.seconds

  println("terminate actorsystem")
  system.terminate()
  try Await.ready(system.whenTerminated, duration)
  catch {
    case _: TimeoutException =>
      val msg = "Failed to stop [%s] within [%s]".format(
        system.name,
        duration)
      println(msg)
  }





}

