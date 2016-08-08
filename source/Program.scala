import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Await, Future}
import DataFetcher._
import SummonerData._
import ChampionData._
import MatchData._
import scala.util.Success
import scala.util.Failure
import argonaut._

object Program {

  def scrubDetector(args: List[String]): Unit = {

    //Get Champion data
    val championIdMappingF: Future[Map[Long, String]] = for {
      json <- requestsJsonFromApiCall("https://global.api.pvp.net/api/lol/static-data/euw/v1.2/champion?")
      championMapping <- decodeJson[ChampionMapping](json)

      //Create an id to name map.
      championDtos = championMapping.data.values.toSeq
      championIdMapping = Map(championDtos map { champDto => champDto.id -> champDto.name }: _*)

    } yield championIdMapping

    //Get Summoners Match Data
    val matchListF: Future[List[MatchReference]] = for {

      //Get the summoner Id
      summonerJson <- requestsJsonFromApiCall("https://euw.api.pvp.net/api/lol/euw/v1.4/summoner/by-name/".concat(summonerNameString) + "?")
      summonerNameMap <- decodeJson[SummonerNameMap](prepareSummonerNameJSONForParse(summonerJson))
      summonerId = summonerNameMap.nameMap.get(summonerNameString).getOrElse(null).id

      //Get the summoners match history
      matchJson <- requestsJsonFromApiCall("https://euw.api.pvp.net/api/lol/euw/v2.2/matchlist/by-summoner/".concat(summonerId.toString).concat("?"))
      matchList <- decodeJson[MatchList](matchJson)
      matchReferenceList = matchList.matches

    } yield (matchReferenceList)

    //Wait on both futures to complete.
    val listFutures : List[Future[Equals]] = List(championIdMappingF, matchListF)
    val seqFutures : Future[List[Equals]] = Future.sequence(listFutures)

    //Act on completion of the future sequence
    seqFutures.onComplete {
      case Success(seq)  =>
        println("\nMatch List Data and Champion Mapping Retrieved Successfully ")
        detectScrubFromMatchHistory(seq.tail.head.asInstanceOf[List[MatchReference]], seq.head.asInstanceOf[Map[Long, String]])

      case Failure(err) =>
        println("Failed to get data required, error message : " + err.getMessage)
    }
  }

  def detectScrubFromMatchHistory(matchReferences : List[MatchReference], championNameMapping : Map[Long, String]) : Unit = {

    println("\nChampions Played Info :")
    val idToPlayedMapping : Map[Long, Int] = matchReferences.map(matchRef => matchRef.champion).groupBy(identity).mapValues(_.size)
    idToPlayedMapping.foreach{case (k, v) => println("Played " + championNameMapping.get(k).getOrElse("Some Champion....") + " " + v + " times!" )}

    println("\nDetailed Champion Played Info")
    val listFutures : List[Future[Equals]] = idToPlayedMapping.keys.toList.map(id => getChampDetails(id))
    val seqFutures :Future[List[Equals]] = Future.sequence(listFutures)

    seqFutures.onComplete {
      case Success(seq)  =>
        println("\nChampion Detail Data Retrieved Successfully ")
        tellIfScrub(idToPlayedMapping, seq.asInstanceOf[List[ChampionDetails]])

      case Failure(err) =>
        println("Failed to get data required, error message : " + err.getMessage)
    }
  }

  def getChampDetails (champId:Long) : Future[ChampionDetails] = {

    val champDetailsF : Future[ChampionDetails] = for {
      champJson <- requestsJsonFromApiCall("https://global.api.pvp.net/api/lol/static-data/euw/v1.2/champion/".concat(champId.toString).concat("?champData=info&"))
      champDetails <- decodeJson[ChampionDetails](champJson)
    } yield champDetails

    champDetailsF.onComplete {
      case Success(champDetails) => println(champDetails.name + " finished loading")
      case Failure(err) => println("Failed to get champion info :" + err.getMessage)
    }
    champDetailsF
  }

  def getChampionDetailsFromId(championDetails: List[ChampionDetails], id:Long): ChampionDetails = {
    var dets : ChampionDetails = null
    for (champDet <- championDetails if champDet.id == id) {dets = champDet}
    dets
  }

  def tellIfScrub(playedMap: Map[Long, Int], champsDetails : List[ChampionDetails]) : Unit = {
    var scruby : Int = 0
    var pro : Int = 0

    for((id, played) <- playedMap) {
      var dets : ChampionDetails = getChampionDetailsFromId(champsDetails, id)
      if(dets.info.difficulty > 6) {pro += played} else {scruby += played}
    }

    println("Played scruby champs " + scruby + " times")
    println("Played pro champs " + pro + " times")
    if(scruby >= pro) {
      println("Certified scrub")
    }
    else {
      println("Cerifeid pro")
    }
  }

  def main (args: Array[String]): Unit = scrubDetector(args.toList)
}
