package be.tabtabstudio.veganapp.data.local;

import android.arch.persistence.room.TypeConverter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;

import be.tabtabstudio.veganapp.data.entities.Label;

public class LabelListConverters {
    @TypeConverter
    public static List<Label> fromString(String json) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(json, new TypeReference<List<Label>>(){});
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @TypeConverter
    public static String toString(List<Label> labellist) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(labellist);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
