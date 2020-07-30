package control_flow

import chisel3._
import chisel3.util.Enum

class GradLife extends Module {
  val io = IO(new Bundle{
    val state = Input(UInt(2.W))
    val coffee = Input(Bool())
    val idea = Input(Bool())
    val pressure = Input(Bool())
    val nextState = Output(UInt(2.W))
  })

  val idle :: coding :: writing :: grad :: Nil = Enum(4)

  io.nextState := idle
  when (io.state === idle) {
    when (io.coffee === true.B) {
      io.nextState := coding
    }.elsewhen (io.idea === true.B) {
      io.nextState := idle
    }.elsewhen (io.pressure === true.B) {
      io.nextState := writing
    }
  }.elsewhen (io.state === coding) {
    when (io.coffee === true.B) {
      io.nextState := coding
    }.elsewhen (io.idea === true.B) {
      io.nextState := writing
    }.elsewhen (io.pressure === true.B) {
      io.nextState := writing
    }
  }.elsewhen (io.state === writing) {
    when (io.coffee === true.B) {
      io.nextState := writing
    }.elsewhen (io.idea === true.B) {
      io.nextState := writing
    }.elsewhen (io.pressure === true.B) {
      io.nextState := grad
    }
  }
}
