package org.westminster;

import java.util.Scanner;
import java.io.File;
import java.io.IOException;
import java.io.FileWriter;
import java.io.FileNotFoundException;

public class Main {
    public static void main(String[] args) {
        // Init vars
        Scanner scanner = new Scanner(System.in);
        int burgerStock = 0;
        int queueNo = 0;
        String[][] queue = {{"_", "_"}, {"_", "_", "_"}, {"_", "_", "_", "_", "_"}};
        
        System.out.println();
        printMenu();
        System.out.println();

        while (true) {
            if (burgerStock == 10) {
                System.out.println("( ! ) Warning! Burger Stock Low!");
                System.out.println();
            }
            System.out.print("( > ) Enter command: ");
            String command = scanner.nextLine().trim().toUpperCase();
            System.out.println();

            switch (command) {
                case "100": case "VFQ":
                    printQueue(queue);
                    System.out.println();
                    break;

                case "101": case "VEQ":
                    printEmptyQueue(queue);
                    System.out.println();
                    break;

                case "102": case "ACQ":
                    try {
                        System.out.print("( ? ) Enter customer name: ");
                        String customerName = scanner.nextLine().strip();
                        System.out.print("( ? ) Enter queue (1, 2, 3) : ");
                        queueNo = Integer.valueOf(scanner.nextLine()) - 1;
                        addToQueue(queue, customerName, queueNo);
                    
                    } catch (NumberFormatException e) {
                        System.out.println("( ! ) Error! Input not an Integer! Number input required!");
                    }

                    System.out.println();
                    break;

                case "103": case "RCQ":
                    try {
                        System.out.print("( ? ) Enter customer's queue (1, 2, 3): ");
                        queueNo = Integer.valueOf(scanner.nextLine()) - 1;
                        System.out.print("( ? ) Enter row (1, 2, 3, 4, 5): ");
                        int row = Integer.valueOf(scanner.nextLine()) - 1;
                        removeCustomer(queue, queueNo, row);
                    
                    } catch (NumberFormatException e) {
                        System.out.println("( ! ) Error! Input not an Integer! Number input required!");
                    } 
                    
                    System.out.println();
                    break;
                
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
                
                case "105": case "VCS":
                    sortCustomers(queue);
                    System.out.println();
                    break;

                case "106": case "SPD":
                    writeToFile(queue, burgerStock);
                    System.out.println();
                    break;

                case "107": case "LPD":
                    burgerStock = readFromFile(queue);
                    System.out.println();
                    break;

                case "108": case "STK":
                    System.out.println("( $ ) Remaining Stock: " + burgerStock);
                    System.out.println();
                    break;

                case "109": case "AFS":
                    try {
                        System.out.print("( ? ) Enter amount to be added: ");
                        burgerStock += Integer.valueOf(scanner.nextLine());
                        System.out.println("( $ ) Successfully added to stock!");
                    
                    } catch (NumberFormatException e) {
                        System.out.println("( ! ) Error! Input not an Integer! Number input required!");
                    }
                    
                    System.out.println();
                    break;

                case "111": case "PMN":
                    printMenu();
                    System.out.println();
                    break;

                case "999": case "EXT":
                    System.out.println("Exiting...");
                    scanner.close();
                    System.out.println();
                    return;

                default: 
                    System.out.println("( ! ) Error! No such command found!");
                    System.out.println();
            }
        }

    }

