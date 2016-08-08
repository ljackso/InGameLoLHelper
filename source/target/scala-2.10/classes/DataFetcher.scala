import scala.concurrent.{Future}
import scala.concurrent.ExecutionContext.Implicits.global
import argonaut._, Argonaut._
import dispatch._
/**
  * Created by luke.jackson2 on 04/08/2016.
  */
object DataFetcher {

  //Implicit Var
  implicit val apiKey : String  = "RGAPI-6BA9CEBD-28BF-42F5-A4DE-699DCFC4A98D"

  //Global Vars
  val summonerNameString : String = "ljackso"

  def httpGET (address : String): Future[String] = {
    val svc = url(address)
    Http(svc OK as.String)
  }
  def requestsJsonFromApiCall(apiCall : String) (implicit apiKey : String) : Future[String] = httpGET(apiCall + "api_key=" + apiKey)

  def decodeJson[T: DecodeJson] (j : String): Future[T] = Future {
    j.decode[T].leftMap (_ => new Exception ("Failed to decoded json.")).toEither match {
      case Left (e) => throw e
      case Right (decoded) => decoded
    }
  }

}
