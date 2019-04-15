import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor.DiscardPolicy;
import java.util.concurrent.TimeUnit;

/**
 * @author x-zq
 */

public class SimpleServer implements Runnable {

  private static int port = 8080;

  private static String baseFilePath = "";

  private static ServerSocket serverSocket;

  private ThreadPoolExecutor threadPool = new ThreadPoolExecutor(1, 10, 1000, TimeUnit.SECONDS, new LinkedBlockingDeque<>(), new DiscardPolicy());


  public SimpleServer(int port, String baseFilePath) {
    if (port > 0) {
      SimpleServer.port = port;
    }

    if (baseFilePath != null && new File(baseFilePath).exists() && new File(baseFilePath)
        .isDirectory()) {
      SimpleServer.baseFilePath = baseFilePath;
    }
  }
  @Override
  public void run() {
    try {
      serverSocket = new ServerSocket(port);
      Socket socket;
      while ((socket = serverSocket.accept()) != null) {
        threadPool.execute(new SimpleServerHander(socket));
      }
      serverSocket.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static int getPort() {
    return port;
  }

  public static void setPort(int port) {
    SimpleServer.port = port;
  }

  static String getBaseFilePath() {
    return baseFilePath;
  }

  public static void setBaseFilePath(String baseFilePath) {
    SimpleServer.baseFilePath = baseFilePath;
  }

  public static ServerSocket getServerSocket() {
    return serverSocket;
  }

  public static void setServerSocket(ServerSocket serverSocket) {
    SimpleServer.serverSocket = serverSocket;
  }

}
