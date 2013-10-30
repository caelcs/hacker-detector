import com.caeldev.detector._
import com.sky.detector._
import com.sky.detector.domain.LogLine
import com.sky.detector.domain.Treat
import com.sky.detector.domain.{Treat, LogLine}
import org.scalatest.mock.MockitoSugar

/**
 * Copyright (c) 2012 - 2013 Caeldev, Inc.
 *
 * User: cael
 * Date: 29/10/2013
 * Time: 18:40
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
object TestComponentRegistry extends HackerDetectorComponentImpl with LogLineBuilderComponentImpl with TreatManagerComponentImpl with  MockitoSugar {
  val hackerDetector: HackerDetector = ComponentRegistry.hackerDetector
  val builder: Builder[String, LogLine] = ComponentRegistry.builder
  val treatManager: TreatManager[LogLine, Option[Treat]] = ComponentRegistry.treatManager
}
