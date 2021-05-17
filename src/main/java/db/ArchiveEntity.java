package db;

import javax.persistence.*;

@Entity
@Table(name = "archive", schema = "public", catalog = "Words")
public class ArchiveEntity extends WordEntity {

    public ArchiveEntity(WordEntity w) {
        super(w.getWord(), w.getTranslation());
    }

    public ArchiveEntity() {}
}
