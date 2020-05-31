package validator

import java.io.File

import org.scalatest.{FlatSpec, Matchers}
import validator.ChecksumRequestValidator.{ChecksumMismatch, RequestError, Valid}
import validator.ChecksumsResultFormatter.CsvReport

class ChecksumsResultFormatterSpec extends FlatSpec with Matchers {
  private val expectedHeader = "file name,PASSED/FAILED/ERROR,expected checksum,actual checksum"

  "generateCsvReport" should "generate empty report if input is empty" in {
    // when
    val report = ChecksumsResultFormatter.generateCsvReport(Seq())

    report shouldEqual CsvReport(Seq(expectedHeader))
  }

  "generateCsvReport" should "format one valid result" in {
    // given
    val result = Valid(new File("/blah/video.mp4"), "114b769462fa209f914dd6872883e170")

    // when
    val report = ChecksumsResultFormatter.generateCsvReport(Seq(result))

    // then
    val expectedRow = "video.mp4,PASSED,114b769462fa209f914dd6872883e170,114b769462fa209f914dd6872883e170"
    report shouldEqual CsvReport(Seq(expectedHeader, expectedRow))
  }

  "generateCsvReport" should "format several valid results" in {
    // given
    val result1 = Valid(new File("/blah/video1.mp4"), "114b769462fa209f914dd6872883e170")
    val result2 = Valid(new File("/blah/video2.mp4"), "114b769462fa209f914dd6872883e171")
    val result3 = Valid(new File("/blah/video3.mp4"), "114b769462fa209f914dd6872883e172")

    // when
    val report = ChecksumsResultFormatter.generateCsvReport(Seq(result1 ,result2, result3))

    // then
    val expectedRow1 = "video1.mp4,PASSED,114b769462fa209f914dd6872883e170,114b769462fa209f914dd6872883e170"
    val expectedRow2 = "video2.mp4,PASSED,114b769462fa209f914dd6872883e171,114b769462fa209f914dd6872883e171"
    val expectedRow3 = "video3.mp4,PASSED,114b769462fa209f914dd6872883e172,114b769462fa209f914dd6872883e172"
    report shouldEqual CsvReport(Seq(expectedHeader, expectedRow1, expectedRow2, expectedRow3))
  }

  "generateCsvReport" should "format one invalid result" in {
    // given
    val result = ChecksumMismatch(new File("/blah/video.mp4"), "114b769462fa209f914dd6872883e170", "114b769462fa209f914dd6872883e171")

    // when
    val report = ChecksumsResultFormatter.generateCsvReport(Seq(result))

    // then
    val expectedRow = "video.mp4,FAILED,114b769462fa209f914dd6872883e170,114b769462fa209f914dd6872883e171"
    report shouldEqual CsvReport(Seq(expectedHeader, expectedRow))
  }

  "generateCsvReport" should "format one error result" in {
    // given
    val result = RequestError(new File("/blah/video.mp4"), "114b769462fa209f914dd6872883e170")

    // when
    val report = ChecksumsResultFormatter.generateCsvReport(Seq(result))

    // then
    val expectedRow = "video.mp4,ERROR,114b769462fa209f914dd6872883e170,"
    report shouldEqual CsvReport(Seq(expectedHeader, expectedRow))
  }

}
