package parameters

import chisel3._

class ParameterizedScalaObject(param1: Int, param2: String) {
  println(s"I have parameters: param1 = $param1 and param2 = $param2")
}
