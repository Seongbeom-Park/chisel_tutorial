package exercise

import chisel3._

class My4ElementFir(b0: Int, b1: Int, b2: Int, b3: Int) extends Module {
  val io = IO(new Bundle {
    val in = Input(UInt(8.W))
    val out = Output(UInt(8.W))
  })

  val x0 = Wire(UInt(8.W))
  val x1 = RegInit(UInt(8.W), 0.U)
  val x2 = RegInit(UInt(8.W), 0.U)
  val x3 = RegInit(UInt(8.W), 0.U)

  x3 := x2
  x2 := x1
  x1 := x0
  x0 := io.in

  io.out := b0.U * x0 + b1.U * x1 + b2.U * x2 + b3.U * x3
}
