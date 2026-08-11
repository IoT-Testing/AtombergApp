//package app.marketPlace;
//
//import com.fasterxml.jackson.databind.JsonNode;
//import com.fasterxml.jackson.databind.ObjectMapper;
//
//import java.io.File;
//import java.util.ArrayList;
//import java.util.List;
//
//public class JsonReader {
//
//    private final JsonNode root;
//
//    public JsonReader() throws Exception {
//
//        File file = new File("products.json");
////        System.out.println("File exists: " + file.exists() + " | Absolute path: " + file.getAbsolutePath());
//        ObjectMapper mapper = new ObjectMapper();
//        this.root = mapper.readTree(file);
//    }
//
//    public String getCategoryName() {
//        return root.path("data").path("category").path("name").asText();
//    }
//
//    public List<String> getProductNames() {
//        List<String> names = new ArrayList<>();
//        JsonNode items = root.path("data").path("category").path("products").path("items");
//        for (JsonNode item : items) {
//            String name = item.path("name").asText();
//            if (name != null && !name.isEmpty()) {
//                names.add(name);
//            }
//        }
//        return names;
//    }
//}
