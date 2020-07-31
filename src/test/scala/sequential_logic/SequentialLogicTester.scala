package sequential_logic

import org.scalatest.FunSuite

import chisel3.iotesters.{Driver, PeekPokeTester}

class SequentialLogicTester extends FunSuite {
  test("RegisterModule module correctness test") {
    class RegisterModuleTester(c: RegisterModule) extends PeekPokeTester(c) {
      for(i <- 0 to 100) {
        poke(c.io.in, i)
        step(1)
        expect(c.io.out, i+1)
      }
    }
    val testResult = Driver(() => new RegisterModule()) {
      c => new RegisterModuleTester(c)
    }
    assert(testResult, "failed")
  }

  test("RegNextModule module correctness test") {
    val testResult = Driver(() => new RegNextModule()) {
      c => new PeekPokeTester(c) {
        for(i <- 0 to 100) {
          poke(c.io.in, i)
          step(1)
          expect(c.io.out, i+1)
        }
      }
    }
    assert(testResult, "failed")
  }

  test("RegInitModule module correctness test") {
    val testResult = Driver(() => new RegInitModule()) {
      c => new PeekPokeTester(c) {
        for(i <- 0 to 100) {
          poke(c.io.in, i)
          step(1)
          expect(c.io.out, i+1)
        }
      }
    }
    assert(testResult, "failed")
  }

  test("FindMax module correctness test") {
    val testResult = Driver(() => new FindMax()) {
      c => new PeekPokeTester(c) {
        expect(c.io.max, 0)
        poke(c.io.in, 1)
        step(1)
        expect(c.io.max, 1)
        poke(c.io.in, 3)
        step(1)
        expect(c.io.max, 3)
        poke(c.io.in, 2)
        step(1)
        expect(c.io.max, 3)
        poke(c.io.in, 24)
        step(1)
        expect(c.io.max, 24)
      }
    }
    assert(testResult, "failed")
  }

  test("Comb module correctness test") {
    val testResult = Driver(() => new Comb()) {
      c => new PeekPokeTester(c) {
        poke(c.io.in, 0)
        for(i <- 1 to 100) {
          step(1)
          poke(c.io.in, i)
          expect(c.io.out, 1)
        }
      }
    }
    assert(testResult, "failed")
  }

  test("MyShiftRegister module correctness test") {
    val testResult = Driver(() => new MyShiftRegister()) {
      c => new PeekPokeTester(c) {
        var state = c.init
        for (i <- 0 until 10) {
          // poke in LSB of i (i % 2)
          poke(c.io.in, i % 2)
          // update expected state
          state = ((state * 2) + (i % 2)) & 0xf
          step(1)
          expect(c.io.out, state)
        }
      }
    }
    assert(testResult, "failed")
  }

  test("MyOptionalShiftRegister module correctness test") {
    for(i <- Seq(3, 4, 8, 24, 65)) {
      val testResult = Driver(() => new MyOptionalShiftRegister(n = i)) {
        c => new PeekPokeTester(c) {
          val inSeq = Seq(0, 1, 1, 1, 0, 1, 1, 0, 0, 1)
          var state = c.init
          var i = 0
          poke(c.io.en, 1)
          while (i < 10 * c.n) {
            // poke in repeated inSeq
            val toPoke = inSeq(i % inSeq.length)
            poke(c.io.in, toPoke)
            // update expected state
            state = ((state * 2) + toPoke) & BigInt("1"*c.n, 2)
            step(1)
            expect(c.io.out, state)

            i += 1
          }
        }
      }
      assert(testResult, "failed")
    }
  }
}
