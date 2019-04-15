public class Main {

    public static void main(String[] args) {
        System.out.println("Hello World!");
        new Thread(new SimpleServer(8080,"E:\\ideaworkspace\\SimpleStaticServer\\static")).start();
    }
}