    public static void printMenu() {
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

    public static void printQueue(String[][] queue) {
        // Create queue header
        System.out.println("    *****************");
        System.out.println("    *    Cashiers   *");
        System.out.println("    *****************");

        int cashier = 0;
        int index = 0;
        System.out.print("        ");
        while (true) {
            if (cashier == 3) {
                System.out.println();
                System.out.print("        ");
                index++;
                cashier = 0;
            }

            if (index == 5) {
                break;
            }

            if (index < queue[cashier].length) {
                if (queue[cashier][index].equals("_")) {
                    System.out.print("X");
                
                } else {
                    System.out.print("O");
                }
            
            } else {
                System.out.print(" ");
            }
            
            System.out.print("   ");
            cashier++;
        }
        
        System.out.println();
        System.out.println("O - Occupied    X - Not Occupied");
    }

    public static void printEmptyQueue(String[][] arr) {
        System.out.println("  *********************");
        System.out.println("  *   Empty Cashiers  *");
        System.out.println("  *********************");

        boolean firstQueueEmpty = hasEmptySpots(arr[0]);
        boolean secondQueueEmpty = hasEmptySpots(arr[1]);
        boolean thirdQueueEmpty = hasEmptySpots(arr[2]);

        int cashier = 0;
        int index = 0;
        System.out.print("        ");
        while (true) {
            if (cashier == 3) {
                System.out.println();
                System.out.print("        ");
                index++;
                cashier = 0;
            }

            if (index == 5) {
                break;
            }

            if (cashier == 0 && firstQueueEmpty && index < 2) {
                if (arr[0][index] == "_") {
                    System.out.print("X");
                
                } else {
                    System.out.print("O");
                }
            
            } else {
                System.out.print(" ");
            }

            if (cashier == 1 && secondQueueEmpty && index < 3) {
                if (arr[1][index] == "_") {
                    System.out.print("X");
                
                } else {
                    System.out.print("O");
                }
            
            } else {
                System.out.print(" ");
            }

            if (cashier == 2 && thirdQueueEmpty && index < 5) {
                if (arr[2][index] == "_") {
                    System.out.print("X");
                
                } else {
                    System.out.print("O");
                }
            
            } else {
                System.out.print(" ");
            }
            
            cashier++;
        }
        
        System.out.println();
        System.out.println("O - Occupied    X - Not Occupied");
    }

    public static void addToQueue(String[][] arr, String customerName, int queueNo) {
        if (queueNo == 0 || queueNo == 1 || queueNo == 2) {
            if (!hasEmptySpots(arr[queueNo])) {
                System.out.println("( ! ) Error! No empty slots in queue!");
            
            } else {
                for (int i = 0; i < arr[queueNo].length; i++) {
                    if (arr[queueNo][i] == "_") {
                        arr[queueNo][i] = customerName;
                        System.out.println("( $ ) Successfully added to queue!");
                        return;
                    }
                }
            }
        
        } else {
            System.out.println("( ! ) Error! No such queue found!");
        }
        
    }

    public static boolean hasEmptySpots(String[] arr) {
        for (String i : arr) {
            if (i == "_") {
                return true;
            }
        }

        return false;
    }

    public static void removeCustomer(String[][] arr, int queueNo, int row) {
        if (queueNo == 0 || queueNo == 1 || queueNo == 2) {
            if (row < arr[queueNo].length) {
                if (arr[queueNo][row] != "_") {
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

        moveForward(arr);
        System.out.println("( $ ) Successfully removed from queue!");
    }

    public static void moveForward(String[][] arr) {
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[i].length; j++) {
                if (j != arr[i].length - 1) {
                    if (arr[i][j + 1] != "_" && arr[i][j] == "_") {
                        arr[i][j] = arr[i][j + 1];
                        arr[i][j + 1] = "_";
                    }
                }
            }
        }
    }

    public static int removeServedCustomer(String[][] arr, int burgerStock, int queueNo) {
        if (queueNo == 0 || queueNo == 1 || queueNo == 2) {
            if (arr[queueNo][0] != "_") {
                arr[queueNo][0] = "_";
                moveForward(arr);
                System.out.println("( $ ) Successfully served customer!");
                return burgerStock -= 5;
            } else {
                System.out.println("( ! ) Error! No customers in queue!");
                return burgerStock;
            }

        } else {
            System.out.println("( ! ) Error! No such queue Found!");
            return burgerStock;
        }
    }

    public static void sortCustomers(String[][] arr) {
        int numberOfCustomers = 0;

        for (String[] i : arr) {
            for (String j : i) {
                if (j != "_") {
                    numberOfCustomers++;
                
                }
            }
        }

        String[] customerArr = new String[numberOfCustomers];

        int arrIndex = 0;

        for (String[] i : arr) {
            for (String j : i) {
                if (j != "_") {
                    customerArr[arrIndex] = j;
                    arrIndex++;
                }
            }
        }

        for (int i = 0; i < numberOfCustomers; i++) {
            for (int j = 0; j < numberOfCustomers; j++) {
                if (customerArr[i].compareTo(customerArr[j]) < 0) {
                    String temp = customerArr[i];
                    customerArr[i] = customerArr[j];
                    customerArr[j] = temp;
                
                }
            }
        }

        System.out.println("( $ ) Displaying all customers in alphabetical order: ");
        System.out.println();
        for (String i : customerArr) {
            if (!i.equals("_")) {
                System.out.println("    " + i);
            }
        }
    }

    public static void writeToFile(String[][] arr, int burgerStock) {
        try {
            File file = new File("data.txt");
            FileWriter writer = new FileWriter(file);

            writer.write(burgerStock + ",\n");

            for (String[] i : arr) {
                for (String j : i) {
                    writer.write(j + ",");
                }

                writer.write("\n");
            }

            writer.close();

            System.out.println("( $ ) Session saved to file!");
        
        } catch (IOException e) {
            System.out.println("( !!! ) Fatal error occured: ");
            e.printStackTrace();
        }
    }

    public static int readFromFile(String[][] arr) {
        int stock = 0;
        
        try {
            File file = new File("data.txt");
            Scanner fileScan = new Scanner(file);
            
            while (fileScan.hasNextLine()) {
                stock = Integer.valueOf(fileScan.nextLine().replaceAll(",", ""));
                arr[0] = fileScan.nextLine().split(",");
                arr[1] = fileScan.nextLine().split(",");
                arr[2] = fileScan.nextLine().split(",");
            }

            fileScan.close();
        
        } catch (FileNotFoundException e) {
            System.out.println("( ! ) Error! Saved data not found! Please save first!");
            return stock;

        }

        System.out.println("( $ ) Session restored from file!");
        return stock;
    }
}
