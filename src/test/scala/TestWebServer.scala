import java.io._
import java.net._

import akka.actor.ActorRef

import scala.io._
import org.scalatest._
import org.mockito.Mockito._
import org.scalatest.mockito.MockitoSugar

import scala.io.Source

class TestWebServer extends FlatSpec with Matchers with MockitoSugar {

    "ServerResponse root file" should "be 200 Ok" in {
        val serverSocket = mock[ServerSocket]
        val socket = mock[Socket]
        val actorRef = mock[ActorRef]
        val byteArrayInputStream = new ByteArrayInputStream("GET / HTTP/1.1".getBytes())
        val byteArrayOutputStream = new ByteArrayOutputStream()

        when(serverSocket.accept()).thenReturn(socket)
        when(socket.getInputStream).thenReturn(byteArrayInputStream)
        when(socket.getOutputStream).thenReturn(byteArrayOutputStream)



        byteArrayOutputStream.toString() should be("HTTP/1.1 200\r\n" + "Content-Type=text/html\r\n" + "\r\n"
                                                    + Source.fromFile("./index.html").mkString)

        verify(socket).close()
    }

    "ServerResponse valid file" should "be 200 Ok" in {
        val serverSocket = mock[ServerSocket]
        val socket = mock[Socket]
        val byteArrayInputStream = new ByteArrayInputStream("GET /helloworld HTTP/1.1".getBytes())
        val byteArrayOutputStream = new ByteArrayOutputStream()

        when(serverSocket.accept()).thenReturn(socket)
        when(socket.getInputStream).thenReturn(byteArrayInputStream)
        when(socket.getOutputStream).thenReturn(byteArrayOutputStream)

        //WebServer.serve(socket)

        byteArrayOutputStream.toString() should be("HTTP/1.1 200\r\n" + "Content-Type=text/html\r\n" + "\r\n"
                                                    + Source.fromFile("./helloworld.html").mkString)

        verify(socket).close()
    }

    "ServerResponse invalid file" should "be 404 Not Found" in {
        val serverSocket = mock[ServerSocket]
        val socket = mock[Socket]
        val byteArrayInputStream = new ByteArrayInputStream("GET /abc HTTP/1.1".getBytes())
        val byteArrayOutputStream = new ByteArrayOutputStream()

        when(serverSocket.accept()).thenReturn(socket)
        when(socket.getInputStream).thenReturn(byteArrayInputStream)
        when(socket.getOutputStream).thenReturn(byteArrayOutputStream)

        //WebServer.serve(socket)

        byteArrayOutputStream.toString() should be("HTTP/1.1 404\r\n" + "Content-Type=text/html\r\n" + "\r\n"
                                                    + Source.fromFile("./404.html").mkString)

        verify(socket).close()
    }
}