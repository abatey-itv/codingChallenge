package validator

import java.io.{File, FileInputStream}
import java.security.{DigestInputStream, MessageDigest}

object MD5Checker {

  def calculateChecksum(path: String): String = {
    val buffer = new Array[Byte](8192)
    val md5 = MessageDigest.getInstance("MD5")

    val dis = new DigestInputStream(new FileInputStream(new File(path)), md5)
    try { while (dis.read(buffer) != -1) {} } finally { dis.close() }

    md5.digest.map("%02x".format(_)).mkString
  }

}
