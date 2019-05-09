import java.net._
import java.io.{FileNotFoundException, _}

import scala.io._

import akka.actor.{Actor, ActorRef, ActorSystem, Props}

object WebServer {

    import Server._

    val system: ActorSystem = ActorSystem("WebServer")
    val myServer: ActorRef = system.actorOf(Server.props)

    def main(args: Array[String]): Unit = {
        val server = new ServerSocket(9999)
        println("Server Running...\nGo to: http://localhost:9999")
        while (true) {
            val s = server.accept()
            myServer ! MySocket(s)
        }
    }
}

object Server {
    def props: Props = Props[Server]

    final case class MySocket(socket: Socket)
}

class Server extends Actor {

    import Server._

    def receive: PartialFunction[Any, Unit] = {
        case MySocket(s) =>
            val in = new BufferedReader(new InputStreamReader(s.getInputStream))
            val out = new BufferedWriter(new OutputStreamWriter(s.getOutputStream))
            readAndWrite(in, out)
            s.close()
    }

    def readAndWrite(in: BufferedReader, out: BufferedWriter): Unit = {
        val content = in.readLine()
        requestServerResponse(content, out)
        out.write(content)
        out.flush()
        in.close()
        out.close()
    }

    def requestServerResponse(in: String, out: BufferedWriter): Unit = {
        val responseArray = in.split(" ")
        println(s"${responseArray(0)} ${responseArray(1)} ${responseArray(2)}\r\n")

        if (responseArray(1).equals("/")) {
            val file = Source.fromFile("index.html")
            out.write(s"${responseArray(2)} 200\r\n")
            out.write("Content-Type=text/html\r\n")
            out.write("\r\n")
            out.write(file.mkString)
        } else {
            try {
                val file = Source.fromFile("." + s"${responseArray(1)}" + ".html")
                out.write(s"${responseArray(2)} 200\r\n")
                out.write("Content-Type=text/html\r\n")
                out.write("\r\n")
                out.write(file.mkString)
            } catch {
                case _: FileNotFoundException =>
                    val file = Source.fromFile("404.html")
                    out.write(s"${responseArray(2)} 404\r\n")
                    out.write("Content-Type=text/html\r\n")
                    out.write("\r\n")
                    out.write(file.mkString)
            }
        }
    }
}
