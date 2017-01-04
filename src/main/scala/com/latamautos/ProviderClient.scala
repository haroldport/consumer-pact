package com.latamautos

/**
  * Created by Harold on 25/11/16.
  */
import org.json4s.DefaultFormats
import org.json4s.native.JsonParser._

import scalaj.http.{Http, HttpResponse, HttpRequest}

object ProviderClient {

  implicit def convertHeaders(headers: Map[String, IndexedSeq[String]]): Map[String, String] =
    headers.map { h => (h._1, h._2.headOption.getOrElse("")) }

  def doGetRequest(baseUrl: String, endPoint: String, headers: Map[String, String]): SimpleResponse = {
    val request = Http(baseUrl + endPoint).headers(headers)

    doRequest(request)
  }

  def doPostRequest(baseUrl: String, endPoint: String, headers: Map[String, String], body: String): SimpleResponse = {
    val request = Http(baseUrl + endPoint)
      .headers(headers)
      .postData(body)

    doRequest(request)
  }

  def doRequest(request: HttpRequest): SimpleResponse = {
    try {
      val response = request.asString
      SimpleResponse(response.code, response.headers, response.body)
    } catch {
      case e: Throwable =>
        SimpleResponse(500, Map(), e.getMessage)
    }
  }

  case class SimpleResponse(status: Int, headers: Map[String, String], body: String)

  private implicit val formats = DefaultFormats

  def fetchResults(baseUrl: String): Option[Question] = {
    println(baseUrl + "/test")
    Http(baseUrl + "/test").asString match {
      case r: HttpResponse[String] if r.is2xx =>
        parse(r.body).extractOpt[Question]
      case _ =>
        None
    }
  }

  def fetchAuthToken(host: String, port: Int, name: String): Option[Token] = {
    Http("http://" + host + ":" + port + "/auth_token?name=" + name)
      .headers(("Accept", "application/json"), ("Name", name))
      .asString match {
      case r: HttpResponse[String] if r.is2xx =>
        println(">> " + r)
        parse(r.body).extractOpt[Token]

      case r: HttpResponse[String] =>
        println("<< " + r)
        None
    }
  }

}

case class Question(id: String, title: String, text: String)

case class Token(token: String)