/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vc;

import m.Case;

/**
 *
 * @author Hugo
 */
public class Transformation {
    private int new_value;
    private Case to_transform;
    private String transform_type;
    
    public Transformation (int new_value, Case to_transform, String transform_type) {
        this.new_value = new_value;
        this.to_transform = to_transform;
        this.transform_type = transform_type;
    }

    public Transformation(Case to_transform, String transform_type) {
        this.to_transform = to_transform;
        this.transform_type = transform_type;
    }
    
    public String getType() {
        return this.transform_type;
    }
    
    public Case getTo_transform() {
        return this.to_transform;
    }
    
    public int getNew_value() {
        return this.new_value;
    }
    
    
}
