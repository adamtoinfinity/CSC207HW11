package edu.grinnell.csc207.Json;

//Inspired by Mark and Matt @*insert their github thing here*
//Also inspired by Rebelsky
public class JSONPair implements JSONValue {
    String name;
    Object value;

    public JSONPair(String name, Object value) {
	this.name = name;
	this.value = value;
    } // JSONPair(name, value)
} // JSONPair
