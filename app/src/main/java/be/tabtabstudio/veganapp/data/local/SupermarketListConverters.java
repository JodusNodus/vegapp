package be.tabtabstudio.veganapp.data.local;

import android.arch.persistence.room.TypeConverter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;

import be.tabtabstudio.veganapp.data.entities.Label;
import be.tabtabstudio.veganapp.data.entities.Supermarket;

public class SupermarketListConverters {
    @TypeConverter
    public static List<Supermarket> fromString(String json) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(json, new TypeReference<List<Supermarket>>(){});
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @TypeConverter
    public static String toString(List<Supermarket> labellist) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(labellist);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
