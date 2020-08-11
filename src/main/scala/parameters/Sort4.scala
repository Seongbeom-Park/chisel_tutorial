package parameters

import chisel3._

class Sort4(ascending: Boolean) extends Module {
  val io = IO(new Bundle {
    val in0 = Input(UInt(16.W))
    val in1 = Input(UInt(16.W))
    val in2 = Input(UInt(16.W))
    val in3 = Input(UInt(16.W))
    val out0 = Output(UInt(16.W))
    val out1 = Output(UInt(16.W))
    val out2 = Output(UInt(16.W))
    val out3 = Output(UInt(16.W))
  })

  val row10 = Wire(UInt(16.W))
  val row11 = Wire(UInt(16.W))
  val row12 = Wire(UInt(16.W))
  val row13 = Wire(UInt(16.W))

  when ((io.in0 < io.in1) === ascending.B) {
    row10 := io.in0
    row11 := io.in1
  }.otherwise {
    row10 := io.in1
    row11 := io.in0
  }

  when ((io.in2 < io.in3) === ascending.B) {
    row12 := io.in2
    row13 := io.in3
  }.otherwise {
    row12 := io.in3
    row13 := io.in2
  }
  
  val row20 = Wire(UInt(16.W))
  val row21 = Wire(UInt(16.W))
  val row22 = Wire(UInt(16.W))
  val row23 = Wire(UInt(16.W))

  when ((row11 < row12) === ascending.B) {
    row21 := row11
    row22 := row12
  }.otherwise {
    row21 := row12
    row22 := row11
  }

  when ((row10 < row13) === ascending.B) {
    row20 := row10
    row23 := row13
  }.otherwise {
    row20 := row13
    row23 := row10
  }

  when ((row20 < row21) === ascending.B) {
    io.out0 := row20
    io.out1 := row21
  }.otherwise {
    io.out0 := row21
    io.out1 := row20
  }
  
  when ((row22 < row23) === ascending.B) {
    io.out2 := row22
    io.out3 := row23
  }.otherwise {
    io.out2 := row23
    io.out3 := row22
  }
}
