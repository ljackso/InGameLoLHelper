import argonaut.Argonaut._
import SummonerData._

/**
  * Created by luke.jackson2 on 04/08/2016.
  */
object CurrentGameData {

  //Observer in Lol
  case class LoLObserver (encryptionKey : String)
  object LoLObserver {
    implicit val loLObserver = casecodec1(LoLObserver.apply, LoLObserver.unapply)("encryptionKey")
  }

  //A Player in the game
  case class CurrentGameParticipant (bot:Boolean, championId:Long, masteries:List[Mastery], profileIconId:Long, runes:List[Rune],
                                     spell1Id:Long, spell2Id:Long, summonerId:Long,summonerName:String, teamId:Long) {
    lazy val stringify = s"isBot : $bot, summonerName : $summonerName"
  }
  object CurrentGameParticipant {
    implicit val currentGameParticipant = casecodec10(CurrentGameParticipant.apply, CurrentGameParticipant.unapply)("bot", "championId", "masteries",
                                          "profileIconId", "runes", "spell1Id", "spell2Id", "summonerId", "summonerName", "teamId")
  }

  //Banned Champion
  case class BannedChampion(championId:Long, pickTurn:Int, teamId:Long){
    lazy val stringify = s"championId : $championId"
  }
  object BannedChampion {
    implicit val bannedChampion = casecodec3(BannedChampion.apply, BannedChampion.unapply)("championId", "pickTurn","teamId")
  }

  //Current Game Info
  case class CurrentGameInfo(bannedChampions:List[BannedChampion], gameId:Long,gameLength:Long, gameMode:String, gameQueueConfigId:Long,
                              gameStartTime:Long, gameType:String, mapId:Long, observers:LoLObserver, participants:List[CurrentGameParticipant],
                              platformId : String ) {

    lazy val stringify = s"gameMode : $gameMode, platformId : $platformId "
  }
  object CurrentGameInfo {
    implicit val currentGameInfo = casecodec11(CurrentGameInfo.apply, CurrentGameInfo.unapply)("bannedChampions", "gameId", "gameLength","gameMode",
                                    "gameQueueConfigId", "gameStartTime", "gameType", "mapId", "observers", "participants", "platformId")

  }
}
