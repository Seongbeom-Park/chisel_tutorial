package parameters

import chisel3._

// TODO
class DelayBy1(resetValue: Option[Int] = None) extends Module {
  val io = IO(new Bundle {
    val in = Input(UInt(16.W))
    val out = Output(UInt(16.W))
  })

  val reg = RegInit(UInt(16.W), resetValue.getOrElse(0).U)
  reg := io.in
  io.out := reg
}
