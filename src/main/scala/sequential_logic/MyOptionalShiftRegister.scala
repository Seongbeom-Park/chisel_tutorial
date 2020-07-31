package sequential_logic

import chisel3._
import chisel3.util._

class MyOptionalShiftRegister(val n: Int, val init: BigInt = 1) extends Module {
  val io = IO(new Bundle {
    val en = Input(Bool())
    val in = Input(Bool())
    val out = Output(UInt(n.W))
  })

  val state = RegInit(init.U(n.W))

  state := Cat(state, io.in)
  io.out := state
}
