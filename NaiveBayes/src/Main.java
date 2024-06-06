import java.io.IOException;
import java.util.Scanner;

public class Main {

    private static NaiveBayes naiveBayes;

    public static void main(String[] args) {
        start();
    }

    public static void start() {
        try {
            Dataset();
        } catch (IOException e) {
            System.out.println("Files were not found");
            return;
        }
        Menu();
    }

    private static void Dataset() throws IOException {
        String train_set = "agaricus-lepiota.data";
        String test_set = "agaricus-lepiota.test.data";
        naiveBayes = new NaiveBayes(train_set, test_set);
        naiveBayes.train();
    }

    private static void Menu() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("1. Get accuracy and everything that you asked for");
            System.out.println("2. Classify case");

            switch (scanner.nextInt()) {
                case 1:
                    naiveBayes.test();
                    naiveBayes.getAccuracyAndSomeOtherStuff();
                    break;
                case 2:
                    System.out.println("Classification: " + naiveBayes.classify());
                    break;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }
}
