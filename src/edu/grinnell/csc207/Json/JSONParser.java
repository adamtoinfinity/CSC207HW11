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

    static ArrayList parseArray(String s) {
	return parseBrackets(s, false);
    }

    static JSONObject parseObject(String s) {
	return new JSONObject(parseBrackets(s, true));
    }

    static ArrayList parseBrackets(String s, boolean object) {
	ArrayList list = new ArrayList();
	char[] chars = s.toCharArray();
	int unadded = 0;
	int nextComma = findOnLayer(chars, ',', unadded);

	while (nextComma != -1) {
	    if (object) {
		list.add(parsePair(s.substring(unadded, nextComma)));
	    } else {
		list.add(parse(s.substring(unadded, nextComma)));
	    }
	    unadded = nextComma + 1;
	    nextComma = findOnLayer(chars, ',', unadded);
	}

	if (object) {
	    list.add(parsePair(s.substring(unadded)));
	} else {
	    list.add(parse(s.substring(unadded)));
	}

	return list;
    }

    static JSONPair parsePair(String s) {
	if (s.equals("")) {
	    return null;
	}
	int colonIndex = s.indexOf(':');
	String name = s.substring(0, colonIndex);
	String value = s.substring(colonIndex + 1);
	return new JSONPair(name, parse(value));
    }

    public static Object parse(String s) {
	char[] chars = s.toCharArray();
	for (int i = 0; i < chars.length; i++) {
	    if (Character.isWhitespace(chars[i])) {
		// Skip over
	    } else if (chars[i] == '{') {
		String sub = s.substring(i + 1, findOnLayer(chars, '}', i));
		i += sub.length() - 1;
		return parseObject(sub);
	    } else if (chars[i] == '[') {
		String sub = s.substring(i + 1, findOnLayer(chars, ']', i));
		i += sub.length() - 1;
		return parseArray(sub);
	    } else if (chars[i] == '"') {
		int j = i + 1;
		while (j < chars.length
			&& !(chars[j] == '"' && chars[j - 1] != '\\')) {
		    j++;
		}
		String sub = s.substring(i + 1, j);
		i += sub.length() - 1;
		return sub;
	    } else if (chars[i] == '-' || Character.isDigit(chars[i])) {
		int end = i;
		while (end < chars.length
			&& (chars[end] == '-' || chars[end] == '+'
				|| chars[end] == 'e' || chars[end] == 'E'
				|| chars[end] == '.' || Character
				    .isDigit(chars[end]))) {
		    end++;
		}
		String sub = s.substring(i, end);
		i = end - 1;
		return parseNums(sub);
	    } else if (chars[i] == 't') {
		if (s.substring(i, i + 4).equals("true")) {
		    i += 3;
		    return true;
		} else {
		    throw new IllegalArgumentException(
			    "Invalid input at character " + i);
		}
	    } else if (chars[i] == 'f') {
		if (s.substring(i, i + 5).equals("false")) {
		    i += 4;
		    return false;
		} else {
		    throw new IllegalArgumentException(
			    "Invalid input at character " + i);
		}
	    } else if (chars[i] == 'n') {
		if (s.substring(i, i + 4).equals("null")) {
		    i += 3;
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

    private static double parseNums(String s) {
	String exponent = "1";
	int eLocation = Math.max(s.indexOf('e'), s.indexOf('E'));
	if (eLocation != -1) {
	    exponent = s.substring(eLocation);
	    s = s.substring(0, eLocation);
	}
	double num = Double.parseDouble(s);
	num *= Math.pow(10, Integer.parseInt(exponent));
	return num;
    }

    static int findOnLayer(char[] chars, char c, int i) {
	int layer = 0;
	boolean inQuotes = false;

	for (; i < chars.length; i++) {
	    if (chars[i] == '"') {
		inQuotes = !inQuotes;
	    }
	    if (!inQuotes) {
		if (chars[i] == '{' || chars[i] == '[') {
		    layer++;
		} else if (chars[i] == '}' || chars[i] == ']') {
		    layer--;
		}
		if ((chars[i] == c && layer == 0)) {
		    return i;
		}
	    }
	}
	return -1;
    }

    public static void main(String[] args) {
	parse("{}");
	System.out.println("Case 0 runs.");
	parse("{\"sam\":\"rebelsky\"}");
	System.out.println("Case 1 runs.");
	parse("{\"samr\":{\"fname\":\"Sam\",\"lname\":\"Rebelsky\",\"ryphot\":0}}");
	System.out.println("Case 2 runs.");
	parse("{\"samr\":[\"fname\":\"Sam\",null:true,\"-3.141e2\":0]}");
	System.out.println("Case 3 runs.");
    }
}