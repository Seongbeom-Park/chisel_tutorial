package parameters

import chisel3._
sealed trait Verbosity

class ParameterizedWidthAdder2(in0Width: Int, in1Width: Int, sumWidth: Int)(implicit verbosity: Verbosity)
extends Module {
  implicit case object Silent extends Verbosity
  case object Verbose extends Verbosity

  def log(msg: => String): Unit = verbosity match {
    case Silent =>
    case Verbose => println(msg)
  }
  require(in0Width >= 0)
  log(s"in0Width of $in0Width OK")
  require(in1Width >= 0)
  log(s"in1Width of $in0Width OK")
  require(sumWidth >= 0)
  log(s"sumWidth of $in0Width OK")
  val io = IO(new Bundle{
    val in0 = Input(UInt(in0Width.W))
    val in1 = Input(UInt(in1Width.W))
    val sum = Output(UInt(sumWidth.W))
  })
  log("Made IO")
  io.sum := io.in0 + io.in1
  log("Assigned output")
}
