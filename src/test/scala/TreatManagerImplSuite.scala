import com.sky.detector.domain.{Treat, UserActions, LogLine}
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
class TreatManagerImplSuite extends JUnitSuite with TableDrivenPropertyChecks with ShouldMatchers {

  val treatManager = TestComponentRegistry.treatManager

  val loglinesNoTread =
    Table(
      ("logLine", "expected"),
      (new LogLine("80.238.9.179",133612947,UserActions.SIGNIN_SUCCESS,"Dave.Branning"), None),
      (new LogLine("80.238.9.179",133612947,UserActions.SIGNIN_SUCCESS,"Dave.Branning"), None),
      (new LogLine("80.238.9.179",133612947,UserActions.SIGNIN_SUCCESS,"Dave.Branning"), None)
    )

  val loglinesOneTread =
    Table(
      ("logLine", "expected"),
      (new LogLine("80.238.9.179",133612947,UserActions.SIGNIN_FAILURE,"Dave.Branning"), Some(new Treat("80.238.9.179", 133612947, 1))),
      (new LogLine("80.238.9.179",133612948,UserActions.SIGNIN_FAILURE,"Dave.Branning"), Some(new Treat("80.238.9.179", 133612947, 2))),
      (new LogLine("80.238.9.180",133612947,UserActions.SIGNIN_SUCCESS,"Dave.Branning"), None),
      (new LogLine("80.238.9.180",133612946,UserActions.SIGNIN_SUCCESS,"Dave.Branning"), None),
      (new LogLine("80.238.9.179",133612949,UserActions.SIGNIN_FAILURE,"Dave.Branning"), Some(new Treat("80.238.9.179", 133612947, 3))),
      (new LogLine("80.238.9.179",133612950,UserActions.SIGNIN_FAILURE,"Dave.Branning"), Some(new Treat("80.238.9.179", 133612947, 4))),
      (new LogLine("80.238.9.179",133612951,UserActions.SIGNIN_FAILURE,"Dave.Branning"), Some(new Treat("80.238.9.179", 133612947, 5)))
    )

  val loglinesTwoTreads =
    Table(
      ("logLine", "expected"),
      (new LogLine("80.238.9.179",133612947,UserActions.SIGNIN_FAILURE,"Dave.Branning"), Some(new Treat("80.238.9.179", 133612947, 1))),
      (new LogLine("80.238.9.179",133612948,UserActions.SIGNIN_FAILURE,"Dave.Branning"), Some(new Treat("80.238.9.179", 133612947, 2))),
      (new LogLine("80.238.9.180",133612947,UserActions.SIGNIN_FAILURE,"Dave.Branning"), Some(new Treat("80.238.9.180", 133612947, 1))),
      (new LogLine("80.238.9.180",133612946,UserActions.SIGNIN_FAILURE,"Dave.Branning"), Some(new Treat("80.238.9.180", 133612947, 2))),
      (new LogLine("80.238.9.179",133612949,UserActions.SIGNIN_FAILURE,"Dave.Branning"), Some(new Treat("80.238.9.179", 133612947, 3))),
      (new LogLine("80.238.9.179",133612950,UserActions.SIGNIN_FAILURE,"Dave.Branning"), Some(new Treat("80.238.9.179", 133612947, 4))),
      (new LogLine("80.238.9.179",133612951,UserActions.SIGNIN_FAILURE,"Dave.Branning"), Some(new Treat("80.238.9.179", 133612947, 5))),
      (new LogLine("80.238.9.180",133612950,UserActions.SIGNIN_FAILURE,"Dave.Branning"), Some(new Treat("80.238.9.180", 133612947, 3))),
      (new LogLine("80.238.9.180",133612950,UserActions.SIGNIN_FAILURE,"Dave.Branning"), Some(new Treat("80.238.9.180", 133612947, 4))),
      (new LogLine("80.238.9.180",133612950,UserActions.SIGNIN_FAILURE,"Dave.Branning"), Some(new Treat("80.238.9.180", 133612947, 5)))
    )

