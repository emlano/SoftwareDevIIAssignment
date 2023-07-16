package org.westminster;

// Import modules
import java.util.Scanner;
import java.io.File;
import java.io.IOException;
import java.io.FileWriter;
import java.io.FileNotFoundException;

public class Main {

    public enum Condition {
        AllQueues,
        EmptyQueues
    }

    public static void main(String[] args) {
        // Init vars
        Scanner scanner = new Scanner(System.in);
        int burgerStock = 0;
        int queueNo;
        String[][] queue = {{"_", "_"}, {"_", "_", "_"}, {"_", "_", "_", "_", "_"}};
        
        // Print menu on program start
        System.out.println();
        printMenu();
        System.out.println();

        // Main UI loop
        while (true) {
            // Show warning when stock is low
            if (burgerStock <= 10) {
                System.out.println("( ! ) Warning! Burger Stock Low!");
                System.out.println();
            }

            // Prompt user for commands
            System.out.print("( > ) Enter command: ");
            String command = scanner.nextLine().trim().toUpperCase();
            System.out.println();

            // Main selection logic
            switch (command) {
                // Show all queues
                case "100": case "VFQ":
                    printQueue(queue);
                    System.out.println();
                    break;

                // Show all empty queues
                case "101": case "VEQ":
                    printEmptyQueue(queue);
                    System.out.println();
                    break;

                // Add customer to queue
                case "102": case "ACQ":
                    try {
                        System.out.print("( ? ) Enter customer name: ");
                        String customerName = scanner.nextLine().strip();

                        if (customerName.equals("")) {
                            System.out.println("( ! ) Error! Customer name cannot be empty!");
                            System.out.println();
                            break;
                        
                        } else if (customerName.equals("null")) {
                            System.out.println("( ! ) Error! Illegal string 'null' in input!");
                            System.out.println();
                            break;

                        } else if (customerName.equals("_")) {
                            System.out.println("( ! ) Error! Illegal string '_' in input!");
                            System.out.println();
                            break;
                        
                        } else if (customerName.contains(",")) {
                            System.out.println("( ! ) Error! Illegal character ',' in input!");
                            System.out.println();
                            break;
                        }
                        System.out.print("( ? ) Enter queue (1, 2, 3) : ");
                        queueNo = Integer.valueOf(scanner.nextLine()) - 1;
                        addToQueue(queue, customerName, queueNo);

                    // Catch wrong input types
                    } catch (NumberFormatException e) {
                        System.out.println("( ! ) Error! Input not an Integer! Number input required!");
                    }

                    System.out.println();
                    break;

                // Remove customer
                case "103": case "RCQ":
                    try {
                        System.out.print("( ? ) Enter customers' queue: ");
                        queueNo = Integer.valueOf(scanner.nextLine()) - 1;
                        System.out.print("( ? ) Enter customers' row: ");
                        int row = Integer.valueOf(scanner.nextLine()) - 1;
                        removeCustomer(queue, queueNo, row);

                    } catch (NumberFormatException e) {
                        System.out.println("( ! ) Error! Input not an Integer! Number input required!");
                    }

                    System.out.println();
                    break;

                // Remove served customer
                case "104": case "PCQ":
                    try {
                        System.out.print("( ? ) Enter queue: ");
                        queueNo = Integer.valueOf(scanner.nextLine()) - 1;
                        burgerStock = removeServedCustomer(queue, burgerStock, queueNo);

                    } catch (NumberFormatException e) {
                        System.out.println("( ! ) Error! Input not an Integer! Number input required!");
                    }

                    System.out.println();
                    break;

                // Sort and display customers
                case "105": case "VCS":
                    sortCustomers(queue);
                    System.out.println();
                    break;

                // Write to file
                case "106": case "SPD":
                    writeToFile(queue, burgerStock);
                    System.out.println();
                    break;

                // Read from file
                case "107": case "LPD":
                    burgerStock = readFromFile(queue, burgerStock);
                    System.out.println();
                    break;

                // Display burger stock
                case "108": case "STK":
                    System.out.println("( $ ) Remaining Stock: " + burgerStock);
                    System.out.println();
                    break;

                // Add burger stock
                case "109": case "AFS":
                    try {
                        System.out.print("( ? ) Enter amount to be added: ");
                        int addToBurgerStock = Integer.valueOf(scanner.nextLine());
                        // Check if burger stocks exceeds limit with addition
                        if ((addToBurgerStock + burgerStock <= 50) && !(addToBurgerStock < 0)) {
                            burgerStock += addToBurgerStock;
                            System.out.println("( $ ) Successfully added to stock!");
                        
                        } else {
                            System.out.println("( ! ) Error! Burger count out of bounds (Limit : 50)!");
                        }

                    } catch (NumberFormatException e) {
                        System.out.println("( ! ) Error! Input not an Integer! Number input required!");
                    }

                    System.out.println();
                    break;

                // Print main menu
                case "111": case "PMN":
                    printMenu();
                    System.out.println();
                    break;

                // Program quit
                case "999": case "EXT":
                    System.out.println("Exiting...");
                    scanner.close();
                    System.out.println();
                    return;

                // Catch any other command
                default:
                    System.out.println("( ! ) Error! No such command found!");
                    System.out.println();
            }
        }
    }

