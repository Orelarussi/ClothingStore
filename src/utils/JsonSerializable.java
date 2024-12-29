package utils;

import com.google.gson.Gson;

public interface JsonSerializable {
    default String serializeToString(){
        return new Gson().toJson(this);
    }
    default <T> T deserializeFromString(Class<T> type,String objectString){
        return new Gson().fromJson(objectString,type);
    }
}
