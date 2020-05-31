package validator

import validator.ChecksumRequestValidator.{ChecksumMismatch, RequestError, Result, Valid}

object ChecksumsResultFormatter {

  final case class CsvReport(lines: Seq[String])

  def generateCsvReport(results: Seq[Result]): CsvReport = {
    val header = "file name,PASSED/FAILED/ERROR,expected checksum,actual checksum"
    val lines = results.map(r => format(r))
    CsvReport(List(header) ++ lines)
  }

  private def format(result: Result): String = result match {
    case Valid(file, checksum) => s"${file.getName},PASSED,$checksum,$checksum"
    case ChecksumMismatch(file, expected, actual) => s"${file.getName},FAILED,$expected,$actual"
    case RequestError(file, expectedChecksum) => s"${file.getName},ERROR,${expectedChecksum},"
  }

}
