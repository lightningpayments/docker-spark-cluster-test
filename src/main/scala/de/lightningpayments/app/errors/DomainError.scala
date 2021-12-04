package de.lightningpayments.app.errors

import akka.http.interop.ErrorResponse
import akka.http.scaladsl.model.{HttpResponse, StatusCodes}
import play.api.Logging

sealed trait DomainError

object DomainError extends Logging {

  final case class FatalError(t: Throwable) extends DomainError

  final case object BadData                 extends DomainError

  implicit val domainErrorResponse: ErrorResponse[DomainError] = {
    case FatalError(t) =>
      logger.error("fatal error", t)
      HttpResponse(StatusCodes.InternalServerError)
    case BadData =>
      HttpResponse(StatusCodes.BadRequest)
  }

}
