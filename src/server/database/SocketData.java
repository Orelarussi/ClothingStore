package server.database;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class SocketData implements Closeable {
    private Socket socket;
    private BufferedReader inputStream;
    private PrintWriter outputStream;
    private String clientAddress;

    public SocketData(Socket socket) throws IOException {
        this.socket = socket;
        this.inputStream = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
        this.outputStream = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8), true);
        this.clientAddress = socket.getInetAddress() + ":" + socket.getPort();
    }

    public Socket getSocket() {
        return socket;
    }

    public BufferedReader getInputStream() {
        return inputStream;
    }

    public PrintWriter getOutputStream() {
        return outputStream;
    }

    public String getClientAddress() {
        return clientAddress;
    }

    /**
     * closes socket , inputStream, outputStream
     * @throws IOException
     */
    @Override
    public void close() throws IOException {
        socket.close();
        inputStream.close();
        outputStream.close();
    }
}