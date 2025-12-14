import java.io.Serializable;
import java.util.Date;

public class Person implements Serializable 
{
    String firstName, lastName;
    Date birthDate;

    public String toString() { return firstName + " " + lastName; }
}
