import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * @author x-zq
 */

public class SimpleServerHander implements Runnable{

  private Socket socket;

  SimpleServerHander(Socket socket) {
    this.socket = socket;
  }

  @Override
  public void run() {

    BufferedReader reader = null;
    BufferedReader br = null;
    PrintWriter out = null;
    InputStream in = null;
    try {
      reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      String header = reader.readLine();
      String filePath = SimpleServer.getBaseFilePath() + header.split(" ")[1];

      out = new PrintWriter(socket.getOutputStream());

      if (filePath.endsWith(".jpg") || filePath.endsWith(".ico")) {
        in = new FileInputStream(filePath);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        int i;
        while ((i = in.read()) != -1) {
          baos.write(i);
        }

        byte[] bytes = baos.toByteArray();

        out.println("HTTP/1.1 200 OK");
        out.println("Server: Java");
        out.println("Content-Type: image/jpeg");
        out.println("Content-Length: " + bytes.length);
        out.println("");
        out.flush();
        //如果不加flush，那么可能在println没有结束写入时，就开始write字节。导致错误
        socket.getOutputStream().write(bytes, 0, bytes.length);
      } else {
        br = new BufferedReader(new InputStreamReader(new FileInputStream(filePath)));
        out = new PrintWriter(socket.getOutputStream());

        out.println("HTTP/1.1 200 OK");
        out.println("Server: Java");
        out.println("Content-Type: text/html; charset=UTF-8");
        out.println("");

        String line;
        while ((line = br.readLine()) != null) {
          out.println(line);
        }
        out.flush();
      }


    } catch (Exception e) {
      e.printStackTrace();
      if (out != null) {
        out.println("HTTP/1.1 500");
        out.println(" ");
        out.flush();
      }
    }finally {
      close(br, in, reader, out, socket);
    }
  }

  private static void close(Closeable... closeables) {
    for (Closeable closeable : closeables) {
      try {
        if (closeable != null) {
          closeable.close();
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
}
