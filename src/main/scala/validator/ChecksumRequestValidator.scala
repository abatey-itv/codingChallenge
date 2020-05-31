package validator

import java.io.File

import validator.MD5Checker.calculateChecksum

import scala.util.Try

object ChecksumRequestValidator {

  final case class Request(file: File, expectedChecksum: String)

  sealed trait Result

  final case class Valid(file: File, checksum: String) extends Result

  final case class ChecksumMismatch(file: File, expectedChecksum: String, actualChecksum: String) extends Result

  final case class RequestError(file: File, expectedChecksum: String) extends Result

  def validate(requests: Seq[Request]): Seq[Result] = requests.map(req => validateChecksum(req))

  private def validateChecksum(request: Request): Result = {
    Try({
      val actualChecksum = calculateChecksum(request.file.getAbsolutePath)
      if (actualChecksum == request.expectedChecksum)
        Valid(request.file, actualChecksum)
      else
        ChecksumMismatch(request.file, request.expectedChecksum, actualChecksum)
    }).getOrElse(RequestError(request.file, request.expectedChecksum))
  }

}
