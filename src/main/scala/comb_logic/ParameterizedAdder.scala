package comb_logic

import chisel3._

class ParameterizedAdder(saturate: Boolean) extends Module {
  val io = IO(new Bundle {
    val in_a = Input(UInt(4.W))
    val in_b = Input(UInt(4.W))
    val out = Output(UInt(4.W))
  })

  val sum = io.in_a +& io.in_b
  io.out := sum
  if(saturate) {
    when (sum > 15.U) {
      io.out := 15.U
    }
  }
}
