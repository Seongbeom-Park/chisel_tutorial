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

  test("HalfFullAdder module correctness test HalfAdder (false)") {
    val testResult = Driver(() => new HalfFullAdder(false)) {
      c => new PeekPokeTester (c) {
        require(!c.hasCarry, "DUT must be half adder")
        // 0 + 0 = 0
        poke(c.io.a, 0)
        poke(c.io.b, 0)
        expect(c.io.s, 0)
        expect(c.io.carryOut, 0)
        // 0 + 1 = 1
        poke(c.io.b, 1)
        expect(c.io.s, 1)
        expect(c.io.carryOut, 0)
        // 1 + 1 = 2
        poke(c.io.a, 1)
        expect(c.io.s, 0)
        expect(c.io.carryOut, 1)
        // 1 + 0 = 1
        poke(c.io.b, 0)
        expect(c.io.s, 1)
        expect(c.io.carryOut, 0)
      }
    }
    assert(testResult, "failed")
  }

  test("HalfFullAdder module correctness test FullAdder (true)") {
    val testResult = Driver(() => new HalfFullAdder(true)) {
      c => new PeekPokeTester (c) {
        require(c.hasCarry, "DUT must be half adder")
        poke(c.io.carryIn.get, 0)
        // 0 + 0 + 0 = 0
        poke(c.io.a, 0)
        poke(c.io.b, 0)
        expect(c.io.s, 0)
        expect(c.io.carryOut, 0)
        // 0 + 0 + 1 = 1
        poke(c.io.b, 1)
        expect(c.io.s, 1)
        expect(c.io.carryOut, 0)
        // 0 + 1 + 1 = 2
        poke(c.io.a, 1)
        expect(c.io.s, 0)
        expect(c.io.carryOut, 1)
        // 0 + 1 + 0 = 1
        poke(c.io.b, 0)
        expect(c.io.s, 1)
        expect(c.io.carryOut, 0)
      
        poke(c.io.carryIn.get, 1)
        // 1 + 0 + 0 = 1
        poke(c.io.a, 0)
        poke(c.io.b, 0)
        expect(c.io.s, 1)
        expect(c.io.carryOut, 0)
        // 1 + 0 + 1 = 2
        poke(c.io.b, 1)
        expect(c.io.s, 0)
        expect(c.io.carryOut, 1)
        // 1 + 1 + 1 = 3
        poke(c.io.a, 1)
        expect(c.io.s, 1)
        expect(c.io.carryOut, 1)
        // 1 + 1 + 0 = 2
        poke(c.io.b, 0)
        expect(c.io.s, 0)
        expect(c.io.carryOut, 1)
      }
    }
    assert(testResult, "failed")
  }

  test("BinaryMealy module correctness test") {
    val nStates = 3
    val s0 = 2
    def stateTransision(state: Int, in: Boolean): Int = {
      if (in) {
        1
      } else {
        0
      }
    }
    def output(state: Int, in: Boolean): Int = {
      if (state == 2) {
        0
      } else if ((state == 1 && !in) || (state == 0 && in)) {
        1
      } else {
        0
      }
    }
    val testParams = BinaryMealyParams(nStates, s0, stateTransision, output)

    val testResult = Driver(() => new BinaryMealy(testParams)) {
      c => new PeekPokeTester(c) {
        poke(c.io.in, false)
        expect(c.io.out, 0)
        step(1)
        poke(c.io.in, false)
        expect(c.io.out, 0)
        step(1)
        poke(c.io.in, false)
        expect(c.io.out, 0)
        step(1)
        poke(c.io.in, true)
        expect(c.io.out, 1)
        step(1)
        poke(c.io.in, true)
        expect(c.io.out, 0)
        step(1)
        poke(c.io.in, false)
        expect(c.io.out, 1)
        step(1)
        poke(c.io.in, true)
        expect(c.io.out, 1)
        step(1)
        poke(c.io.in, false)
        expect(c.io.out, 1)
        step(1)
        poke(c.io.in, true)
        expect(c.io.out, 1)
      }
    }
    assert(testResult, "failed")
  }
}
