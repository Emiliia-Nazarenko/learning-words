package db;

import javax.persistence.*;

@Entity
@Table(name = "words", schema = "public", catalog = "Words")
public class WordsEntity extends WordEntity {

    public WordsEntity(String w, String t) {
        super(w, t);
    }

    public WordsEntity() {}
}