    public static void printMenu() {
        // Print main menu accepts and returns no args
        System.out.println("    Foodie Fave Customer Management System");
        System.out.println();
        System.out.println("                Version 0.1.0");
        System.out.println("        _______________________________");
        System.out.println();
        System.out.println("    100 or VFQ -- View all queues");
        System.out.println("    101 or VEQ -- View all empty queues");
        System.out.println("    102 or ACQ -- Add customer to queue");
        System.out.println("    103 or RCQ -- Remove a customer from queue");
        System.out.println("    104 or PCQ -- Remove a served customer");
        System.out.println("    105 or VCS -- View customers alphabetically");
        System.out.println("    106 or SPD -- Store program data in file");
        System.out.println("    107 or LPD -- Load program data from file");
        System.out.println("    108 or STK -- View remaining burger stock");
        System.out.println("    109 or AFS -- Add burgers to stock");
        System.out.println("    111 or PMN -- Print this menu");
        System.out.println("    999 or EXT -- Exit the program");
    }

    public static void printQueue(String[][] arr) {
        // Print all queues, requires 2d String array containing all queues as arg
        // Display header
        System.out.println("    *****************");
        System.out.println("    *    Cashiers   *");
        System.out.println("    *****************");

        System.out.print("       ");

        // Iterate through 2d array and check existence of elements at each index
        displayQueues(Condition.AllQueues, arr);
    }

    public static void printEmptyQueue(String[][] arr) {
        // Print all queues with empty spots, accepts 2d String array containing all queues as arg
        System.out.println("  *********************");
        System.out.println("  *   Empty Cashiers  *");
        System.out.println("  *********************");

        // To indent first line from edge
        System.out.print("       ");
        
        displayQueues(Condition.EmptyQueues, arr);
    }

