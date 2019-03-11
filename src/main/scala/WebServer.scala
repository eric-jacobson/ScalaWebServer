import java.net._
import java.io._
import scala.io._

object WebServer {

    def main(args: Array[String]): Unit = {
        if(args.length == 0){
            println("To run web server: sbt run <fileName.html>")
            System.exit(1)
        }
        val server = new ServerSocket(9999)
        val file = new File("./" + args(0))
        println("Server Running...\nGo to: http://localhost:9999")
        while(true) {
            serve(server, file)
        }
    }

    def serverResponse(in: String, out:BufferedWriter, file: File): Unit = {
        val responseArray = in.split(" ")
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

    def read_and_write(in: BufferedReader, out:BufferedWriter, file: File): Unit = {
        val content = in.readLine()
        val responseArray = content.split(" ")

        if (responseArray(0).equals("GET") && responseArray(1).equals("/")) {
            //println(s"${responseArray(0)} ${responseArray(1)} ${responseArray(2)}\r\n")
            serverResponse(content, out, file)
        }
        else {
            println("Something has gone very wrong!")
            System.exit(1)
        }

        out.flush()
        in.close()
        out.close()
    }

    def serve(server: ServerSocket, file: File): Unit = {
        val s = server.accept()
        val in = new BufferedReader(new InputStreamReader(s.getInputStream))
        val out = new BufferedWriter(new OutputStreamWriter(s.getOutputStream))
        val filePath = file

        read_and_write(in, out, filePath)

        s.close()
    }
}