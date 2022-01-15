package me.damian.ciepiela.recipes.utils.dto;

import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@DtoBasicTranslator
@Component
public class DtoTranslator {

    @StringIdToObjectId
    public ObjectId convertStringToObjectId(String id) {
        return new ObjectId(id);
    }

    @ObjectIdToString
    public String convertObjectIdToString(ObjectId objectId) {
        return objectId.toString();
    }

    @ListObjectIdToListStringId
    public List<String> convertListOfObjectIdsToStringIds(List<ObjectId> objectIds) {
        return objectIds.stream()
                .map(Objects::toString)
                .collect(Collectors.toList());
    }

    @ListStringIdToListObjectId
    public List<ObjectId> convertListOfStringIdsToObjectIds(List<String> objectIds) {
        return objectIds.stream()
                .map(ObjectId::new)
                .collect(Collectors.toList());
    }

}
