package control_flow

import chisel3._

class Polynomial extends Module {
  val io = IO(new Bundle {
    val select = Input(UInt(2.W))
    val x = Input(SInt(32.W))
    val fOfX = Output(SInt(32.W))
  })

  val square = Wire(SInt(33.W))
  square := io.x*io.x

  val first = Wire(SInt(33.W))
  val second = Wire(SInt(33.W))
  val third = Wire(SInt(33.W))
  when (io.select === 0.U) {
    first := square
    second := -2.S*io.x
    third := 1.S
  }.elsewhen (io.select === 1.U) {
    first := 2.S*square
    second := 6.S*io.x
    third := 3.S
  }.otherwise {
    first := 4.S*square
    second := -10.S*io.x
    third := -5.S
  }

  io.fOfX := first + second + third
}

