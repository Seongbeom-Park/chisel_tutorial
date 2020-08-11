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

  test("Sort4 module ascending test") {
    val testResult = Driver(() => new Sort4(true)) {
      c => new PeekPokeTester(c) {
        poke(c.io.in0, 3)
        poke(c.io.in1, 6)
        poke(c.io.in2, 9)
        poke(c.io.in3, 12)
        expect(c.io.out0, 3)
        expect(c.io.out1, 6)
        expect(c.io.out2, 9)
        expect(c.io.out3, 12)

        poke(c.io.in0, 13)
        poke(c.io.in1, 4)
        poke(c.io.in2, 6)
        poke(c.io.in3, 1)
        expect(c.io.out0, 1)
        expect(c.io.out1, 4)
        expect(c.io.out2, 6)
        expect(c.io.out3, 13)

        poke(c.io.in0, 13)
        poke(c.io.in1, 6)
        poke(c.io.in2, 4)
        poke(c.io.in3, 1)
        expect(c.io.out0, 1)
        expect(c.io.out1, 4)
        expect(c.io.out2, 6)
        expect(c.io.out3, 13)
      }
    }
    assert(testResult, "failed")
  }

  test("Sort4 module descending test") {
    val testResult = Driver(() => new Sort4(false)) {
      c => new PeekPokeTester(c) {
        poke(c.io.in0, 3)
        poke(c.io.in1, 6)
        poke(c.io.in2, 9)
        poke(c.io.in3, 12)
        expect(c.io.out0, 12)
        expect(c.io.out1, 9)
        expect(c.io.out2, 6)
        expect(c.io.out3, 3)
 
        poke(c.io.in0, 13)
        poke(c.io.in1, 4)
        poke(c.io.in2, 6)
        poke(c.io.in3, 1)
        expect(c.io.out0, 13)
        expect(c.io.out1, 6)
        expect(c.io.out2, 4)
        expect(c.io.out3, 1)
          
        poke(c.io.in0, 1)
        poke(c.io.in1, 6)
        poke(c.io.in2, 4)
        poke(c.io.in3, 13)
        expect(c.io.out0, 13)
        expect(c.io.out1, 6)
        expect(c.io.out2, 4)
        expect(c.io.out3, 1)
      }
    }
    assert(testResult, "failed")
  }

  // TODO: DelayBy1
  test("DelayBy1 module correctness test") {
    val testResult = Driver(() => new DelayBy1()) {
      c => new PeekPokeTester(c) {
        poke(c.io.in, 0)
        expect(c.io.out, 0)
        step(1)
        poke(c.io.in, 1)
        expect(c.io.out, 0)
        step(1)
        expect(c.io.out, 1)
      }
    }
    assert(testResult, "failed")
  }

  test("DelayBy1 module correctness test initialized by 3") {
    val testResult = Driver(() => new DelayBy1(Some(3))) {
      c => new PeekPokeTester(c) {
        poke(c.io.in, 0)
        expect(c.io.out, 3)
        step(1)
        poke(c.io.in, 1)
        expect(c.io.out, 0)
        step(1)
        expect(c.io.out, 1)
      }
    }
    assert(testResult, "failed")
  }

  test("DelayBy1 module correctness test initialized by 12345") {
    val testResult = Driver(() => new DelayBy1(Some(12345))) {
      c => new PeekPokeTester(c) {
        poke(c.io.in, 0)
        expect(c.io.out, 12345)
        step(1)
        poke(c.io.in, 1)
        expect(c.io.out, 0)
        step(1)
        expect(c.io.out, 1)
      }
    }
    assert(testResult, "failed")
  }
}
