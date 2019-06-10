package pl.twitch.chatbot;


import java.io.IOException;

public class ChatThread implements Runnable  {
    private IRCConnect ircConnect = null;
    private IRCChat ircChat = null;
    private String streamer = null;
    private Boolean countingMessages;
    private DatabaseConnect db = null;

    ChatThread(String streamer, Boolean countingMessages, DatabaseConnect db, IRCConnect ircConnect) {
        this.streamer = streamer;
        this.countingMessages = countingMessages;
        this.db = db;
        this.ircConnect = ircConnect;
    }

    @Override
    public void run() {
//            System.out.println("START");
//            ircConnect = new IRCConnect(streamer);
            IRCChat ircChat = new IRCChat(ircConnect, Thread.currentThread(), db);
            try {
                ircChat.showChat(countingMessages);
            } catch (IOException e) {
                e.printStackTrace();
            }
//            System.out.println("FINITO");
    }
    public void closeIrcConnect() {
        ircConnect.closeSocket();
    }
}
