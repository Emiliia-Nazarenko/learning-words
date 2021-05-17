package db;

import javax.persistence.*;
import java.util.Objects;

@MappedSuperclass
public abstract class  WordEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    private Integer id;
    private String word;
    private String translation;
    private Integer attempts_w;
    private Integer attempts_t;

    public WordEntity(String word, String translation) {
        this.word = word;
        this.translation = translation;
        this.attempts_w = 0;
        this.attempts_t = 0;
    }

    public WordEntity(){}

    @Column(name = "id",unique=true, nullable = false)
    public int getId(){
        return this.id;
    }

    @Basic
    @Column(name = "word", nullable = false, length = -1, insertable = true, updatable = true)
    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    @Basic
    @Column(name = "translation", nullable = false, length = -1, insertable = true, updatable = true)
    public String getTranslation() {
        return translation;
    }

    public void setTranslation(String translation) {
        this.translation = translation;
    }

    @Basic
    @Column(name = "attempts_w")
    public Integer getAttempts_w() {
        return attempts_w;
    }

    public void setAttempts_w(Integer a){
        this.attempts_w = a;
    }

    public void increaseAttempts_w() {
        this.setAttempts_w(this.getAttempts_w() + 1);
    }

    public void decreaseAttempts_w() {
        this.setAttempts_w(this.getAttempts_w()  - 1);
    }

    @Basic
    @Column(name = "attempts_t")
    public Integer getAttempts_t() {
        return attempts_t;
    }

    public void setAttempts_t(Integer a) {
        this.attempts_t = a;
    }
    public void increaseAttempts_t() {
        this.setAttempts_t(this.getAttempts_t() + 1);
    }

    public void decreaseAttempts_t() {
        this.setAttempts_t(getAttempts_t()  - 1);
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        WordEntity that = (WordEntity) o;
        return Objects.equals(word, that.word) && Objects.equals(translation, that.translation) && Objects.equals(attempts_w, that.attempts_w) && Objects.equals(attempts_t, that.attempts_t);
    }

    @Override
    public int hashCode() {
        return Objects.hash(word, translation, attempts_w, attempts_t);
    }

    @Override
    public String toString() {
        return "WordEntity{" +
                "id=" + id +
                ", word='" + word + '\'' +
                ", translation='" + translation + '\'' +
                ", attempts_w=" + attempts_w +
                ", attempts_t=" + attempts_t +
                '}';
    }
}
