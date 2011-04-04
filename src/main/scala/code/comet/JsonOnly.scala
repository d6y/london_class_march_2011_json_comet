package code
package comet

import net.liftweb._
import common._
import util._
import http._
import js._
import JsCmds._
import JE._
import json._
import Helpers._

object MyJsonOnly extends SessionVar[Box[JsonOnly]](Empty)

class JsonOnly extends CometActor {
  var myList = List(55)

  private def ping() {
    Schedule.schedule(() => this ! Ping(true), 10 seconds)
  }

  def render = 
    JsRaw("myInfo = ["+myList.mkString(",")+"]") &
    JsRaw("drawMe()")

  override def lowPriority = {
    case Ping(start) => {
      val info: Int = randomInt(5000)
      myList = myList ::: List(info)

      partialUpdate(JsRaw("myInfo.push("+info+")") &
                  JsRaw("drawMe()"))
      if (start) ping()
    }
  }

  ping()
  MyJsonOnly.set(Full(this))
}

case class Ping(start: Boolean)
