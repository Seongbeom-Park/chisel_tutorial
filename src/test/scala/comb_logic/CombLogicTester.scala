package comb_logic

import org.scalatest.FunSuite

import chisel3._
import chisel3.iotesters.{Driver, PeekPokeTester}

import scala.util.Random
import scala.math.min

class CombLogicTester extends FunSuite {
  test ("CombLogic module correctness test") {
    val testResult = Driver(() => new CombLogic()) {
      c => new PeekPokeTester(c) {
        val cycles = 100
        for (i <- 0 until cycles) {
          val in_a = Random.nextInt(16)
          val in_b = Random.nextInt(16)
          val in_c = Random.nextInt(16)
          poke(c.io.in_a, in_a)
          poke(c.io.in_b, in_b)
          poke(c.io.in_c, in_c)
          expect(c.io.out, in_a*in_b+in_c)
        }
      }
    }
    assert(testResult, "failed")
  }

  test ("Arbiter module correctness test") {
    val testResult = Driver(() => new Arbiter()) {
      c => new PeekPokeTester(c) {
        val data = Random.nextInt(65536)
        poke(c.io.fifo_data, data)
        for (i <- 0 until 8) {
          poke(c.io.fifo_valid, (i>>0)%2)
          poke(c.io.pe0_ready, (i>>1)%2)
          poke(c.io.pe1_ready, (i>>2)%2)

          expect(c.io.fifo_ready, i>1)
          expect(c.io.pe0_valid, i==3 || i==7)
          expect(c.io.pe1_valid, i==5)

          if (i==3 || i==7) {
            expect(c.io.pe0_data, data)
          }else if (i==5) {
            expect(c.io.pe1_data, data)
          }
        }
      }
    }
    assert(testResult, "failed")
  }

  test ("ParameterizedAdder module correctness test") {
    for (saturate <- Seq(true, false)) {
      val testResult = Driver(() => new ParameterizedAdder(saturate)) {
        c => new PeekPokeTester(c) {
          val cycles = 100
          for (i <- 0 until cycles) {
            val in_a = Random.nextInt(16)
            val in_b = Random.nextInt(16)
            poke(c.io.in_a, in_a)
            poke(c.io.in_b, in_b)
            if (saturate) {
              expect(c.io.out, min(in_a+in_b, 15))
            }else {
              expect(c.io.out, (in_a+in_b)%16)
            }
          }
        }
      }
      assert(testResult, "failed")
    }
  }
}
