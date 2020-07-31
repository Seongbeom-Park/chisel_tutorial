package sequential_logic

import chisel3._

class Comb extends Module {
  val io = IO(new Bundle {
    val in = Input(SInt(12.W))
    val out = Output(SInt(12.W))
  })

  val delay = RegInit(SInt(12.W), 0.S)
  delay := io.in
  io.out := io.in - delay
}
