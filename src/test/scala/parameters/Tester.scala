package parameters

import org.scalatest.FunSuite
import chisel3.iotesters.{Driver, PeekPokeTester}

class Tester extends FunSuite {
  test("ParameterizedScalaObject correctness test") {
    val obj1 = new ParameterizedScalaObject(4, "Hello")
    val obj2 = new ParameterizedScalaObject(4+2, "World")
  }

  test("ParameterizedWidthAdder module correctness test (1, 4, 6)") {
    val testResult = Driver(() => new ParameterizedWidthAdder(1, 4, 6)) {
      c => new PeekPokeTester(c) {
        for(in0 <- 0 to 1) {
          for(in1 <- 0 to 15) {
            val sum = in0 + in1
            //println(s"in0: $in0, in1: $in1, sum: $sum")
            poke(c.io.in0, in0)
            poke(c.io.in1, in1)
            expect(c.io.sum, sum)
          }
        }
      }
    }
    assert(testResult, "failed")
  }
}
