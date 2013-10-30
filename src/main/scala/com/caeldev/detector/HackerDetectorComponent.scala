package com.caeldev.detector

import com.sky.detector.domain.{LogLine, UserActions}

/**
 * Copyright (c) 2012 - 2013 Caeldev, Inc.
 *
 * User: cael
 * Date: 29/10/2013
 * Time: 18:32
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
trait HackerDetectorComponent {
  this:TreatManagerComponent with LogLineBuilderComponent =>
  val hackerDetector:HackerDetector
}

trait HackerDetectorComponentImpl extends HackerDetectorComponent {
  this:TreatManagerComponent with LogLineBuilderComponent =>

  class HackerDetectorImpl extends HackerDetector {
    def parseLine(line: String): String = {
      val logLine = builder.build(line)
      logLine.action match {
        case UserActions.SIGNIN_SUCCESS => {
          null
        }
        case UserActions.SIGNIN_FAILURE => {
          countTreat(logLine)
        }
      }
    }

    private def countTreat(logLine:LogLine):String = {
      val result = treatManager.sumTreat(logLine)
      result match {
        case Some(c) if c.count == 5 => {
          c.ip
        }
        case _ => {
          null
        }
      }
    }
  }
}
