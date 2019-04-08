import java.net._
import java.io._

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.io._

object WebServer {

    def main(args: Array[String]): Unit = {
        val server = new ServerSocket(9999)
        println("Server Running...\nGo to: http://localhost:9999")
        while (true) {
            val s = server.accept()
            Future {
                serve(s)
            }
        }
    }

    def serverResponse(in: String, out: BufferedWriter): Unit = {
        val responseArray = in.split(" ")
        val filePath = new File("." + s"${responseArray(1)}" + ".html")

        println(s"${responseArray(0)} ${responseArray(1)} ${responseArray(2)}\r\n")

        if (responseArray(1).equals("/")) {
            val file = Source.fromFile("index.html")
            out.write(s"${responseArray(2)} 200\r\n")
            out.write("Content-Type=text/html\r\n")
            out.write("\r\n")
            out.write(file.mkString)
        } else if (!filePath.exists()) {
            val file = Source.fromFile("404.html")
            out.write(s"${responseArray(2)} 404\r\n")
            out.write("Content-Type=text/html\r\n")
            out.write("\r\n")
            out.write(file.mkString)
        } else {
            val file = Source.fromFile("." + s"${responseArray(1)}" + ".html")
            out.write(s"${responseArray(2)} 200\r\n")
            out.write("Content-Type=text/html\r\n")
            out.write("\r\n")
            out.write(file.mkString)
        }
    }

    def read_and_write(in: BufferedReader, out: BufferedWriter): Unit = {
        val content = in.readLine()
        serverResponse(content, out)
        out.flush()
        in.close()
        out.close()
    }

    def serve(server: Socket): Unit = {
        val in = new BufferedReader(new InputStreamReader(server.getInputStream))
        val out = new BufferedWriter(new OutputStreamWriter(server.getOutputStream))
        read_and_write(in, out)
        server.close()
    }
}