  val loglinesFiveTreadsThatAreNotWithinTimeFrame =
    Table(
      ("logLine", "expected"),
      (new LogLine("80.238.9.179",133612947,UserActions.SIGNIN_FAILURE,"Dave.Branning"), Some(new Treat("80.238.9.179", 133612947, 1))),
      (new LogLine("80.238.9.179",133612948,UserActions.SIGNIN_FAILURE,"Dave.Branning"), Some(new Treat("80.238.9.179", 133612947, 2))),
      (new LogLine("80.238.9.180",133612947,UserActions.SIGNIN_FAILURE,"Dave.Branning"), Some(new Treat("80.238.9.180", 133612947, 1))),
      (new LogLine("80.238.9.180",133612946,UserActions.SIGNIN_FAILURE,"Dave.Branning"), Some(new Treat("80.238.9.180", 133612947, 2))),
      (new LogLine("80.238.9.179",133612949,UserActions.SIGNIN_FAILURE,"Dave.Branning"), Some(new Treat("80.238.9.179", 133612947, 3))),
      (new LogLine("80.238.9.179",133612950,UserActions.SIGNIN_FAILURE,"Dave.Branning"), Some(new Treat("80.238.9.179", 133612947, 4))),
      (new LogLine("80.238.9.179",144612951,UserActions.SIGNIN_FAILURE,"Dave.Branning"), Some(new Treat("80.238.9.179", 144612951, 1)))
    )

  val loglinesDeleteEldestElements =
    Table(
      ("logLine", "expected"),
      (new LogLine("80.238.9.179",133612947,UserActions.SIGNIN_FAILURE,"Dave.Branning"), Some(new Treat("80.238.9.179", 133612947, 1))),
      (new LogLine("80.238.9.180",143612947,UserActions.SIGNIN_FAILURE,"Dave.Branning"), Some(new Treat("80.238.9.180", 143612947, 1)))
    )

  @Before
  def setup() {
    treatManager.clear()
  }

  @Test
  def shouldTreatManagerNotAddAnyTreat() {
    forAll (loglinesNoTread) { (line: LogLine, expected: Option[Treat]) =>
      treatManager.createTreat(line) should be (expected)
    }
    treatManager.totalTreats should be (0)
  }

  @Test
  def shouldTreatManagerNotSumAnyTreat() {
    forAll (loglinesNoTread) { (line: LogLine, expected: Option[Treat]) =>
      treatManager.sumTreat(line) should be (expected)
    }
    treatManager.totalTreats should be (0)
  }


  @Test
  def shouldTreatManagerSumOneTreat() {
    forAll (loglinesOneTread) { (line: LogLine, expected: Option[Treat]) =>
      treatManager.sumTreat(line) should be (expected)
    }
    treatManager.totalTreats should be (1)
  }

  @Test
  def shouldTreatManagerGetAnExistentTreat() {
    forAll (loglinesOneTread) { (line: LogLine, expected: Option[Treat]) =>
      treatManager.sumTreat(line) should be (expected)
    }
    treatManager.totalTreats should be (1)
    val result = treatManager.getTread(new LogLine("80.238.9.179",133612951,UserActions.SIGNIN_FAILURE,"Dave.Branning"))
    result should not be None
  }

  @Test
  def shouldTreatManagerNotGetAnUnexistentTreat() {
    val result = treatManager.getTread(new LogLine("80.238.9.179",133612951,UserActions.SIGNIN_FAILURE,"Dave.Branning"))
    result should be (None)
  }

  @Test
  def shouldTreatManagerSumTwoTreat() {
    forAll (loglinesTwoTreads) { (line: LogLine, expected: Option[Treat]) =>
      treatManager.sumTreat(line) should be (expected)
    }
    treatManager.totalTreats should be (2)
  }

  @Test
  def shouldTreatManagerAddTreadsThatAreNotWithinTimeFrame() {
    forAll (loglinesFiveTreadsThatAreNotWithinTimeFrame) { (line: LogLine, expected: Option[Treat]) =>
      treatManager.sumTreat(line) should be (expected)
    }
    treatManager.totalTreats should be (2)
    val result = treatManager.getTread(new LogLine("80.238.9.179",144612951,UserActions.SIGNIN_FAILURE,"Dave.Branning"))
    result should be (Some(new Treat("80.238.9.179", 144612951, 1)))
  }

  @Test
  def shouldTreatManagerEldestElement() {
    forAll (loglinesDeleteEldestElements) { (line: LogLine, expected: Option[Treat]) =>
      treatManager.sumTreat(line) should be (expected)
    }
    treatManager.totalTreats should be (1)
  }
}
