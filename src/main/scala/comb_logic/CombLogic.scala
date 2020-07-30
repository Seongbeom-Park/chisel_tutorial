package comb_logic

import chisel3._

class CombLogic extends Module {
  val io = IO(new Bundle {
    val in_a = Input(UInt(4.W))
    val in_b = Input(UInt(4.W))
    val in_c = Input(UInt(4.W))
    val out = Output(UInt(8.W))
  })

  io.out := io.in_a*io.in_b+io.in_c
}
