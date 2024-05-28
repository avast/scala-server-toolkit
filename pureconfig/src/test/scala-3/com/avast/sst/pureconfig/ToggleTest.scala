package com.avast.sst.pureconfig

import cats.{Applicative, Eq, FlatMap, Functor, Monad, Monoid}
import com.avast.sst.pureconfig.util.Toggle
import com.avast.sst.pureconfig.util.Toggle.{Disabled, Enabled}
import org.scalatest.diagrams.Diagrams
import org.scalatest.funsuite.AnyFunSuite

import scala.annotation.nowarn

@nowarn("msg=unused value")
class ToggleTest extends AnyFunSuite with Diagrams {

  test("has Functor instance and map method works correctly") {
    import com.avast.sst.pureconfig.util.Toggle.ToggleStdInstances.*

    val oldValue = "some value"
    val newValue = "new value"
    val toggle: Toggle[String] = Enabled(oldValue)
    val toggle2: Toggle[String] = Disabled
    val result = Functor[Toggle].map(toggle)(_ => newValue)

    import cats.syntax.functor.*
    val result2 = toggle.map(_ => newValue)

    val result3 = toggle2.map(_ => newValue)

    assert(result.fold(false, value => value === newValue))
    assert(result2.fold(false, value => value === newValue))
    assert(result3.fold(true, _ => false))
  }

  test("has FlatMap instance and flatMap method works correctly") {
    import com.avast.sst.pureconfig.util.Toggle.ToggleStdInstances.*

    val oldValue = "some value"
    val newValue = "new value"
    val toggle: Toggle[String] = Enabled(oldValue)
    val toggle2: Toggle[String] = Disabled
    val result = FlatMap[Toggle].flatMap(toggle)(_ => Enabled(newValue))

    import cats.syntax.flatMap.*
    val result2 = toggle.flatMap(_ => Enabled(newValue))

    val result3 = toggle2.flatMap(_ => Enabled(newValue))
    val result4 = toggle.flatMap(_ => Disabled)

    assert(result.fold(false, value => value === newValue))
    assert(result2.fold(false, value => value === newValue))
    assert(result3 === Disabled)
    assert(result4 === Disabled)
  }

  test("has Applicative and Monad instance and pure method works correctly") {
    import com.avast.sst.pureconfig.util.Toggle.ToggleStdInstances.*
    val value = "some value"
    val result = Applicative[Toggle].pure(value)
    val result2 = Monad[Toggle].pure(value)

    import cats.syntax.applicative.*
    val result3 = value.pure[Toggle]

    assert(result.fold(false, value => value === value))
    assert(result2.fold(false, value => value === value))
    assert(result3.fold(false, value => value === value))
  }

  test("has Monoid instance and combine method works correctly") {
    import com.avast.sst.pureconfig.util.Toggle.ToggleStdInstances.*

    val result = Monoid[Toggle[String]].empty

    val value1 = 1
    val value2 = 2
    val toggle1: Toggle[Int] = Enabled(value1)
    val toggle2: Toggle[Int] = Enabled(value2)
    val toggle3: Toggle[Int] = Disabled

    import cats.syntax.monoid.*
    val result2 = toggle1.combine(toggle2)
    val result3 = toggle1.combine(Disabled)
    val result4 = toggle3.combine(toggle2)
    val result5 = toggle3.combine(Disabled)

    assert(result === Disabled)
    assert(result2.fold(false, value => value === 3))
    assert(result3.fold(false, value => value === 1))
    assert(result4.fold(false, value => value === 2))
    assert(result5 === Disabled)
  }

  test("has Order instance and compare method works correctly") {
    import com.avast.sst.pureconfig.util.Toggle.ToggleStdInstances.*

    val value1 = 1
    val value2 = 2
    val toggle1: Toggle[Int] = Enabled(value1)
    val toggle2: Toggle[Int] = Enabled(value2)
    val toggle3: Toggle[Int] = Disabled

    import cats.syntax.order.*
    val result1 = toggle1.compare(toggle2)
    val result2 = toggle1.compare(toggle1)
    val result3 = toggle2.compare(toggle1)
    val result4 = toggle3.compare(toggle2)
    val result5 = toggle3.compare(Disabled)

    assert(result1 < 0)
    assert(Eq[Int].eqv(result2, 0))
    assert(result3 > 0)
    assert(result4 < 0)
    assert(Eq[Int].eqv(result5, 0))
  }
}
