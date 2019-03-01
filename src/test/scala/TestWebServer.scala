import java.io.{BufferedReader, BufferedWriter, ByteArrayInputStream, ByteArrayOutputStream}
import java.net._

import org.scalatest._
import org.mockito.Mockito._
import org.scalatest.mockito.MockitoSugar

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

        byteArrayOutputStream.toString() should be("This is a test")

        verify(socket).close()
    }

    "Read and write" should "echo" in {
        val in = mock[BufferedReader]
        val out = mock[BufferedWriter]

        when(in.readLine()).thenReturn("This is a test")

        WebServer.read_and_write(in, out)

        verify(out).write("This is a test")
        verify(out).flush()
        verify(out).close()
        verify(in).close()
    }
}