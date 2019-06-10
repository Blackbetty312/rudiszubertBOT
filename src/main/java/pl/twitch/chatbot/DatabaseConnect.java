package pl.twitch.chatbot;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.io.IOException;
import java.util.Properties;

public class DatabaseConnect {
    private SessionFactory sessions = null;

    private Session session = null;
    private Configuration cfg = null;
    private Properties p = null;

    DatabaseConnect() {

        cfg = new Configuration();
        p = new Properties();
//load properties file
        try {
            p.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("hibernate.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        cfg.setProperties(p);
        cfg.addAnnotatedClass(topchatters.class);
// build session factory
        sessions = cfg.buildSessionFactory();

    }
    public void startSession() {
        session = sessions.openSession();
    }
    public void closeSession() {
        session.close();
    }

    public Session getSession() {
        return session;
    }

    public SessionFactory getSessions() {
        return sessions;
    }
}
