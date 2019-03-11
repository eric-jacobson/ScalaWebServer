import java.net._
import java.io._
import scala.io._

object WebServer {

    def main(args: Array[String]): Unit = {
        val server = new ServerSocket(9999)
        println("Server Running...\nGo to: http://localhost:9999")
        while(true) {
            println("Enter file name: ")
            val fileName = StdIn.readLine()
            serve(server, fileName)
        }
    }

    def serverResponse(in: String, out:BufferedWriter, fileName: String): Unit = {
        val responseArray = in.split(" ")
        val file = new File("./" + fileName)
        if(file.exists()){
            out.write(s"${responseArray(2)} 200 Ok\r\n")
            out.write("Content-Type=text/html\r\n")
            out.write("\r\n")
            out.write(Source.fromFile(file).mkString)

        } else {
            out.write(s"${responseArray(2)} 404 Not Found\r\n")
            out.write("Content-Type=text/html\r\n")
            out.write("\r\n")
            out.write(Source.fromFile("./404.html").mkString)
        }
    }

    def read_and_write(in: BufferedReader, out:BufferedWriter, file: String): Unit = {
        val content = in.readLine()
        val responseArray = content.split(" ")
        val fileName = file

        if (responseArray(0).equals("GET") && responseArray(1).equals("/")) {
            //println(s"${responseArray(0)} ${responseArray(1)} ${responseArray(2)}\r\n")
            serverResponse(content, out, fileName)
        }
        else {
            println("Something has gone very wrong!")
            //System.exit(1)
        }

        out.flush()
        in.close()
        out.close()
    }

    def serve(server: ServerSocket, file: String): Unit = {
        val fileName = file
        val s = server.accept()
        val in = new BufferedReader(new InputStreamReader(s.getInputStream))
        val out = new BufferedWriter(new OutputStreamWriter(s.getOutputStream))

        read_and_write(in, out, fileName)

        s.close()
    }
}