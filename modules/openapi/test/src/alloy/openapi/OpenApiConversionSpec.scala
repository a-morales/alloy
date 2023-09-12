/* Copyright 2022 Disney Streaming
 *
 * Licensed under the Tomorrow Open Source Technology License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    https://disneystreaming.github.io/TOST-1.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package alloy.openapi

import _root_.software.amazon.smithy.model.Model

import scala.io.Source
import scala.util.Using
import software.amazon.smithy.openapi.OpenApiConfig
import software.amazon.smithy.openapi.OpenApiVersion

final class OpenApiConversionSpec extends munit.FunSuite {

  test("OpenAPI conversion from alloy#simpleRestJson protocol") {
    val model = Model
      .assembler()
      .addImport(getClass().getClassLoader().getResource("foo.smithy"))
      .discoverModels()
      .assemble()
      .unwrap()

    val result = convert(model, None)
      .map(_.contents)
      .mkString
      .filterNot(_.isWhitespace)

    val expected = Using
      .resource(Source.fromResource("foo.json"))(
        _.getLines().mkString.filterNot(_.isWhitespace)
      )

    assertEquals(result, expected)
  }

  test("OpenAPI conversion from testJson protocol") {
    val model = Model
      .assembler()
      .addImport(getClass().getClassLoader().getResource("baz.smithy"))
      .discoverModels()
      .assemble()
      .unwrap()

    val result = convert(model, None)
      .map(_.contents)
      .mkString
      .filterNot(_.isWhitespace)

    val expected = Using
      .resource(Source.fromResource("baz.json"))(
        _.getLines().mkString.filterNot(_.isWhitespace)
      )
    assertEquals(result, expected)
  }

  test("OpenAPI conversion configuring the version") {
    val model = Model
      .assembler()
      .addImport(getClass().getClassLoader().getResource("foo.smithy"))
      .discoverModels()
      .assemble()
      .unwrap()

    val result = convertWithConfig(model, None, _ => {
      val config = new OpenApiConfig()
      config.setVersion(OpenApiVersion.VERSION_3_1_0)
      config
    })
      .map(_.contents)
      .mkString
      .filterNot(_.isWhitespace)

    assert(result.contains("\"openapi\":\"3.1.0"))
  }

}
