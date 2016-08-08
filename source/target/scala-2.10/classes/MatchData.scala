import argonaut.Argonaut._
/**
  * Created by luke.jackson2 on 04/08/2016.
  */
object MatchData {


  case class MatchReference(champion:Long, lane:String, matchId:Long, platformId:String, queue:String, region:String, role:String, season:String, timestamp:Long) {
    lazy val stringify = s"championId : $champion, lane : $lane, matchId : $matchId"
  }
  object MatchReference {
    implicit val matchReference = casecodec9(MatchReference.apply, MatchReference.unapply)("champion","lane","matchId","platformId", "queue","region",
                                    "role", "season", "timestamp")
  }

  //Match List
  case class MatchList( matches:List[MatchReference], startIndex:Int, endIndex:Int, totalGames:Int) {
    lazy val stringify = s"total Games = $totalGames"
  }
  object MatchList {
    implicit val matchList = casecodec4(MatchList.apply, MatchList.unapply)("matches","startIndex","endIndex", "totalGames")
  }
}
