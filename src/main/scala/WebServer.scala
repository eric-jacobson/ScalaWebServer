import java.net._
import java.io._
import scala.io._

object WebServer {

    def main(args: Array[String]): Unit = {
        val server = new ServerSocket(9999)
        println("Server Running...\nGo to: http://localhost:9999")
        while(true) {
            serve(server)
        }
    }

    def read_and_write(in: BufferedReader, out: BufferedWriter): Unit = {
        println("File name: ")

        val fileName = in.readLine()
        val file = new File("./" + fileName)

        try{
            println("HTTP/1.1 200")
            println("Content-Type: text/plain")
            println("Connection: close")
            println("\r\n")

            out.write(Source.fromFile(fileName).mkString)
        } catch {
            case fileNotFound: Exception =>
                println("HTTP/1.1 404")
                out.write(Source.fromFile("./404.html").mkString)

        }

        out.flush()
        in.close()
        out.close()
    }

    def serve(server: ServerSocket): Unit = {
        val s = server.accept()
        val in = new BufferedReader(new InputStreamReader(s.getInputStream))
        val out = new BufferedWriter(new OutputStreamWriter(s.getOutputStream))

        read_and_write(in, out)

        s.close()
    }
}

