package com.circulous.xpert.model;

import java.io.Serializable;

public class ColumnModel  implements Serializable{  
	  
    private String header;  
    private String property;  

    public ColumnModel(String header, String property) {  
        this.header = header;  
        this.property = property;  
    }  

    public String getHeader() {  
        return header;  
    }  

    public String getProperty() {  
        return property;  
    }  
}  