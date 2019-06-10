package pl.twitch.chatbot;

import java.io.*;
import java.net.Socket;

public class IRCConnect {

    private BufferedReader reader;
    private BufferedWriter writer;
    private Socket socket;
    private String server = "irc.twitch.tv";
    private String login = "rudiszubertbot";
    private String pass = "oauth:r0di566vfx80izz4wusorza4f1sg80";
    private String channel = null;
    private String nick = "rudiszubertbot";

    IRCConnect(String channel) {
        this.channel = "#" + channel;
        try {
            socket = new Socket(server, 6667);
            writer = new BufferedWriter(
                    new OutputStreamWriter(socket.getOutputStream( )));
            reader = new BufferedReader(
                    new InputStreamReader(socket.getInputStream( )));

            writer.write("pass " + pass + "\r\n");
            writer.write("nick " + nick + "\r\n");
            writer.write("join " + this.channel + "\r\n");
//            writer.write("CAP REQ :twitch.tv/membership\r\n");
//            writer.write("CAP REQ :twitch.tv/commands\r\n");
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public BufferedReader getReader() {
        return reader;
    }

    public void setReader(BufferedReader reader) {
        this.reader = reader;
    }
    public BufferedWriter getWriter() {
        return writer;
    }

    public void setWriter(BufferedWriter writer) {
        this.writer = writer;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public void closeSocket() {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
