package com.caeldev.detector

import com.sky.detector.domain.{UserActions, LogLine}

/**
 * Copyright (c) 2012 - 2013 Caeldev, Inc.
 *
 * User: cael
 * Date: 29/10/2013
 * Time: 22:49
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
trait Builder[T, V] {
  def build(source:T):V
}

trait LogLineBuilderComponent {
  val builder:Builder[String, LogLine]
}

trait LogLineBuilderComponentImpl extends LogLineBuilderComponent {

  class LogLineBuilderImpl extends Builder[String, LogLine] {
    def build(source: String): LogLine = {
      require(source.ne(null))
      val splitValues = source.split(',')
      require(splitValues.length == 4)
      new LogLine(splitValues(0), splitValues(1).toLong*1000, UserActions.withName(splitValues(2)), splitValues(3))
    }
  }

}
