import java.io.{BufferedReader, BufferedWriter, ByteArrayInputStream, ByteArrayOutputStream}
import java.net._

import org.scalatest._
import org.mockito.Mockito._
import org.scalatest.mockito.MockitoSugar

import scala.io.Source

class TestWebServer extends FlatSpec with Matchers with MockitoSugar {

    "Bytes in" should "be bytes out" in {
        val serverSocket = mock[ServerSocket]
        val socket = mock[Socket]
        val byteArrayInputStream = new ByteArrayInputStream("This is a test".getBytes())
        val byteArrayOutputStream = new ByteArrayOutputStream()

        when(serverSocket.accept()).thenReturn(socket)
        when(socket.getInputStream).thenReturn(byteArrayInputStream)
        when(socket.getOutputStream).thenReturn(byteArrayOutputStream)

        WebServer.serve(serverSocket)

        byteArrayOutputStream.toString() should be(Source.fromFile("./404.html").mkString)

        verify(socket).close()
    }

    "read_and_write invalid file" should "load 404" in {
        val in = mock[BufferedReader]
        val out = mock[BufferedWriter]

        when(in.readLine()).thenReturn("hello.html")

        WebServer.read_and_write(in, out)

        verify(out).write(Source.fromFile("./404.html").mkString)
        verify(out).flush()
        verify(out).close()
        verify(in).close()
    }

    "read_and_write valid file" should "load page" in {
        val in = mock[BufferedReader]
        val out = mock[BufferedWriter]

        when(in.readLine()).thenReturn("index.html")

        WebServer.read_and_write(in, out)

        verify(out).write(Source.fromFile("./index.html").mkString)
        verify(out).flush()
        verify(out).close()
        verify(in).close()
    }
}