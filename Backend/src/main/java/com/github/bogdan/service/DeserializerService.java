package com.github.bogdan.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.NullNode;
import com.github.bogdan.exception.WebException;
import com.j256.ormlite.dao.Dao;
import org.mindrot.jbcrypt.BCrypt;

import java.time.LocalDate;

public class DeserializerService {

    public static String getStringFieldValue(JsonNode node, String field){
        checkForExplicitlyNullField(node.get(field),"Necessary field \""+field+ "\" can't be null");
        if(node.get(field) == null){
            throw new WebException("Necessary field \""+field+ "\" can't be null",400);
        }else if(node.get(field).asText()==""){
            throw new WebException("Necessary field \""+field+ "\" can't be null",400);
        } else return node.get(field).asText();
    }

    public static String getOldStringFieldValue(JsonNode node, String field,String value){
        if(node instanceof NullNode){
            return value;
        }else if(node.get(field) == null){
            return value;
        }else if(node.get(field).asText().isEmpty()){
            return value;
        } else return node.get(field).asText();
    }


    public static boolean checkNullFieldValue(JsonNode node, String field){
        if(node instanceof NullNode){
            return true;
        }else if(node.get(field) == null){
            return true;
        }else if(node.get(field).asText()==""){
            return true;
        } else return false;
    }


    public static String getOldPasswordFieldValue(JsonNode node, String field,String value){
        if(node instanceof NullNode){
            return value;
        }else if(node.get(field) == null){
            return value;
        }else if(node.get(field).asText()==""){
            return value;
        } else{
            String hashedPassword = BCrypt.hashpw(node.get(field).asText(), BCrypt.gensalt(12));
            return hashedPassword;
        }
    }

    public static int getOldIntFieldValue(JsonNode node, String field,int value){
        if(node instanceof NullNode){
            return value;
        }else if(node.get(field) == null){
            return value;
        }else if(node.get(field).asText()==""){
            return value;
        } else return node.get(field).asInt();
    }

    public static boolean getOldBooleanFieldValue(JsonNode node, String field,boolean value){
        if(node instanceof NullNode){
            return value;
        }else if(node.get(field) == null){
            return value;
        } else return node.get(field).asBoolean();
    }

    public static int getIntFieldValue(JsonNode node, String field){
        checkForExplicitlyNullField(node.get(field),"Necessary field \""+field+ "\" can't be null");
        if(node.get(field) == null){
            throw new WebException("Necessary field \""+field+ "\" can't be null",400);
        }else if(node.get(field).asText()==""){
            throw new WebException("Necessary field \""+field+ "\" can't be null",400);
        }else return node.get(field).asInt();
    }

    public static boolean checkNullBooleanFieldValue(JsonNode node,String field){
        checkForExplicitlyNullField(node.get(field),"Necessary field \""+field+ "\" can't be null");
        if(node.get(field) == null){
            throw new WebException("Necessary field \""+field+ "\" can't be null",400);
        }else if(node.get(field).asText()==""){
            throw new WebException("Necessary field \""+field+ "\" can't be null",400);
        } else return node.get(field).asBoolean();
    }


    public static void checkForExplicitlyNullField(JsonNode node, String exceptionMessage){
        if (node instanceof NullNode) {
            throw new WebException(exceptionMessage,400);
        }
    }
}
