package com.caeldev.detector

import com.sky.detector.domain.{UserActions, LogLine, Treat}

/**
 * Copyright (c) 2012 - 2013 Caeldev, Inc.
 *
 * User: cael
 * Date: 29/10/2013
 * Time: 20:28
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
trait TreatManager[T, V] {
  def getTread(entity:T):V
  def sumTreat(entity: T):V
  def createTreat(entity:T):V
  def totalTreats:Int
  def clear()
}

trait TreatManagerComponent {
  val treatManager:TreatManager[LogLine, Option[Treat]]
}

trait TreatManagerComponentImpl extends TreatManagerComponent {

  class TreatManagerImpl extends TreatManager[LogLine, Option[Treat]] {

    val treatStore:java.util.Map[String, Treat] = new LinkedHashMapTreat[String]

    def getTread(entity: LogLine): Option[Treat] = {
      val result = treatStore.get(entity.ip)
      result match {
        case result:Treat => Some(result)
        case _ => None
      }
    }

    def sumTreat(entity: LogLine): Option[Treat] = {
      val treat = getTread(entity)
      treat match {
        case Some(t) => evalTreatTimeFrameforUpdate(t, entity)
        case None => createTreat(entity)
      }
    }

    def createTreat(entity: LogLine): Option[Treat] = {
      entity.action match {
        case UserActions.SIGNIN_SUCCESS => {
          None
        }
        case UserActions.SIGNIN_FAILURE => {
          val treatTemp = new Treat(entity.ip, entity.timestamp, 1)
          treatStore.put(entity.ip, treatTemp)
          Some(treatTemp)
        }
      }
    }

    def totalTreats:Int = {
      treatStore.size
    }

    def clear() {
      treatStore.clear
    }

    private def evalTreatTimeFrameforUpdate(treat:Treat, logLine:LogLine): Option[Treat] = {
      val differentTime = logLine.timestamp - treat.timestamp
      differentTime match {
        case diff if diff <= 300000 => {
          val treatTemp = new Treat(treat.ip, treat.timestamp, treat.count + 1)
          treatStore.put(treat.ip, treatTemp)
          Some(treatTemp)
        }
        case _ => {
          createTreat(logLine)
        }
      }
    }
  }
}
