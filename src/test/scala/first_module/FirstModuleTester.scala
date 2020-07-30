package first_module

import org.scalatest.FunSuite

import chisel3.iotesters.{Driver, PeekPokeTester}
import chisel3.stage.ChiselStage

class FirstModuleTester extends FunSuite {
  test ("Passthrough module correctness test") {
    val testResult = Driver(() => new Passthrough(4)) {
      c => new PeekPokeTester(c) {
        poke(c.io.in, 0)
        expect(c.io.out, 0)
        poke(c.io.in, 1)
        expect(c.io.out, 1)
        poke(c.io.in, 2)
        expect(c.io.out, 2)
      }
    }
    assert(testResult, "fail")
  }
  test ("Passthrough verilog test") {
    val stage = new ChiselStage
    stage.emitVerilog(new Passthrough(10))
  }
  test ("Passthrough firrtl test") {
    val stage = new ChiselStage
    stage.emitFirrtl(new Passthrough(10))
  }
}
