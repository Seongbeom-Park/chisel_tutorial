package exercise

import chisel3._

class MyManyDynamicElementVecFir(val length: Int) extends Module {
  val io = IO(new Bundle {
    val in = Input(UInt(8.W))
    val valid = Input(Bool())
    val out = Output(UInt(8.W))
    val consts = Input(Vec(length, UInt(8.W)))
  })

  val taps = Seq(io.in) ++ Seq.fill(length - 1)(RegInit(UInt(8.W), 0.U))
  taps.zip(taps.tail).foreach {
    case (a, b) => when (io.valid) { b := a }
  }

  io.out := taps.zip(io.consts).map {
    case (a, b) => a * b
  }.reduce(_ + _)
}
