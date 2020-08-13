package parameters

import chisel3._

case class BinaryMealyParams(
  nStates: Int,
  s0: Int,
  stateTransition: (Int, Boolean) => Int,
  output: (Int, Boolean) => Int
) {
  require(nStates >= 0)
  require(s0 < nStates && s0 >= 0)
}

class BinaryMealy(val mp: BinaryMealyParams) extends Module {
  val io = IO(new Bundle {
    val in = Input(Bool())
    val out = Output(UInt())
  })

  val state = RegInit(UInt(), mp.s0.U)

  io.out := 0.U
  for (s <- 0 until mp.nStates) {
    for (b <- Vector(true, false)) {
      when (state === s.U) {
        when (io.in === b.B) {
          state := mp.stateTransition(s, b).U
          io.out := mp.output(s, b).U
        }
      }
    }
  }
}
