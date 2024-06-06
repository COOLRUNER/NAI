import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
public class Reader {

    private final List<String[]> content;

    public Reader(String fileName) throws IOException {
        content = new ArrayList<>();
        List<String> lines = Files.readAllLines(Paths.get(fileName));
        for(String line : lines) {
            content.add(line.split(","));
        }
    }

    public List<String[]> getContent() {
        return content;
    }

    public List<String> getClasses() {
        List<String> classes = new ArrayList<>();
        for(String[] line : content) {
            if(!classes.contains(line[0])) classes.add(line[0]);
        }
        return classes;
    }

}