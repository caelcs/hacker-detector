import com.sky.detector.domain.{UserActions, LogLine}
import org.junit.Test
import org.scalatest.junit.JUnitSuite
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.prop.TableDrivenPropertyChecks

/**
  * Copyright (c) 2012 - 2013 Caeldev, Inc.
  *
  * User: cael
  * Date: 29/10/2013
  * Time: 18:22
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
class LogLineBuilderImplSuite extends JUnitSuite with TableDrivenPropertyChecks with ShouldMatchers {

   val logLineBuilder = TestComponentRegistry.builder

  val loglinesSample =
    Table(
      ("line", "expected"),
      ("80.238.9.179,133612947,SIGNIN_SUCCESS,Dave.Branning", new LogLine("80.238.9.179", 133612947, UserActions.SIGNIN_SUCCESS, "Dave.Branning")),
      ("80.238.9.179,133612948,SIGNIN_FAILURE,Adolfo", new LogLine("80.238.9.179", 133612948, UserActions.SIGNIN_FAILURE, "Adolfo"))

    )
  val loglinesFailedSample =
    Table(
      "line",
      "80.238.9.179,SIGNIN_SUCCESS,Dave.Branning",
      "80.238.9.179,SIGNIN_SUCCESS,Dave.Branning,80.238.9.179,SIGNIN_SUCCESS,Dave.Branning",
      "80.238.9.179",
      null
    )

  @Test
  def shouldBuildALogLineEntityFromString() {
    forAll (loglinesSample) { (line: String, expected: LogLine) =>
      logLineBuilder.build(line) should be equals expected
    }
  }

  @Test
  def shouldFailBuildingFromAnyRandomString() {
    forAll (loglinesFailedSample) { (line: String) =>
      intercept[IllegalArgumentException] {
        logLineBuilder.build(line)
      }
    }
  }
 }
