import com.caeldev.detector.HackerDetector
import org.junit.{Before, Test}
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
class HackerDetectorImplSuite extends JUnitSuite with TableDrivenPropertyChecks with ShouldMatchers {

  val hackerDetector:HackerDetector = TestComponentRegistry.hackerDetector
  val treatManager = TestComponentRegistry.treatManager

  val loglinesNoTread =
    Table(
      ("line", "expected"),
      ("80.238.9.179,133612947,SIGNIN_SUCCESS,Dave.Branning", null),
      ("80.238.9.179,133612947,SIGNIN_SUCCESS,Dave.Branning", null),
      ("80.238.9.179,133612947,SIGNIN_SUCCESS,Dave.Branning", null)
    )

  val loglinesOneTread =
    Table(
      ("line", "expected"),
      ("80.238.9.179,133612947,SIGNIN_FAILURE,Dave.Branning", null),
      ("80.238.9.179,133612948,SIGNIN_FAILURE,Dave.Branning", null),
      ("80.238.9.180,133612947,SIGNIN_SUCCESS,Dave.Branning", null),
      ("80.238.9.180,133612946,SIGNIN_SUCCESS,Dave.Branning", null),
      ("80.238.9.179,133612949,SIGNIN_FAILURE,Dave.Branning", null),
      ("80.238.9.179,133612950,SIGNIN_FAILURE,Dave.Branning", null),
      ("80.238.9.179,133612951,SIGNIN_FAILURE,Dave.Branning", "80.238.9.179")
    )

  val loglinesTwoTreads =
    Table(
      ("line", "expected"),
      ("80.238.9.179,133612947,SIGNIN_FAILURE,Dave.Branning", null),
      ("80.238.9.179,133612948,SIGNIN_FAILURE,Dave.Branning", null),
      ("80.238.9.180,133612947,SIGNIN_FAILURE,Dave.Branning", null),
      ("80.238.9.180,133612946,SIGNIN_FAILURE,Dave.Branning", null),
      ("80.238.9.179,133612949,SIGNIN_FAILURE,Dave.Branning", null),
      ("80.238.9.179,133612950,SIGNIN_FAILURE,Dave.Branning", null),
      ("80.238.9.179,133612951,SIGNIN_FAILURE,Dave.Branning", "80.238.9.179"),
      ("80.238.9.180,133612950,SIGNIN_FAILURE,Dave.Branning", null),
      ("80.238.9.180,133612950,SIGNIN_FAILURE,Dave.Branning", null),
      ("80.238.9.180,133612950,SIGNIN_FAILURE,Dave.Branning", "80.238.9.180")

    )

  val loglinesFiveTreadsThatAreNotWithinTimeFrame =
    Table(
      ("line", "expected"),
      ("80.238.9.179,133612947,SIGNIN_FAILURE,Dave.Branning", null),
      ("80.238.9.179,133612948,SIGNIN_FAILURE,Dave.Branning", null),
      ("80.238.9.180,133612947,SIGNIN_FAILURE,Dave.Branning", null),
      ("80.238.9.180,133612946,SIGNIN_FAILURE,Dave.Branning", null),
      ("80.238.9.179,133612949,SIGNIN_FAILURE,Dave.Branning", null),
      ("80.238.9.179,133612950,SIGNIN_FAILURE,Dave.Branning", null),
      ("80.238.9.179,144612951,SIGNIN_FAILURE,Dave.Branning", null)
    )

  @Before
  def setup() {
    treatManager.clear()
  }

  @Test
  def shouldHackerDetectorDetectNoTread() {
    forAll (loglinesNoTread) { (line: String, expected: String) =>
      hackerDetector.parseLine(line) should be equals expected
    }
  }

  @Test
  def shouldHackerDetectorDetectOneTread() {
    forAll (loglinesOneTread) { (line: String, expected: String) =>
      hackerDetector.parseLine(line) should be equals expected
    }
  }

  @Test
  def shouldHackerDetectorDetectFiveTreadsThatAreNotWithinTimeFrame() {
    forAll (loglinesTwoTreads) { (line: String, expected: String) =>
      hackerDetector.parseLine(line) should be equals expected
    }
  }

  @Test
  def shouldHackerDetectorDetectTwoTreads() {
    forAll (loglinesFiveTreadsThatAreNotWithinTimeFrame) { (line: String, expected: String) =>
      hackerDetector.parseLine(line) should be equals expected
    }
  }
}
