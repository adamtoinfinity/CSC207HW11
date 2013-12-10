package edu.grinnell.csc207.Json;

import java.util.ArrayList;

public class JSONObject implements JSONValue {
    ArrayList<JSONPair> items;

    public JSONObject(ArrayList<JSONPair> items) {
	this.items = items;
    } // JSONObject(items)
} // JSONObject