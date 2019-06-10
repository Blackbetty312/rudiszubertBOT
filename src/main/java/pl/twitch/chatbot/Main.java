package pl.twitch.chatbot;


import java.util.*;

public class Main {

    public static void main(String[] args) throws Exception {

        int refreshTimeInMin = 5;
        List<Thread> threads = new ArrayList<>();
        List<ChatThread> chatThreads = new ArrayList<>();
        DatabaseConnect db = new DatabaseConnect();

//        IRCConnect ircConnect = new IRCConnect("overpow");
//        IRCConnect ircConnect2 = new IRCConnect("kubon_");
//        IRCChat ircChat = new IRCChat(ircConnect);
//        IRCChat ircChat2 = new IRCChat(ircConnect2);
//
//        ircChat.showChat(true);
//        ircChat2.showChat(true);

        //TODO  SESSIONFACTORY ONLY ONE PER APP, SESSION FOR SEPARATE THREAD

        while(true) {
            List<String> streamers = ParseJSON.getListOfStreamers(100);
            for(String streamer : streamers) {
                IRCConnect ircConnect = new IRCConnect(streamer);
                chatThreads.add(new ChatThread(streamer, true, db, ircConnect));
            }
            for(ChatThread ct : chatThreads) {
                threads.add(new Thread(ct));

            }
            System.out.println("PROCESY: " + threads.size() + " | STREAMERZY: " + streamers.size());
            for(Thread t : threads) {
                t.setPriority(Thread.MAX_PRIORITY);
                t.start();
            }
            System.out.println("THREADS STARTED");
            Thread.sleep(1000 * 60 * refreshTimeInMin);
            for(Thread t : threads) {
                t.interrupt();
            }
            for(ChatThread ct: chatThreads) {
                ct.closeIrcConnect();
            }
            Thread.sleep(1000);
            System.out.println("THREADS STOPPED, CLOSING CONNECTIONS");

            threads.clear();
            chatThreads.clear();

        }

//        chat2.start();
//        ParseJSON.getListOfStreamers(100);

//            if(line.contains("!witam")) {
//                writer.write("PRIVMSG #blaack_betty :gituwa elo @" + msg[0] + "\r\n");
//                writer.flush();
//            }
//            if(line.contains("!jd")) {
//                writer.write("PRIVMSG #blaack_betty :@" + msg[0] + " Jebiesz disa na " + (random.nextInt(99) + 1) + "%\r\n");
//                writer.flush();
//            }
//            if(line.contains("!result") && msg[0].contains("blaack_betty")) {
//                if(!participants.isEmpty()) {
//                    int randomInt = random.nextInt(participants.size());
//                    System.out.println(randomInt);
//                    String winner = participants.get(randomInt);
//                    writer.write("PRIVMSG #blaack_betty :Zwyciezca raffle @" + winner + "\r\n");
//                    writer.flush();
//                    participants.remove(randomInt);
//                }
//            }
//            if(line.contains("!raffle")) {
//                if(!participants.contains(msg[0])) {
//                    participants.add(msg[0]);
//                }
//            }
//            System.out.println(participants);
    }


}
