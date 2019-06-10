package pl.twitch.chatbot;


import javax.persistence.*;

@Entity
public class topchatters {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(name = "nickname")
    private String nickname;
    @Column(name = "messages")
    private Long msgs;
    @Column(name = "streamer")
    private String streamer;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Long getMsgs() {
        return msgs;
    }

    public void setMsgs(Long msgs) {
        this.msgs = msgs;
    }

    public String getStreamer() {
        return streamer;
    }

    public void setStreamer(String streamer) {
        this.streamer = streamer;
    }
}
