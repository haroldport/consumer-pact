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
        .between("consumer-pact")
        .and("provider-pact")
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

    it("Should be able to create a contract with a simple body matcher for the request and response") {

      val endPoint = "/questions"

      val json: String = {
        s"""
           |{
           |  "id" : "q1",
           |  "title" : "question1",
           |  "text" : "question1",
           |}
        """.stripMargin
      }

      forgePact
        .between("consumer-pact")
        .and("provider-pact")
        .addInteraction(
          interaction
            .description("a simple post example with body matchers")
            .uponReceiving(
              method = POST,
              path = endPoint,
              query = None,
              headers = Map.empty,
              body = json,
              matchingRules =
                bodyRegexRule("id", "\\w+")
                  ~> bodyTypeRule("title")
            )
            .willRespondWith(
              status = 200,
              headers = Map.empty,
              body = "q1",
              matchingRules = None
            )
        )
        .runConsumerTest { mockConfig =>

          val result = ProviderClient.doPostRequest(mockConfig.baseUrl, endPoint, Map.empty, json)

          result.status should equal(200)
          result.body should equal("q1")

        }

    }

  }



}
