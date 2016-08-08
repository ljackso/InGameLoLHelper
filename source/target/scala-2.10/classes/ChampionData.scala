import argonaut.Argonaut._
/**
  * Created by luke.jackson2 on 04/08/2016.
  */
object ChampionData {

  case class ChampionInfo(defense:Int, magic:Int, difficulty:Int, attack:Int)
  object ChampionInfo {
    implicit val championInfo = casecodec4(ChampionInfo.apply, ChampionInfo.unapply)("defense","magic","difficulty","attack")
  }

  case class ChampionDetails(id:Long, title:String, name:String, key:String, info:ChampionInfo)
  object ChampionDetails {
   implicit val championDetails = casecodec5(ChampionDetails.apply, ChampionDetails.unapply)("id","title","name","key","info")
  }

  case class ChampionDto(id: Long, title: String, name: String, key: String) {
    lazy val stringify = s" name : $name, id : $id"
  }
  object ChampionDto {
    implicit val championDto = casecodec4(ChampionDto.apply, ChampionDto.unapply)("id", "title", "name", "key")
  }

  case class ChampionMapping(data:Map[String, ChampionDto], lolType : String, lolVersion : String)
  object ChampionMapping {
    implicit val championMapping = casecodec3(ChampionMapping.apply, ChampionMapping.unapply)("data","type","version" )
  }
}
