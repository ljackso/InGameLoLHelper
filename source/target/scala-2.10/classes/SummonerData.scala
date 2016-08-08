import argonaut.Argonaut._

/**
  * Created by luke.jackson2 on 04/08/2016.
  */
object SummonerData {

  //Summoner DTO
  case class SummonerDto(id:Long, name:String, profileIconId:Int, revisionDate:Long, summonerLevel:Long){
    lazy val stringify = s"id:$id, name:$name, profileIconId:$profileIconId, revisionDate:$revisionDate, summonerLevel:$summonerLevel"
  }
  object SummonerDto {
    implicit val summonerDto = casecodec5(SummonerDto.apply, SummonerDto.unapply)("id", "name", "profileIconId", "revisionDate", "summonerLevel")
  }
  //Summoner name
  case class SummonerNameMap(nameMap : Map[String, SummonerDto]) {
    lazy val stringify = nameMap.values.map(summonerDto => summonerDto.stringify)
  }
  object SummonerNameMap{
    implicit val summonerNameMap = casecodec1(SummonerNameMap.apply, SummonerNameMap.unapply)("data")
  }
  def prepareSummonerNameJSONForParse(json : String) : String =  ("{\"data\" : " + json + "}")

  //Mastery Info
  case class Mastery(id:Long, rank:Int) {
    lazy val stringify = s"id : $id. rank : $rank"
  }
  object Mastery {
    implicit val mastery = casecodec2(Mastery.apply, Mastery.unapply)("masteryId","rank")
  }

  //Runes Info
  case class Rune(count:Int, id:Int) {
    lazy val stringify = s"id : $id. count : $count"
  }
  object Rune {
    implicit val rune = casecodec2(Rune.apply, Rune.unapply)("count","runeId")
  }
}
