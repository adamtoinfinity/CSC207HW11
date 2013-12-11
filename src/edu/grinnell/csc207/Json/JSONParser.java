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

    ArrayList parseArray(String s) {
	return parseBrackets(s, false);
    }

    JSONObject parseObject(String s) {
	return new JSONObject(parseBrackets(s, true));
    }

    ArrayList parseBrackets(String s, boolean object) {
	ArrayList list = new ArrayList();
	int layer = 0;
	char[] chars = s.toCharArray();
	int startOfString = 0;
	for (int i = 0; i < chars.length; i++) {
	    if (chars[i] == '{' || chars[i] == '[') {
		layer++;
	    } else if (chars[i] == '}' || chars[i] == ']') {
		layer--;
	    }
	    if ((chars[i] == ',' && layer == 0) || i == chars.length - 1) {
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

    public Object parse(String s) {
	int layer = 0;
	char[] chars = s.toCharArray();
	int startOfString = 0;
	for (int i = 0; i < chars.length; i++) {
	    if (Character.isWhitespace(chars[i])) {
		// Skip over
	    } else if (chars[i] == '{') {
		return parseObject(s.substring(i, findOnLayer(chars, '}', i)));
	    } else if (chars[i] == '[') {
		return parseArray(s.substring(i, findOnLayer(chars, ']', i)));
	    } else if (chars[i] == '"') {
		return parseString(s.substring(i, findNext(chars, '"', i)));
	    } else if (chars[i] == '-' || Character.isDigit(chars[i])) {
		int end = i;
		while (chars[end] == '-' || chars[end] == '+'
			|| chars[end] == 'e' || chars[end] == 'E'
			|| chars[end] == '.' || Character.isDigit(chars[end])) {
		    end++;
		}
		return parseNums(s.substring(i, end));
	    } else if (chars[i] == 't') {
		if (s.substring(i, i + 4).equals("true")) {
		    i += 4;
		    return true;
		} else {
		    throw new IllegalArgumentException(
			    "Invalid input at character " + i);
		}
	    } else if (chars[i] == 'f') {
		if (s.substring(i, i + 5).equals("false")) {
		    i += 5;
		    return false;
		} else {
		    throw new IllegalArgumentException(
			    "Invalid input at character " + i);
		}
	    } else if (chars[i] == 'n') {
		if (s.substring(i, i + 4).equals("null")) {
		    i += 4;
		    return null;
		} else {
		    throw new IllegalArgumentException(
			    "Invalid input at character " + i);
		}
	    } else {
		throw new IllegalArgumentException(
			"Invalid input at character " + i);
	    }
	}
	return null;
    }

    private Object parseNums(String substring) {
	// TODO Auto-generated method stub
	return null;
    }

    private Object parseString(String substring) {
	// TODO Auto-generated method stub
	return null;
    }

    int findOnLayer(char[] chars, char c, int i) {
	int layer = 0;
	boolean inQuotes = false;

	for (; i < chars.length; i++) {
	    if (chars[i] == '"') {
		inQuotes = !inQuotes;
	    }
	    if (!inQuotes) {
		if (chars[i] == '{' || chars[i] == '[') {
		    layer++;
		} else if (i == '}' || i == ']') {
		    layer--;
		}
		if ((chars[i] == c && layer == 0)) {
		    return i;
		}
	    }
	}
	return -1;
    }

    int findNext(char[] chars, char c, int i) {
	for (; i < chars.length; i++) {
	    if (chars[i] == c) {
		return i;
	    }
	}
	return -1;
    }
}