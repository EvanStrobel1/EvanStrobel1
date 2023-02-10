/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package helper;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 *
 * @author Evan
 */
public abstract class Localization {
    static ResourceBundle rb;
    
    public static ResourceBundle getResourceBundle() {
        rb = ResourceBundle.getBundle("locale/Nat", Locale.getDefault());
        return rb;
    }
}
