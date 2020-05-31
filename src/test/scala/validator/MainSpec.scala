package validator

import org.scalatest._

class MainSpec extends FlatSpec with Matchers {

  private val header = "file name,PASSED/FAILED/ERROR,expected checksum,actual checksum"

  "generate csv report" should "generate report for empty but valid input" in {
    val result = Main.generateCsvReport("src/test/resources/emptyChecksums.csv")
    result shouldEqual List(header)
  }

  "generate csv report" should "generate report for non empty, valid input where two pass and one fails" in {
    val result = Main.generateCsvReport("src/test/resources/testChecksums.csv")

    val expected1 = "video4.mp4,PASSED,114b769462fa209f914dd6872883e170,114b769462fa209f914dd6872883e170"
    val expected2 = "video5.mp4,FAILED,d2aa249b4bb07e646cc9a90d29a98826,d2aa249b4bb07e646cc9a90d29a98825"
    val expected3 = "video6.mp4,PASSED,1526614416ec96b8a8e9817c9c680490,1526614416ec96b8a8e9817c9c680490"
    result shouldEqual List(header, expected1, expected2, expected3)
  }

  "generate csv report" should "generate report where input has one missng video file" in {
    val result = Main.generateCsvReport("src/test/resources/testChecksumsWithOneMissingVideo.csv")

    val expected1 = "video4.mp4,PASSED,114b769462fa209f914dd6872883e170,114b769462fa209f914dd6872883e170"
    val expected2 = "video5.mp4,FAILED,d2aa249b4bb07e646cc9a90d29a98826,d2aa249b4bb07e646cc9a90d29a98825"
    val expected3 = "video10.mp4,ERROR,1526614416ec96b8a8e9817c9c680490,"
    result shouldEqual List(header, expected1, expected2, expected3)
  }

  "generate csv report" should "generate report where input file is missing" in {
    val result = Main.generateCsvReport("src/test/resources/notFound.csv")
    result shouldEqual List("Error generating report: File Not Found: src/test/resources/notFound.csv")
  }

  "generate csv report" should "generate report where input file content is invalid" in {
    val result = Main.generateCsvReport("src/test/resources/invalidFile.csv")
    result shouldEqual List("Error generating report: Invalid File: src/test/resources/invalidFile.csv")
  }

  "generate csv report" should "generate report where input file header is invalid" in {
    val result = Main.generateCsvReport("src/test/resources/invalidHeader.csv")
    result shouldEqual List("Error generating report: Invalid Header: Expected 'file name,expected checksum'")
  }
}
