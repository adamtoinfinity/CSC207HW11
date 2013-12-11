package edu.grinnell.csc207.Json;

import java.util.ArrayList;

/**
 * 
 * @author Justus Goldstein-Shirley
 * @author Fiona Byrne
 * @author Adam Arsenault
 * @author Kitt Nika
 * 
 */

public class JSONParser {

    /**
     * Sends parseBrackets the string, and false, indicating to parseBrackets
     * that the string we're passing is not an object.
     * 
     */
    static ArrayList parseArray(String s) {
	return parseBrackets(s, false);
    } // parseArray(String)

    /**
     * Sends parseBrackets the string, and true, indicating to parseBrackets
     * that the string we're passing is an object.
     * 
     */
    static JSONObject parseObject(String s) {
	return new JSONObject(parseBrackets(s, true));
    } // parseObject(String)

    /**
     * Parses JSONObjects and arrays.
     * 
     */
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
	} // while

	if (object) {
	    list.add(parsePair(s.substring(unadded)));
	} else {
	    list.add(parse(s.substring(unadded)));
	}

	return list;
    } // parseBrackets(String, boolean)

    /**
     * Parses a string into a JSONPair, which contains a name and a parsed
     * value.
     * 
     * @return a new JSONPair
     */
    static JSONPair parsePair(String s) {
	if (s.equals("")) {
	    return null;
	} // if
	int colonIndex = s.indexOf(':');
	String name = s.substring(0, colonIndex);
	String value = s.substring(colonIndex + 1);
	return new JSONPair(name, parse(value));
    } // parsePair(String)

    /**
     * Parses a JSON value.
     * 
     * @return the Java object represented by the string s
     * 
     */
    public static Object parse(String s) {
	char[] chars = s.toCharArray();
	for (int i = 0; i < chars.length; i++) {
	    // skip whitespace
	    if (Character.isWhitespace(chars[i])) {
		// Strings starting with { are JSONObjects, so we pass them to
		// parseObject
	    } else if (chars[i] == '{') {
		String sub = s.substring(i + 1, findOnLayer(chars, '}', i));
		i += sub.length() - 1;
		return parseObject(sub);
		// Strings starting with [ are JSONArrays, so we pass them to
		// parseArray
	    } else if (chars[i] == '[') {
		String sub = s.substring(i + 1, findOnLayer(chars, ']', i));
		i += sub.length() - 1;
		return parseArray(sub);
	    }
	    // Strings starting with '"' are strings, so we we return the string
	    // until the trailing quote
	    else if (chars[i] == '"') {
		int j = i + 1;
		while (j < chars.length
			&& !(chars[j] == '"' && chars[j - 1] != '\\')) {
		    j++;
		}
		String sub = s.substring(i + 1, j);
		i += sub.length() - 1;
		return sub;
	    }
	    // Strings starting with '-' or number characters are numbers. We
	    // send the number (including the -) to parseNums
	    else if (chars[i] == '-' || Character.isDigit(chars[i])) {
		int end = i;
		while (end < chars.length
			&& (chars[end] == '-' || chars[end] == '+'
				|| chars[end] == 'e' || chars[end] == 'E'
				|| chars[end] == '.' || Character
				    .isDigit(chars[end]))) {
		    end++;
		} // while
		String sub = s.substring(i, end);
		i = end - 1;
		return parseNums(sub);
	    }
	    // Strings starting with 't' must be the boolean true, or it is
	    // improper JSON. If it's improper, throw an exception accordingly.
	    else if (chars[i] == 't') {
		if (s.substring(i, i + 4).equals("true")) {
		    i += 3;
		    return true;
		} else {
		    throw new IllegalArgumentException(
			    "Invalid input at character " + i);
		}
	    }
	    // Strings starting with 'f' must be the boolean false, or throw an
	    // exception
	    else if (chars[i] == 'f') {
		if (s.substring(i, i + 5).equals("false")) {
		    i += 4;
		    return false;
		} else {
		    throw new IllegalArgumentException(
			    "Invalid input at character " + i);
		}
	    }
	    // Strings starting with 'n' are either null or throw an exception
	    else if (chars[i] == 'n') {
		if (s.substring(i, i + 4).equals("null")) {
		    i += 3;
		    return null;
		} else {
		    throw new IllegalArgumentException(
			    "Invalid input at character " + i);
		}
	    } else {
		// Any other character is invalid JSON and throws an exception
		throw new IllegalArgumentException(
			"Invalid input at character " + i);
	    }
	} // for
	return null;
    } // parse(String)

    /**
     * Calculates the number represented by s
     * 
     */
    private static double parseNums(String s) {
	String exponent = "1";
	int eLocation = Math.max(s.indexOf('e'), s.indexOf('E'));
	if (eLocation != -1) {
	    exponent = s.substring(eLocation);
	    s = s.substring(0, eLocation);
	} // if
	double num = Double.parseDouble(s);
	num *= Math.pow(10, Integer.parseInt(exponent));
	return num;
    } // parseNums(String)

    /**
     * Finds the next instance of character c that is not enclosed by brackets
     * or quotation marks.
     * 
     */
    static int findOnLayer(char[] chars, char c, int i) {
	int layer = 0;
	boolean inQuotes = false;

	for (; i < chars.length; i++) {
	    if (chars[i] == '"') {
		inQuotes = !inQuotes;
	    } // if
	    if (!inQuotes) {
		if (chars[i] == '{' || chars[i] == '[') {
		    layer++;
		} else if (chars[i] == '}' || chars[i] == ']') {
		    layer--;
		}
		if ((chars[i] == c && layer == 0)) {
		    return i;
		}
	    } // if
	} // for
	return -1;
    } // findOnLayer(char[], char, int)
} // JSONParser