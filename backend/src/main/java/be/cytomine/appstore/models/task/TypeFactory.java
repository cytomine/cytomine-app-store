package be.cytomine.appstore.models.task;

import java.util.Optional;


import com.fasterxml.jackson.databind.JsonNode;

public class TypeFactory {

    public static String getTypeId(JsonNode typeNode) {
        if (typeNode.isTextual()) {
            return typeNode.textValue();
        } else {
            return typeNode.get("id").textValue();
        }
    }

    public static Type createType(JsonNode node, String charset) {
        JsonNode typeNode = Optional.ofNullable(node.get("type"))
            .orElse(node);
        // add new types here
        String typeId = getTypeId(typeNode);
        Type type = new Type();
        type.setId(typeId);
        type.setCharset(charset);
        return type;
    }

}
