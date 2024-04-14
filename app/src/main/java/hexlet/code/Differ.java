package hexlet.code;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

public class Differ {
    private JsonNode leftNode;          // JsonNode is immutable presentation of json
    private JsonNode rightNode;          // JsonNode is immutable presentation of json
    private ObjectNode rightObjectNode;       // ObjectNode is mutable subclass of JsonNode
    private final Map<String, String> innerDiffMap = new TreeMap<>();
    private final ObjectMapper objectMapper = new ObjectMapper();

    private JsonNode processFile(Path file) throws IOException {
        String jsonString = Files.readString(file);
        return objectMapper.readTree(jsonString);
    }

    public Differ(JsonNode leftNode, JsonNode rightNode) {
        this.leftNode = leftNode;
        this.rightNode = rightNode;
        this.rightObjectNode = (ObjectNode) rightNode;
        initParse();
    }

    public Differ(Path leftFile, Path rightFile) throws IOException {
        this.leftNode = processFile(leftFile);
        this.rightNode = processFile(rightFile);
        this.rightObjectNode = (ObjectNode) this.rightNode;
        initParse();
    }

    private void initParse() {
        Iterator<Map.Entry<String, JsonNode>> jsonFieldsIterator = leftNode.fields();
        jsonFieldsIterator.forEachRemaining(field -> {
            String leftFieldKey = field.getKey();
            JsonNode leftFieldJsondNode = field.getValue();
            String leftFieldText = leftFieldKey + ": " + leftFieldJsondNode;
            JsonNode rightFieldJsonNode = rightNode.get(leftFieldKey);
            if (rightFieldJsonNode == null) {
                innerDiffMap.put(leftFieldKey, " - " + leftFieldText);
            } else {
                if (leftFieldJsondNode.equals(rightFieldJsonNode)) {
                    innerDiffMap.put(leftFieldKey, "   " + leftFieldText);
                } else {
                    String rightFieldText = leftFieldKey + ": " + rightFieldJsonNode;
                    innerDiffMap.put(leftFieldKey, " - " + leftFieldText + "\n + " + rightFieldText);
                }
                rightObjectNode.remove(leftFieldKey);
            }
        });
        jsonFieldsIterator = rightObjectNode.fields();
        jsonFieldsIterator.forEachRemaining(field -> {
            String rightFieldKey = field.getKey();
            innerDiffMap.put(rightFieldKey, " + " + rightFieldKey + ": " + field.getValue());
        });
    }
    public static Map getData(String content) throws Exception {
        return null;
    }

    public String generate() {
        return toString();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{\n");
        for (String s : innerDiffMap.values()) {
            sb.append(s).append("\n");
        }
        sb.append("}\n");
        return sb.toString();
    }
}
