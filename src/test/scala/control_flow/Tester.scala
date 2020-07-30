package control_flow

import org.scalatest.FunSuite

import chisel3.iotesters.{Driver, PeekPokeTester}

class ControlFlowTester extends FunSuite {
  test ("LastConnect module correctness test") {
    val testResult = Driver(() => new LastConnect()) {
      c => new PeekPokeTester(c) {
        expect(c.io.out, 4)
      }
    }
    assert(testResult, "failed")
  }

  test ("Max3 Module correctness test") {
    val testResult = Driver(() => new Max3()) {
      c => new PeekPokeTester(c) {
        poke(c.io.in1, 6)
        poke(c.io.in2, 4)  
        poke(c.io.in3, 2)  
        expect(c.io.out, 6)  // input 1 should be biggest
        poke(c.io.in2, 7)  
        expect(c.io.out, 7)  // now input 2 is
        poke(c.io.in3, 11)  
        expect(c.io.out, 11) // and now input 3
        poke(c.io.in3, 3)  
        expect(c.io.out, 7)  // show that decreasing an input works as well
        poke(c.io.in1, 9)
        poke(c.io.in2, 9)
        poke(c.io.in3, 6)
        expect(c.io.out, 9)  // still get max with tie
      }
    }
    assert(testResult, "failed")
  }

  test ("Sort4 module correctness test") {
    val testResult = Driver(() => new Sort4()) {
      c => new PeekPokeTester(c) {
        poke(c.io.in0, 3)
        poke(c.io.in1, 6)
        poke(c.io.in2, 9)
        poke(c.io.in3, 12)
        expect(c.io.out0, 3)
        expect(c.io.out1, 6)
        expect(c.io.out2, 9)
        expect(c.io.out3, 12)

        poke(c.io.in0, 13)
        poke(c.io.in1, 4)
        poke(c.io.in2, 6)
        poke(c.io.in3, 1)
        expect(c.io.out0, 1)
        expect(c.io.out1, 4)
        expect(c.io.out2, 6)
        expect(c.io.out3, 13)

        poke(c.io.in0, 13)
        poke(c.io.in1, 6)
        poke(c.io.in2, 4)
        poke(c.io.in3, 1)
        expect(c.io.out0, 1)
        expect(c.io.out1, 4)
        expect(c.io.out2, 6)
        expect(c.io.out3, 13)
      }
    }
    assert(testResult, "failed")
  }

  test("Sort4 module correctness exhaustive test") {
    val testResult = Driver(() => new Sort4()) {
      c => new PeekPokeTester(c) {
        List(1, 2, 3, 4).permutations.foreach {
          case i0 :: i1 :: i2 :: i3 :: Nil => 
            println(s"Sorting $i0 $i1 $i2 $i3")
            poke(c.io.in0, i0)
            poke(c.io.in1, i1)
            poke(c.io.in2, i2)
            poke(c.io.in3, i3)
            expect(c.io.out0, 1)
            expect(c.io.out1, 2)
            expect(c.io.out2, 3)
            expect(c.io.out3, 4)
          case _ =>
        }
      }
    }
    assert(testResult, "failed")
  }

  test ("Polynomial module correctness test") {
    val testResult = Driver(() => new Polynomial()) {
      c => new PeekPokeTester(c) {
        for (x <- 0 to 20) {
          for (select <- 0 to 2) {
            poke(c.io.select, select)
            poke(c.io.x, x)
            def poly(select:Int, x:Int):Int = {
              (if (select == 0) {
                x*x - 2*x + 1
              }else if (select == 1){
                2*x*x + 6*x + 3
              }else {
                4*x*x - 10*x - 5
              }).asInstanceOf[Int]
            }
            expect(c.io.fOfX, poly(select, x))
          }
        }
      }
    }
    assert(testResult, "failed")
  }

  def states = Map("idle" -> 0, "coding" -> 1, "writing" -> 2, "grad" -> 3)
  def gradLife (state:Int, coffee:Boolean, idea:Boolean, pressure:Boolean): Int = {
    var nextState = states("idle")
    if (state == states("idle")) {
      if (coffee) {
        nextState = states("coding")
      }else if (idea) {
        nextState = states("idle")
      }else if (pressure) {
        nextState = states("writing")
      }else {
        nextState = states("idle")
      }
    }else if (state == states("coding")) {
      if (coffee) {
        nextState = states("coding")
      }else if (idea) {
        nextState = states("writing")
      }else if (pressure) {
        nextState = states("writing")
      }else {
        nextState = states("idle")
      }
    }else if (state == states("writing")) {
      if (coffee) {
        nextState = states("writing")
      }else if (idea) {
        nextState = states("writing")
      }else if (pressure) {
        nextState = states("grad")
      }else {
        nextState = states("idle")
      }
    }else if (state == states("grad")) {
      nextState = states("idle")
    }
    nextState
  }
  test ("GradLife function correctness test") {
    (0 until states.size).foreach {
      state => assert(gradLife(state, false, false, false) == states("idle"))
    }
    assert(gradLife(states("writing"), true, false, true) == states("writing"))
    assert(gradLife(states("idle"), true, true, true) == states("coding"))
    assert(gradLife(states("idle"), false, true, true) == states("idle"))
    assert(gradLife(states("grad"), false, false, false) == states("idle"))
  }

  test ("GradLife module correctness test") {
    val testResult = Driver(() => new GradLife()) {
      c => new PeekPokeTester(c) {
        for (state <- 0 to 3) {
          for (coffee <- List(true, false)) {
            for (idea <- List(true, false)) {
              for (pressure <- List(true, false)) {
                poke(c.io.state, state)
                poke(c.io.coffee, coffee)
                poke(c.io.idea, idea)
                poke(c.io.pressure, pressure)
                expect(c.io.nextState, gradLife(state, coffee, idea, pressure))
              }
            }
          }
        }
      }
    }
    assert (testResult, "failed")
  }
}
