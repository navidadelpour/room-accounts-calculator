import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.swing.JOptionPane;
import java.util.ArrayList;
import java.lang.Math; 
import java.util.Date; 
import java.time.*;

public class Calculator {
    
    public String file_path = "Accounts.txt";
    public String log_path = "Log.txt";
    public String accounts = null;
    public ArrayList<Person> persons = new ArrayList<Person>();
    
    public int value_total;
    public int value_each;
    
    public Calculator() {

        while(true) {
            persons = new ArrayList<Person>();
            getPersons();

            value_total = getValuePaid();

            Person person_paid = persons.get(findPersonPaidIndex());

            int[] indexes = getWhoShouldPayIndexes();
            int num_persons = indexes.length;

            value_each = costForPerson(num_persons);

            for (int i = 0; i < num_persons; i++) {
                persons.get(indexes[i]).Subtract(value_each);
            }
            person_paid.Add(value_each * num_persons);

            savePersons();
            saveTransaction(person_paid, indexes, num_persons);
        }
    }

    private void saveTransaction(Person person_paid, int[] indexes, int num_persons) {
        try {
            FileWriter fileOpener = new FileWriter(Paths.get(log_path).toString(), true);
            BufferedWriter writer = new BufferedWriter(fileOpener);
            
            Date date = new Date();
            LocalDate localDate = date.toInstant().atZone(ZoneId.of("Iran")).toLocalDate();
            int year  = localDate.getYear();
            int month = localDate.getMonthValue();
            int day   = localDate.getDayOfMonth();
            
            writer.write("\n" + localDate + "\t" + value_total + "\r\n");
            writer.write(person_paid.name + "\t" +  "+ " + value_each * num_persons + "\r\n");
            for (int i = 0; i < num_persons; i++) {
                Person p = persons.get(indexes[i]);
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
            FileWriter fileOpener = new FileWriter(Paths.get(file_path).toString());
            BufferedWriter writer = new BufferedWriter(fileOpener);
            
            for(int i = 0; i < persons.size(); i++) {
                Person p = persons.get(i);
                writer.write(p.name + "\t" + p.money + "\r\n");
            }
            writer.close();
            fileOpener.close();
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    private int costForPerson(int num) {
        return (int) Math.ceil((value_total / (num + 1)) / 100f) * 100;
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
        for(int i = 0; i < persons.size(); i++)
            message += "\n " + i + ". " + persons.get(i).name;
        return message;
    }

    private int getValuePaid() {
        return Integer.parseInt(JOptionPane.showInputDialog(null, "total value paid?"));
    }

    private void getPersons() {
        try {
            FileReader fileOpener = new FileReader(Paths.get(this.file_path).toString());
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
    }


}