import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {

    private static final String pattern = "\"EvseID\":\".{16}\"";
    private static final String INPUT = "evse_data.json";
    private static final String OUTPUT = "id_list.txt";

    private static List<String> idsPool;

    public static void main(String[] args) {
        Parser parser = new Parser();
        try {
            parser.parse(INPUT);
            parser.save(OUTPUT);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public void parse(String file) throws IOException {
        String content = new String(Files.readAllBytes(Paths.get(INPUT)));
        System.out.printf("%14s: %9d %s\n", "File", content.getBytes().length, "B");

        Set<String> ids = new HashSet<>();
        Matcher m = Pattern.compile(pattern).matcher(content);
        int counter = 0;
        for (; m.find(); counter++) {
            String match = m.group();
            match = match.replace("\"EvseID\":\"", "").split("\"")[0];
            ids.add(match);
        }
        System.out.printf("%14s: %9d\n","Matches", counter);
        System.out.printf("%14s: %9d\n","Unique matches", ids.size());
        idsPool = new ArrayList<>(ids);
    }

    public void save(String file) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, false))) {
            boolean addLF = false;
            for (String s : idsPool) {
                if (addLF) {
                    bw.write(String.format("\n%s", s));
                } else {
                    bw.write(String.format("%s", s));
                }
                addLF = true;
            }
        }
    }


}
