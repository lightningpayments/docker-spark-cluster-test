package de.lightningpayments.app.iteration

import zio.{Has, Task}

import scala.util.Random

trait RandomNumberEnv {
  val randomMathGen: Task[Double] = Task(Random.nextDouble())
  val randomNIntGen: Int => Task[Int] = n => Task(Random.nextInt(n))
}

object RandomNumberEnv {
  type HasRandom = Has[RandomNumberEnv]
}
