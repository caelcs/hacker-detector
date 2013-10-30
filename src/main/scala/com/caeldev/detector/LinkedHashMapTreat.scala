package com.caeldev.detector

import com.sky.detector.domain.Treat
import java.util.concurrent.atomic.AtomicLong
import java.util.Map.Entry

/**
 * Copyright (c) 2012 - 2013 Caeldev, Inc.
 *
 * User: cael
 * Date: 30/10/2013
 * Time: 15:01
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
class LinkedHashMapTreat[K] extends java.util.LinkedHashMap[K,Treat] {

  val currentTimestamp:AtomicLong  = new AtomicLong(0L)

  override def put(key: K, value: Treat): Treat = {
    currentTimestamp.set(value.timestamp)
    super.put(key, value)
  }

  override def removeEldestEntry(eldest: Entry[K, Treat]): Boolean = {
    val differenceTime:Long = currentTimestamp.get() - eldest.getValue().timestamp
    (differenceTime >= 300000)
  }
}
