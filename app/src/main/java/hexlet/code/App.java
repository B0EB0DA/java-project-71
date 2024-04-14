package hexlet.code;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.Callable;

@Command(name = "gendiff", mixinStandardHelpOptions = true, version = "gendiff 1.0",
        description = "Compares two configuration files and shows a difference.")

public class App implements Callable<Integer> {

    @Parameters(index = "0", description = "path to first file")
    private Path firstFilePath;
    @Parameters(index = "1", description = "path to second file")
    private Path secondFilePath;
    @Option(names = {"-f", "--format"}, description = "output format [default: stylish]")
    private String format = "stylish";

    /*
    private static Map<String, Object> processFile2(Path file) throws IOException {

        String jsonString = Files.readString(file);
        JsonNode jsonTopNode = objectMapper.readTree(jsonString);
        ObjectNode objectTopNode = (ObjectNode) jsonTopNode;

        System.out.println("Container:" + jsonTopNode.isContainerNode());
        System.out.println("Object:" + jsonTopNode.isObject());
        System.out.println("Array:" + jsonTopNode.isArray());
        System.out.println("Value:" + jsonTopNode.isValueNode());

        JsonNode jsonHostNode = jsonTopNode.get("host");

        System.out.println("Container:" + jsonHostNode.isContainerNode());
        System.out.println("Object:" + jsonHostNode.isObject());
        System.out.println("Array:" + jsonHostNode.isArray());
        System.out.println("Value:" + jsonHostNode.isValueNode());



//        ObjectNode objectTopNode = (ObjectNode) jsonTopNode;
//
//        System.out.println(objectTopNode.toPrettyString());
//        objectTopNode.remove("host1");
//        System.out.println(objectTopNode.toPrettyString());
//


//        for ()
//        System.out.println();
//        System.out.println("Container:" + jsonTopNode.isContainerNode());
//
//        Iterator<Map.Entry<String, JsonNode>> fields = jsonHostNode.fields();
//        fields.forEachRemaining(field -> {
//            System.out.println(field.getKey() + "--->>>>" + field.getValue());
//        });


//        Iterator<String> iterator = jsonTopNode.fieldNames();
//        iterator.forEachRemaining(e -> System.out.println(e + "->" + jsonTopNode.get(e)));

//        for (JsonNode item : jsonTopNode) {
//
////            System.out.println(item.getNodeType()); Gets type if Node (NUMBER, STRING, BOOLEAN etc)
//            //System.out.println(item);
//        }
//        //jsonTopNode.
//        System.out.println();
        return null;
    }
     */
    @Override
    public Integer call() throws Exception {
        firstFilePath = firstFilePath.toAbsolutePath().normalize();
        secondFilePath = secondFilePath.toAbsolutePath().normalize();
        if (!Files.exists(firstFilePath)) {
            throw new FileNotFoundException(firstFilePath.toString());
        }
        if (!Files.exists(secondFilePath)) {
            throw new FileNotFoundException(secondFilePath.toString());
        }
        var jsonDiffer = new Differ(firstFilePath, secondFilePath);
        System.out.println(jsonDiffer.generate());
        return 0;
    }

    public static void main(String[] args) {
        int exitCode = new CommandLine(new App()).execute(args);
        System.exit(exitCode);
    }

}