    private static void displayQueues(Condition condition, String[][] arr) {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 3; j++) {
                if (getCondition(condition, arr[j], i)) {
                    if (arr[j][i].equals("_")) {
                        System.out.print("X");

                    } else {
                        System.out.print("O");
                    }

                } else {
                    // To print whitespace if index bigger than queue size
                    // In order to keep columns straight
                    System.out.print(" ");
                }
                // Draw space between each column - 4 whitespaces
                System.out.print("    ");
            }
            // Line breaker
            System.out.println();
            // Indent at beginning of each line
            System.out.print("       ");
        }
        // Print footer
        System.out.println();
        System.out.println("O - Occupied    X - Not Occupied");
    }

    private static boolean getCondition(Condition condition, String[] arr, int i) {
        if (condition == Condition.AllQueues) {
            return (arr.length > i);
        }
        return (arr.length > i && hasEmptySpots(arr));
    }

    public static void addToQueue(String[][] arr, String customerName, int queueNo) {
        // Add customer to queue when customer name is given
        //Requires 2d String array type arr, and String type customer name and Integer queue number. 
        // Check if valid queue num was entered
        if (queueNo == 0 || queueNo == 1 || queueNo == 2) {
            if (hasEmptySpots(arr[queueNo])) {
                // Inserts customer name to first empty space it finds
                for (int i = 0; i < arr[queueNo].length; i++) {
                    if (arr[queueNo][i].equals("_")) {
                        arr[queueNo][i] = customerName;
                        System.out.println("( $ ) Successfully added to queue!");
                        return;
                    }
                }
            // If queue is full
            } else {
                System.out.println("( ! ) Error! No empty slots in queue!");
            }
        
        } else {
            System.out.println("( ! ) Error! No such queue found!");
        }
    }

    private static boolean hasEmptySpots(String[] arr) {
        // Check if queue is full and return boolean, requires 2d array of queues
        for (String i : arr) {
            if (i.equals("_")) {
                return true;
            }
        }

        return false;
    }

    public static void removeCustomer(String[][] arr, int queueNo, int row) {
        // Removes customer when queue number and row is given,
        // Required arguments 2d string array of queues, queue number integer, row number integer
        if (queueNo == 0 || queueNo == 1 || queueNo == 2) {
            // To check if row input is in range
            if (row < arr[queueNo].length && row >= 0) {
                // To check if position includes a customer
                if (!arr[queueNo][row].equals("_")) {
                    arr[queueNo][row] = "_";
                
                } else {
                    System.out.println("( ! ) Error! No customer in position!");
                    return;
                }
            
            } else {
                System.out.println("( ! ) Error! No such row found!");
                return;
            }
        
        } else {
            System.out.println("( ! ) Error! No such queue found!");
            return;
        }
        // Move other customers forward once
        moveForward(arr);
        System.out.println("( $ ) Successfully removed from queue!");
    }

    private static void moveForward(String[][] arr) {
        // To move customers up in case an empty space opens in the middle of a queue
        // Accepts 2d array of queues
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[i].length; j++) {
                if (j != arr[i].length - 1) {
                    if (!arr[i][j + 1].equals("_") && arr[i][j].equals("_")) {
                        arr[i][j] = arr[i][j + 1];
                        arr[i][j + 1] = "_";
                    }
                }
            }
        }
    }

    public static int removeServedCustomer(String[][] arr, int burgerStock, int queueNo) {
        // Server and remove customer when queue number is given
        // Requires 2d String array type arr and remaining burger number and queue number as Integers
        // Returns burger stock
        if (queueNo == 0 || queueNo == 1 || queueNo == 2) {
            // To check if burger stock is enough to serve customer
            if (burgerStock >= 5) {
                // Always serve and remove the first customer in queue
                if (!arr[queueNo][0].equals("_")) {
                    arr[queueNo][0] = "_";
                    // Shift queue once to left
                    moveForward(arr);
                    System.out.println("( $ ) Successfully served customer!");
                    return (burgerStock -= 5);
                
                } else {
                    System.out.println("( ! ) Error! No customers in queue!");
                    return burgerStock;
                }
            
            } else {
                System.out.println("( ! ) Error! Burger stock too low for transaction!");
                return burgerStock;
            }

        } else {
            System.out.println("( ! ) Error! No such queue Found!");
            return burgerStock;
        }
    }

    public static void sortCustomers(String[][] arr) {
        // Sort alphabetically customers using String Comparison,
        // Requires 2d String array as arr 
        String[] customerArr = new String[10];
        int arrIndex = 0;
        
        // To create a single array of customers from the 2d array
        for (String[] j : arr) {
            for (String k : j) {
                customerArr[arrIndex] = k;
                arrIndex++;

            }
        }

        // Iterate through new array and sort lexicographically (dictionary sort).
        bubbleSort(customerArr);

        System.out.println("( $ ) Displaying all customers in alphabetical order: ");
        System.out.println();
        // Print sorted array to console
        for (String i : customerArr) {
            if (!i.equals("_")) {
                System.out.println("    " + i);
            }
        }
    }

    private static void bubbleSort(String[] arr) {
        // Sort a String array alphabetically using bubble sort algorithm
        // Requires String[] as an argument, returns sorted string array
        int length = arr.length;

        for (int i = 0; i < length; i++) {
            // Iterate through array, again and again till i == length -1, biggest value always
            // pushed to the end of the list with each iteration of i.
            for (int j = 1; j < length - i; j++) {
                // Compare if one string is lower (comes prior alphabetically) in ascii.
                if (!compareStrings(arr[j - 1], arr[j])) {
                    String temp = arr[j - 1];
                    arr[j - 1] = arr[j];
                    arr[j] = temp;
                }
            }
        }
    }

    private static boolean compareStrings(String firstString, String secondString) {
        // Compare each char in string and return if the first string comes prior to second string
        // Accepts two strings as arguments and returns a boolean (first string comes prior to second string)
        int biggestLength = Math.max(firstString.length(), secondString.length());

        int asciiOfFirst = 0;
        int asciiOfSecond = 0;

        for (int i = 0; i < biggestLength; i++) {
            // Return true if first string length is smaller than the second string
            // And strings are similar up till the end of the first string
            if (i == firstString.length()) {
                return true; 
            }
            
            if (i == secondString.length()) {
                return false; 
            }

            // Get char at index and convert it into an ascii value
            asciiOfFirst = firstString.charAt(i);
            asciiOfSecond = secondString.charAt(i);

            if (asciiOfFirst != asciiOfSecond) {
                break;
            }
        }

        return (asciiOfFirst < asciiOfSecond);
    }

    public static void writeToFile(String[][] arr, int burgerStock) {
        // Save session and data into file "data.txt"
        // Requires String type 2d array as arr and Integer type burger stock
        
        // To catch any errors upon creating file
        try {
            File file = new File("data.txt");
            FileWriter writer = new FileWriter(file);
            // Amount of burgers always written on first line
            writer.write(burgerStock + ",\n");

            // Each line contains customers of a queue, delimited by ","
            for (String[] i : arr) {
                for (String j : i) {
                    writer.write(j + ",");
                }

                writer.write("\n");
            }

            writer.close();

            System.out.println("( $ ) Session saved to file!");
        
        // Catch exception if unable to open file to write
        } catch (IOException e) {
            System.out.println("( !!! ) Fatal error occurred: ");
            System.out.println(e.getMessage());
        }
    }

    public static int readFromFile(String[][] arr, int stock) {
        // Locate "data.txt" and restore session with saved data
        // Requires String type 2d array as arr, returns burger stock
            if (stock != 0 || !arr[0][0].equals("_") || !arr[1][0].equals("_") || !arr[2][0].equals("_")) {
                System.out.println("( ! ) Error! Program is storing data! File read stopped to prevent data loss!");
                return stock;
            }

            try {
            File file = new File("data.txt");
            Scanner fileScan = new Scanner(file);
            
            while (fileScan.hasNextLine()) {
                // Remove delimiter before conversion to Integer
                stock = Integer.valueOf(fileScan.nextLine().replaceAll(",", ""));
                arr[0] = fileScan.nextLine().split(",");
                arr[1] = fileScan.nextLine().split(",");
                arr[2] = fileScan.nextLine().split(",");
            }

            fileScan.close();
        
        // Catch if file was not created before an attempted read
        } catch (FileNotFoundException e) {
            System.out.println("( ! ) Error! Saved data not found! Please save first!");
            return stock;

        // Catch if burger stock string cannot be converted to integer
        // or if file attempt to store data out of array
        } catch (NumberFormatException | IndexOutOfBoundsException e) {
            System.out.println("( ! ) Error! Unable to read some values! Possibly corrupted file!");
            return stock;
        }

        System.out.println("( $ ) Session restored from file!");
        return stock;
    }
}
