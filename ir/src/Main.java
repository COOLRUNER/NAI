import java.io.*;
import java.util.*;

public class Main {
    static List<Iris> loadDataset(String filename) throws IOException {
        List<Iris> dataset = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader(filename));
        String line;
        while ((line = br.readLine()) != null) {
            String[] data = line.split(",");
            double[] features = new double[data.length - 1];
            for (int i = 0; i < data.length - 1; i++) {
                features[i] = Double.parseDouble(data[i]);
            }
            dataset.add(new Iris(features, data[data.length - 1]));
        }
        return dataset;
    }

    static double distance(Iris a, Iris b) {
        double sum = 0;
        for (int i = 0; i < a.fancy_numbers.length; i++) {
            sum += Math.pow(a.fancy_numbers[i] - b.fancy_numbers[i], 2);
        }
        return Math.sqrt(sum);
    }

    static List<Iris> getNeighbors(List<Iris> trainSet, Iris characteristic, int k) {
        List<Iris> neighbors = new ArrayList<>();
        PriorityQueue<Iris> pq = new PriorityQueue<>(Comparator.comparingDouble(i -> distance(i, characteristic)));
        pq.addAll(trainSet);
        for (int i = 0; i < k; i++) {
            neighbors.add(pq.poll());
        }
        return neighbors;
    }

    static String classify(List<Iris> neighbors) {
        Map<String, Integer> classCount = new HashMap<>();
        for (Iris neighbor : neighbors) {
            classCount.put(neighbor.name, classCount.getOrDefault(neighbor.name, 0) + 1);
        }
        return Collections.max(classCount.entrySet(), Comparator.comparingInt(Map.Entry::getValue)).getKey();
    }

    static double getAccuracy(List<Iris> testSet, List<Iris> predictions) {
        int correct = 0;
        for (int i = 0; i < testSet.size(); i++) {
            if (testSet.get(i).name.equals(predictions.get(i).name)) {
                correct++;
            }
        }
        return ((double) correct / testSet.size()) * 100.0;
    }

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter the value for k:");
        int k = scanner.nextInt();

        System.out.println("Enter the path to the main file:");
        String trainSetFile = scanner.next();

        System.out.println("Enter the path to the test file:");
        String testSetFile = scanner.next();

        List<Iris> trainSet = loadDataset(trainSetFile);
        List<Iris> testSet = loadDataset(testSetFile);
        List<Iris> predictions = new ArrayList<>();
        for (Iris characteristic : testSet) {
            List<Iris> neighbors = getNeighbors(trainSet, characteristic, k);
            predictions.add(new Iris(characteristic.fancy_numbers, classify(neighbors)));
        }
        System.out.println("Accuracy: " + getAccuracy(testSet, predictions) + "%");

        while (true) {
            System.out.println("Enter a vector to classify (or 'exit' to quit):");
            String input = scanner.next();
            if (input.equalsIgnoreCase("exit")) {
                break;
            }
            String[] parts = input.split(",");
            double[] features = new double[parts.length];
            for (int i = 0; i < parts.length; i++) {
                features[i] = Double.parseDouble(parts[i]);
            }
            Iris instance = new Iris(features, null);
            List<Iris> neighbors = getNeighbors(trainSet, instance, k);
            String name = classify(neighbors);
            System.out.println("The classified name is: " + name);
        }
    }
}