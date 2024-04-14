package scalauv

import scala.scalanative.posix.netinet.in
import scala.scalanative.posix.arpa.inet
import scala.scalanative.unsigned.*
import scala.scalanative.unsafe.*
import scala.scalanative.posix.netinet.inOps.*

object Net {

  private val LocalHost4: in.in_addr_t = inet.htonl(0x7f000001.toUInt)

  def setLocalHost4(addr: Ptr[in.in_addr]): Unit = {
    addr.in_addr = LocalHost4
  }

  def setLocalHostSocket4(socketAddress: Ptr[in.sockaddr_in]): Unit = {
    socketAddress.sin_addr.s_addr = LocalHost4
  }

  def setLocalHost6(addr: Ptr[in.in6_addr]): Unit = {
    addr.s6_addr(15) = 0x01.toUByte;
  }

  def setLocalHostSocket6(socketAddress: Ptr[in.sockaddr_in6]): Unit = {
    socketAddress.sin6_addr._1(15) = 0x01.toUByte;
  }

}
