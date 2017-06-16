package sh.webserver

object Runner {
  def main(args: Array[String]): Unit = {
    new Server(1337).start()
  }
}