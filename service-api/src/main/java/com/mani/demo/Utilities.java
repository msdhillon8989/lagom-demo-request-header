package com.mani.demo;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.commons.lang3.StringUtils;
import play.libs.Json;


public class Utilities 
{



    private static String _makeResult(int status, String message, JsonNode object) {
        ObjectNode result = prepareResult(status, message);
        if(object!=null) {
            result.set("response", object);
        }
        return result.toString();
    }

    private static ObjectNode prepareResult(int status, String message) {
        ObjectNode result = Json.newObject();
        result.put("status", status);
        if (message == null) {
            result.put("message", "OK");
        }
        else {
            result.put("message", message);
        }
        return result;
    }



    public static String ok(JsonNode object,String message){
        if(!StringUtils.isNotBlank(message))
        {
            message = "OK";
        }
        return _makeResult(1,message,object);
    }



    public static String bad(String message){
        if(!StringUtils.isNotBlank(message))
        {
            message = "FAIL";
        }
        return _makeResult(0,message,null);
    }

    public static Double round(Double d){
        return Math.round( d * 100.0 ) / 100.0;
    }


}
