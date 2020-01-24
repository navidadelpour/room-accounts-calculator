public class Person {
    public String name;
    public int money;

    public Person(String name, int money) {
        this.name = name;
        this.money = money;
    }

    public void Add(int money) {
        this.money += money;
    }

    public void Subtract(int money) {
        this.money -= money;
    }
}