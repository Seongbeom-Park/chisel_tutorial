package exercise

import org.scalatest.FunSuite

import chisel3.iotesters.{Driver, PeekPokeTester}

class ExerciseTester extends FunSuite {
  test("My4ElementFir module correctness test (0,0,0,0)") {
    val testResult = Driver(() => new My4ElementFir(0, 0, 0, 0)) {
      c => new PeekPokeTester(c) {
        poke(c.io.in, 0)
        expect(c.io.out, 0)
        step(1)
        poke(c.io.in, 4)
        expect(c.io.out, 0)
        step(1)
        poke(c.io.in, 5)
        expect(c.io.out, 0)
        step(1)
        poke(c.io.in, 2)
        expect(c.io.out, 0)
        step(1)
      }
    }
    assert(testResult, "failed")
  }

  test("My4ElementFir module correctness test (1,1,1,1)") {
    val testResult = Driver(() => new My4ElementFir(1, 1, 1, 1)) {
      c => new PeekPokeTester(c) {
        poke(c.io.in, 1)
        expect(c.io.out, 1) // 1, 0, 0, 0
        step(1)
        poke(c.io.in, 4)
        expect(c.io.out, 5) // 4, 1, 0, 0
        step(1)
        poke(c.io.in, 3)
        expect(c.io.out, 8) // 3, 4, 1, 0
        step(1)
        poke(c.io.in, 2)
        expect(c.io.out, 10) // 2, 3, 4, 1
        step(1)
        poke(c.io.in, 7)
        expect(c.io.out, 16) // 7, 2, 3, 4
        step(1)
        poke(c.io.in, 0)
        expect(c.io.out, 12) // 0, 7, 2, 3
        step(1)
      }
    }
    assert(testResult, "failed")
  }

  test("My4ElementFir module correctness test (1,2,3,4)") {
    val testResult = Driver(() => new My4ElementFir(1, 2, 3, 4)) {
      c => new PeekPokeTester(c) {
        poke(c.io.in, 1)
        expect(c.io.out, 1) // 1*1, 0*2, 0*3, 0*4
        step(1)
        poke(c.io.in, 4)
        expect(c.io.out, 6) // 4*1, 1*2, 0*3, 0*4
        step(1)
        poke(c.io.in, 3)
        expect(c.io.out, 14) // 3*1, 4*2, 1*3, 0*4
        step(1)
        poke(c.io.in, 2)
        expect(c.io.out, 24) // 2*1, 3*2, 4*3, 1*4
        step(1)
        poke(c.io.in, 7)
        expect(c.io.out, 36) // 7*1, 2*2, 3*3, 4*4
        step(1)
        poke(c.io.in, 0)
        expect(c.io.out, 32) // 0*1, 7*2, 2*3, 3*4
        step(1)
      }
    }
    assert(testResult, "failed")
  }

  test("MyManyDynamicElementVecFir module correctness test (0,0,0,0)") {
    val testResult = Driver(() => new MyManyDynamicElementVecFir(4)) {
      c => new PeekPokeTester(c) {
        poke(c.io.valid, 1)
        poke(c.io.consts(0), 0)
        poke(c.io.consts(1), 0)
        poke(c.io.consts(2), 0)
        poke(c.io.consts(3), 0)
        poke(c.io.in, 0)
        expect(c.io.out, 0)
        step(1)
        poke(c.io.in, 4)
        expect(c.io.out, 0)
        step(1)
        poke(c.io.in, 5)
        expect(c.io.out, 0)
        step(1)
        poke(c.io.in, 2)
        expect(c.io.out, 0)
        step(1)
      }
    }
    assert(testResult, "failed")
  }

  test("MyManyDynamicElementVecFir module correctness test (1,1,1,1)") {
    val testResult = Driver(() => new MyManyDynamicElementVecFir(4)) {
      c => new PeekPokeTester(c) {
        poke(c.io.valid, 1)
        poke(c.io.consts(0), 1)
        poke(c.io.consts(1), 1)
        poke(c.io.consts(2), 1)
        poke(c.io.consts(3), 1)
        poke(c.io.in, 1)
        expect(c.io.out, 1) // 1, 0, 0, 0
        step(1)
        poke(c.io.in, 4)
        expect(c.io.out, 5) // 4, 1, 0, 0
        step(1)
        poke(c.io.in, 3)
        expect(c.io.out, 8) // 3, 4, 1, 0
        step(1)
        poke(c.io.in, 2)
        expect(c.io.out, 10) // 2, 3, 4, 1
        step(1)
        poke(c.io.in, 7)
        expect(c.io.out, 16) // 7, 2, 3, 4
        step(1)
        poke(c.io.in, 0)
        expect(c.io.out, 12) // 0, 7, 2, 3
        step(1)
      }
    }
    assert(testResult, "failed")
  }

  test("MyManyDynamicElementVecFir module correctness test (1,2,3,4)") {
    val testResult = Driver(() => new MyManyDynamicElementVecFir(4)) {
      c => new PeekPokeTester(c) {
        poke(c.io.valid, 1)
        poke(c.io.consts(0), 1)
        poke(c.io.consts(1), 2)
        poke(c.io.consts(2), 3)
        poke(c.io.consts(3), 4)
        poke(c.io.in, 1)
        expect(c.io.out, 1) // 1*1, 0*2, 0*3, 0*4
        step(1)
        poke(c.io.in, 4)
        expect(c.io.out, 6) // 4*1, 1*2, 0*3, 0*4
        step(1)
        poke(c.io.in, 3)
        expect(c.io.out, 14) // 3*1, 4*2, 1*3, 0*4
        step(1)
        poke(c.io.in, 2)
        expect(c.io.out, 24) // 2*1, 3*2, 4*3, 1*4
        step(1)
        poke(c.io.in, 7)
        expect(c.io.out, 36) // 7*1, 2*2, 3*3, 4*4
        step(1)
        poke(c.io.in, 0)
        expect(c.io.out, 32) // 0*1, 7*2, 2*3, 3*4
        step(1)
      }
    }
    assert(testResult, "failed")
  }
}
