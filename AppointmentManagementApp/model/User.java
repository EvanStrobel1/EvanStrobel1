/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 * Data model for users.
 * @author Evan
 */
public class User {
    private static int currentUser = -1;
    private static String currentUserName;
    
    /**
     * Getter for currentUser
     * @return 
     */
    public static int getCurrentUser() {
        return currentUser;
    }

    /**
     * Setter for currentUser
     * @param currentUser 
     */
    public static void setCurrentUser(int currentUser) {
        User.currentUser = currentUser;
    }

    /**
     * Getter for currentUserName
     * @return 
     */
    public static String getCurrentUserName() {
        return currentUserName;
    }

    /**
     * Setter for currentUserName
     * @param currentUserName 
     */
    public static void setCurrentUserName(String currentUserName) {
        User.currentUserName = currentUserName;
    }
}