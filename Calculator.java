import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import javax.swing.JOptionPane;
import java.util.ArrayList;
import java.lang.Math; 
import java.util.Date; 
import java.time.*;

public class Calculator {
    
    private String accounts_path;
    private String log_path;

    private Person[] persons;
    private Person person_paid;
    private Person[] persons_in;

    private int value_total;
    private int value_each;

    public Calculator(String accounts_path, String log_path) {
        this.accounts_path = accounts_path;
        this.log_path = log_path;
    }
    
    public void calculate() {
        while(true) {
            persons = getPersons();
            value_total = getValuePaid();

            person_paid = getPersonPaid();
            persons_in = getPersonsIn();

            value_each = costForPerson();

            makeTransaction();

            savePersons();
            saveTransaction();
        }
    }

    private Person getPersonPaid() {
        int index = findPersonPaidIndex();
        return persons[index];
    }

    private Person[] getPersonsIn() {
        int[] indexes = getWhoShouldPayIndexes();
        Person[] persons_in = new Person[indexes.length];
        for (int i = 0; i < indexes.length; i++) {
            persons_in[indexes[i]] = persons_in[i];
        }
        return persons_in;
    }

    private void makeTransaction() {
        for (int i = 0; i < persons_in.length; i++) {
            persons_in[i].Subtract(value_each);
        }
        person_paid.Add(value_each * persons_in.length);
    }

    private void saveTransaction() {
        try {
            FileWriter fileOpener = new FileWriter(Paths.get(log_path).toString(), true);
            BufferedWriter writer = new BufferedWriter(fileOpener);
            
            Date date = new Date();
            LocalDate localDate = date.toInstant().atZone(ZoneId.of("Iran")).toLocalDate();
            
            writer.write("\n" + localDate + "\t" + value_total + "\r\n");
            writer.write(person_paid.name + "\t" +  "+ " + value_each * persons_in.length + "\r\n");
            for (int i = 0; i < persons_in.length; i++) {
                Person p = persons_in[i];
                writer.write(p.name + "\t" + "- " + value_each + "\r\n");
            }
            writer.write("\n===============================================================\n");
            writer.close();
            fileOpener.close();
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    private void savePersons() {
        try {
            FileWriter fileOpener = new FileWriter(Paths.get(accounts_path).toString());
            BufferedWriter writer = new BufferedWriter(fileOpener);
            
            for(int i = 0; i < persons.length; i++) {
                Person p = persons[i];
                writer.write(p.name + "\t" + p.money + "\r\n");
            }
            writer.close();
            fileOpener.close();
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    private int costForPerson() {
        return (int) Math.ceil((value_total / (persons_in.length + 1)) / 100f) * 100;
    }

    private int[] getWhoShouldPayIndexes() {
        String[] indexes_string = JOptionPane.showInputDialog(null, "who should paid?" + getPersonsList()).split(" ");
        int[] indexes = new int[indexes_string.length];
        for (int i = 0; i < indexes_string.length; i++) {
            indexes[i] = Integer.parseInt(indexes_string[i]);
        }
        return indexes;
    }

    private int findPersonPaidIndex() {
        return Integer.parseInt(JOptionPane.showInputDialog(null, "who paid?" + getPersonsList()));
    }

    private String getPersonsList() {
        String message = "";
        for(int i = 0; i < persons.length; i++)
            message += "\n " + i + ". " + persons[i].name;
        return message;
    }

    private int getValuePaid() {
        return Integer.parseInt(JOptionPane.showInputDialog(null, "total value paid?"));
    }

    private Person[] getPersons() {
        ArrayList<Person> persons = new ArrayList<Person>();

        try {
            FileReader fileOpener = new FileReader(Paths.get(this.accounts_path).toString());
            BufferedReader reader = new BufferedReader(fileOpener);
            while(true){
                String line = reader.readLine();
                if(line == null)
                    break;
                String[] data = line.split("\\t");
                persons.add(new Person(data[0], Integer.parseInt(data[1])));
            }
            reader.close();
            fileOpener.close();
        } catch (IOException e) {
            System.out.println(e);
        }
        Person[] persons_array = new Person[persons.size()];
        for (int i = 0; i < persons.size(); i++) {
            persons_array[i] = (Person) persons.get(i);
        }
        return persons_array;
    }


}