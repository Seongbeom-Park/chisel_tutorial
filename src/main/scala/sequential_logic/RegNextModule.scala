package sequential_logic

import chisel3._

class RegNextModule extends Module {
  val io = IO(new Bundle {
    val in = Input(UInt(12.W))
    val out = Output(UInt(12.W))
  })

  io.out := RegNext(io.in + 1.U)
}
