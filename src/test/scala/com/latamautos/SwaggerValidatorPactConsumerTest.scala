package com.latamautos

import com.itv.scalapact.ScalaPactForger
import org.json4s.DefaultFormats
import org.scalatest.{FunSpec, Matchers}
import org.json4s.native.Serialization._

/**
  * Created by Harold on 23/11/16.
  */
class SwaggerValidatorPactConsumerTest extends FunSpec with Matchers {

  import ScalaPactForger._
  implicit val formats = DefaultFormats

  describe("Connecting to the Provider service") {
    it("should be able to get an auth token") {
      forgePact
        .between("microservice-c1")
        .and("microservice-p1")
        .addInteraction(
          interaction
            .description("Fetching least secure auth token ever")
            .uponReceiving(
              method = GET,
              path = "/questions/test",
              query = None,
              headers = Map("Accept" -> "application/json"),
              body = None,
              None
            )
            .willRespondWith(
              status = 200,
              headers = Map("Content-Type" -> "application/json; charset=UTF-8"),
              body = ""
            )
        )
        .runConsumerTest { mockConfig =>
          //          val token = ProviderClient.fetchAuthToken(mockConfig.host, mockConfig.port, "Sally")
          //
          //          token.isDefined shouldEqual true
          //          token.get.token shouldEqual "abcABC123"
        }
    }

    it("should be able to fetch results"){

      val question = Question("test", "MyTitle1", "The text of my question 1")

      val body = write(
        question
      )

      forgePact
        .between("Consumer")
        .and("Provider")
        .addInteraction(
          interaction
            .description("Fetching results")
            .given("Results: Bob, Fred, Harry")
            .uponReceiving("/questions/test")
            .willRespondWith(200, body)
        )
        .runConsumerTest { mockConfig =>

          val results = ProviderClient.fetchResults(mockConfig.baseUrl)

          results.isDefined shouldEqual true
          results.get.id shouldEqual "test"
          results.get.title shouldEqual "MyTitle1"
          results.get.text shouldEqual "The text of my question 1"

        }

    }

  }



}
