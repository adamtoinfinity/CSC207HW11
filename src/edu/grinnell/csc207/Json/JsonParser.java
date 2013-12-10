package edu.grinnell.csc207.Json;

import java.util.LinkedList;
import java.util.ArrayList;

public class JsonParser {

    /*
     * create a linked list of JSONObjects
     * 
     * if { construct a JSON object first string is name value = parse what's
     * after colon add this to the linked list
     * 
     * else if [ construct an array parse each value (seperated by commas)
     * 
     * parse a value if { ^ value = construct a JSON object | if [ value =
     * construct an array list parse each value (seperated by commas) if null
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
    public ArrayList array(String s) {
	ArrayList list = new ArrayList();
	int countsIfInside = 0;
	char[] chars = s.toCharArray();
	// only when startofString is zero will we be outside of any topmost
	// level bracket
	int startOfString = 0;
	for (int i = 0; i < chars.length; i++) {
	    if (i == '{' || i == '[') {
		countsIfInside++;
	    } else if (i == '}' || i == ']') {
		countsIfInside--;
	    }
	    if ((chars[i] == ',' && countsIfInside == 0)
		    || i == chars.length - 1) {
		list.add(parse(s.substring(startOfString, i)));
		startOfString = i + 1;
	    }
	}

	return list;

    }

    public JsonPair pair(String s) {
	int colonIndex = s.indexOf(':');
	String name = s.substring(0, colonIndex);
	String value = s.substring(colonIndex + 1);
	return new JsonPair(name, parse(value));
    }

    public JsonObject object(String s) {
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
		list.add(pair(s.substring(startOfString, i)));
		startOfString = i + 1;
	    }
	}

	return new JsonObject(list);
    }

    public ArrayList<JsonPair> parse(String s) {
	return null;

    }
}