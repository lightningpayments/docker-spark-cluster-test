package de.lightningpayments.app.calculate

import zio.Task

import scala.util.Random

trait RandomNumberEnv {
  val randomMathGen: Task[Double] = Task(Random.nextDouble())
  val randomNIntGen: Int => Task[Int] = n => Task(Random.nextInt(n))
}
