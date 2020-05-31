package validator

import java.io.File

import org.scalatest.{FlatSpec, Matchers}
import validator.ChecksumRequestValidator.{ChecksumMismatch, Request, RequestError, Valid}

class ChecksumsRequestValidatorSpec extends FlatSpec with Matchers {

  "ChecksumsRequestValidator" should "return empty results list if request list is empty" in {
    val result = ChecksumRequestValidator.validate(List.empty);
    result shouldEqual List.empty
  }

  "ChecksumsRequestValidator" should "return error if File doesnt exist" in {
    val requestFile = new File("cat.mp4")
    val request = Request(requestFile, "114b769462fa209f914dd6872883e170")

    val result = ChecksumRequestValidator.validate(List(request));
    val expectedResult = RequestError(request.file, request.expectedChecksum)
    result shouldEqual List(expectedResult)
  }

  "ChecksumsRequestValidator" should "validate a single successful request" in {
    // given
    val requestFile = new File("src/test/resources/video4.mp4")
    val request = Request(requestFile, "114b769462fa209f914dd6872883e170")

    // when
    val result = ChecksumRequestValidator.validate(List(request))

    //then
    result shouldEqual List(Valid(requestFile, "114b769462fa209f914dd6872883e170"))
  }

  "ChecksumsRequestValidator" should "return ChecksumMismatch for a single unsuccessful request" in {
    val request = Request(new File("src/test/resources/video5.mp4"), "d2aa249b4bb07e646cc9a90d29a98826")

    // when
    val result = ChecksumRequestValidator.validate(List(request))

    //then
    val mismatch = ChecksumMismatch(new File("src/test/resources/video5.mp4"), "d2aa249b4bb07e646cc9a90d29a98826", "d2aa249b4bb07e646cc9a90d29a98825")
    result shouldEqual List(mismatch)
  }

  "ChecksumsRequestValidator" should "validate three successful requests" in {
    val request1 = Request(new File("src/test/resources/video4.mp4"), "114b769462fa209f914dd6872883e170")
    val request2 = Request(new File("src/test/resources/video5.mp4"), "d2aa249b4bb07e646cc9a90d29a98825")
    val request3 = Request(new File("src/test/resources/video6.mp4"), "1526614416ec96b8a8e9817c9c680490")

    // when
    val result = ChecksumRequestValidator.validate(List(request1, request2, request3))

    //then
    val result1 = Valid(request1.file, "114b769462fa209f914dd6872883e170")
    val result2 = Valid(request2.file, "d2aa249b4bb07e646cc9a90d29a98825")
    val result3 = Valid(request3.file, "1526614416ec96b8a8e9817c9c680490")
    result shouldEqual List(result1, result2, result3)
  }

  "ChecksumsRequestValidator" should "validate three requests where one is invalid" in {
    val request1 = Request(new File("src/test/resources/video4.mp4"), "114b769462fa209f914dd6872883e170")
    val request2 = Request(new File("src/test/resources/video5.mp4"), "d2aa249b4bb07e646cc9a90d29a98826")
    val request3 = Request(new File("src/test/resources/video6.mp4"), "1526614416ec96b8a8e9817c9c680490")

    // when
    val result = ChecksumRequestValidator.validate(List(request1, request2, request3))

    //then
    val result1 = Valid(request1.file, "114b769462fa209f914dd6872883e170")
    val result2 = ChecksumMismatch(request2.file, "d2aa249b4bb07e646cc9a90d29a98826", "d2aa249b4bb07e646cc9a90d29a98825")
    val result3 = Valid(request3.file, "1526614416ec96b8a8e9817c9c680490")
    result shouldEqual List(result1, result2, result3)
  }
}

