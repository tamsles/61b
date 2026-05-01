public class Dessert {
    public int price;
    public int flavour;
    public static int num = 0;
    public Dessert(int price, int flavour) {
        this.price = price;
        this.flavour = flavour;
        num ++;
    }

    public void printDessert() {
        System.out.println(this.price + " " + this.flavour + " " + num);
    }

    public static void main(String[] args) {
        System.out.println("I love dessert");
    }




}
