package edu.grinnell.csc207.Json;

import java.util.ArrayList;

public class JSONParser {

    /*
     * create a linked list of JSONObjects
     * 
     * if { construct a JSON object first string is name value = parse what's
     * after colon add this to the linked list
     * 
     * else if [ construct an array parse each value (sepArated by commas)
     * 
     * parse a value if { ^ value = construct a JSON object | if [ value =
     * construct an array list parse each value (separated by commas) if null
     * value = null else if true value = true else if false value = false else
     * is a number value = that number (beware of symbols in it, like ^ return
     * value;
     * 
     * NOTES: we might need helper procedures to find a string in " " find a
     * string in {} deal with a number that contains other characters
     * 
     * we'll also need to deal with whitespace - I suggest a for loop
     */

    // ADAM NOTE: We're not passing { or [ or ] or } into these procedures for
    // parsing
    ArrayList parseArray(String s) {
	return parseBrackets(s, false);
    }

    JSONObject parseObject(String s) {
	return new JSONObject(parseBrackets(s, true));
    }

    ArrayList parseBrackets(String s, boolean object) {
	ArrayList list = new ArrayList();
	int countsIfInside = 0;
	char[] chars = s.toCharArray();
	int startOfString = 0;
	for (int i = 0; i < chars.length; i++) {
	    if (i == '{' || i == '[') {
		countsIfInside++;
	    } else if (i == '}' || i == ']') {
		countsIfInside--;
	    }
	    if ((chars[i] == ',' && countsIfInside == 0)
		    || i == chars.length - 1) {
		if (object) {
		    list.add(parsePair(s.substring(startOfString, i)));
		} else {
		    list.add(parse(s.substring(startOfString, i)));
		}
		startOfString = i + 1;
	    }
	}
	return list;
    }

    JSONPair parsePair(String s) {
	int colonIndex = s.indexOf(':');
	String name = s.substring(0, colonIndex);
	String value = s.substring(colonIndex + 1);
	return new JSONPair(name, parse(value));
    }

    //Numbers, String, Booleans, null
    public Object parse(String s) {
	int countsIfInside = 0;
	char[] chars = s.toCharArray();
	int startOfString = 0;
	for (int i = 0; i < chars.length; i++) {
	    if(/*TYPES!*/ false){
		
	    }
	}
	return null;
    }
}