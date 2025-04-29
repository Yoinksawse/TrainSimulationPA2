package com.example.trainstation_pa2.Model;

// Models a single train station
public class Station {
    private String code;
    private String name;

    public Station(String code, String name) {
            this.code = code;
            this.name = name;
        }

        // Copy constructor
    public Station(Station s) {
            this(s.code, s.name);
        }

        public String getName() {
            return name;
        }

        public String getCode() {
            return code;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String toString() {
            return "[" + code + "] " + name;
    }
}
