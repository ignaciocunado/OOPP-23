package commons;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;


@Entity
public final class Task {

    @Id @GeneratedValue
    private int id;

}
