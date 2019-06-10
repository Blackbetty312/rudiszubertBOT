package pl.twitch.chatbot;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import javax.xml.crypto.Data;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class IRCChat {
    private IRCConnect ircConnect;
    private List<String> chatters = new ArrayList<>();
    private String streamer;
    private Thread thread = null;
    DatabaseConnect db = null;
    Session session = null;

    IRCChat(IRCConnect ircConnect, Thread thread, DatabaseConnect db) {
        this.ircConnect = ircConnect;
        this.streamer = this.ircConnect.getChannel().substring(1);
        this.thread = thread;
        this.db = db;
    }

    public void showChat(boolean countingMessages) throws IOException {
        String line = null;
        String[] msg = null;
        int t = 0;

        while ((line = ircConnect.getReader().readLine()) != null) {
            if (thread.isInterrupted()) {
                System.out.println("ZAMYKANIE NIEPUSTY");
                return;
            }
            if (line.length() == 0) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                if (line.startsWith("PING ")) {
                    ircConnect.getWriter().write("PONG tmi.twitch.tv\r\n");
                    ircConnect.getWriter().flush();
                }
//            System.out.println(line);
                if ((msg = parse(ircConnect.getChannel(), line)) != null) {
                line = String.join(": ", msg);
//                System.out.println(msg[0] + ": " + msg[1]);
//                System.out.println("[" + streamer + "]" + line);
                    if (countingMessages) {
//                    chatters.add(msg[0]);
//                    t++;
//                    System.out.println(t);
//                    if (t % 1 == 0 && t != 0) {
//                        System.out.println(thread.isInterrupted());
//                        updateCoins(chatters);
                        session = db.getSessions().openSession();
                        Transaction tx = null;
                        try {
                            System.out.println("[" + streamer + "] " + line);
                            tx = session.beginTransaction();
                            updateCoins(msg[0]);
//                            session.load(...);
//                            session.persist(...);
                            tx.commit(); // Flush happens automatically
                        }
                        catch (RuntimeException e) {
                            tx.rollback();
                            throw e; // or display error message
                        }
                        finally {
                            session.close();
                        }
                    }
                }
            }
        }
    }

    public String[] parse(String channel, String line) {
        String nickname = null;
        String message = null;
        String result[] = null;
        Pattern pattern = Pattern.compile(":([a-zA-Z0-9_]{4,25})!");
        Matcher matcher = pattern.matcher(line);
        Pattern pattern2 = Pattern.compile("(PRIVMSG #([a-zA-Z0-9_]{4,25}) :)(.+)");
        Matcher matcher2 = pattern2.matcher(line);
        if (matcher.find())
        {
            nickname = matcher.group(1);
        }
        if (matcher2.find())
        {
            message = matcher2.group(3);
        }
        if(nickname != null && message != null) {
            result = new String[]{nickname, message};
        } else {
            result = null;
        }
        return result;

    }

    public void updateCoins(String nickname) {
//        db.startSession();
//        Transaction tx = null;
        topchatters user = new topchatters();
        String hql = "FROM topchatters E WHERE E.nickname = :name AND E.streamer = :streamer";
//        Query query = db.getSession().createQuery(hql);
        Query query = session.createQuery(hql);
        query.setParameter("name", nickname);
        query.setParameter("streamer", streamer);
        List results = query.list();
        if (results.size() == 0) {
            user.setNickname(nickname);
            user.setMsgs(Long.valueOf(1));
            user.setStreamer(streamer);
//            db.getSession().save(user);
            session.save(user);
        } else {
            updateMsgs(nickname, Long.valueOf(1));
        }
    }


    public void updateMsgs(String name, Long msgs) {
        String hql = "UPDATE topchatters set msgs = msgs + :msgs "  +
                "WHERE nickname = :name AND streamer = :streamer";
//        Query query = db.getSession().createQuery(hql);
        Query query = session.createQuery(hql);
        query.setParameter("name", name);
        query.setParameter("msgs", msgs);
        query.setParameter("streamer", streamer);
        query.executeUpdate();
    }

}
