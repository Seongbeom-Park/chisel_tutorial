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
}
