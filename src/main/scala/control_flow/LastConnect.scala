package control_flow

import chisel3._

class LastConnect extends Module {
  val io = IO(new Bundle {
    val in = Input(UInt(4.W))
    val out = Output(UInt(4.W))
  })

  io.out := 1.U
  io.out := 2.U
  io.out := 3.U
  io.out := 4.U
}
