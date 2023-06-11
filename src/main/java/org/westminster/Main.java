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
        String[][] queue = {{"", ""}, {"", "", ""}, {"", "", "", "", ""}};

        while (true) {
            if (burgerStock == 10) {
                System.out.println("Warning! Burger Stock Low!");
                System.out.println();
            }
            System.out.print("Enter command: ");
            String command = scanner.nextLine().trim().toUpperCase();

            switch (command) {
                case "100": case "VFQ":
                    printQueue(queue);
                    break;

                case "101": case "VEQ":
                    printEmptyQueue(queue);
                    break;

                case "102": case "ACQ":
                    System.out.print("Enter customer name: ");
                    String customerName = scanner.nextLine().strip();
                    System.out.print("Enter queue (1, 2, 3) : ");
                    queueNo = Integer.valueOf(scanner.nextLine()) - 1;

                    addToQueue(queue, customerName, queueNo);
                    break;

                case "103": case "RCQ":
                    System.out.print("Enter customer's queue (1, 2, 3): ");
                    queueNo = Integer.valueOf(scanner.nextLine()) - 1;
                    System.out.print("Enter row (1, 2, 3, 4, 5): ");
                    int row = Integer.valueOf(scanner.nextLine()) - 1;
                    removeCustomer(queue, queueNo, row);
                    break;
                
                case "104": case "PCQ":
                    System.out.print("Enter queue: ");
                    queueNo = Integer.valueOf(scanner.nextLine()) - 1;
                    burgerStock = removeServedCustomer(queue, burgerStock, queueNo);
                    break;
                
                case "105": case "VCS":
                    sortCustomers(queue);
                    break;

                case "106": case "SPD":
                    writeToFile(queue, burgerStock);
                    break;

                case "107": case "LPD":
                    burgerStock = readFromFile(queue);
                    break;

                case "108": case "STK":
                    System.out.println("Remaining Stock: " + burgerStock);
                    break;

                case "109": case "AFS":
                    System.out.print("Amount to be added: ");
                    burgerStock += Integer.valueOf(scanner.nextLine());
                    break;

                case "999": case "EXT":
                    System.out.println("Exiting...");
                    return;
            }
        }

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
                if (queue[cashier][index].equals("")) {
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
                if (arr[0][index] == "") {
                    System.out.print("X");
                
                } else {
                    System.out.print("O");
                }
            
            } else {
                System.out.print(" ");
            }

            if (cashier == 1 && secondQueueEmpty && index < 3) {
                if (arr[1][index] == "") {
                    System.out.print("X");
                
                } else {
                    System.out.print("O");
                }
            
            } else {
                System.out.print(" ");
            }

            if (cashier == 2 && thirdQueueEmpty && index < 5) {
                if (arr[2][index] == "") {
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
                System.out.println("Error! No empty slots in queue!");
                System.out.println();
            
            } else {
                for (int i = 0; i < arr[queueNo].length; i++) {
                    if (arr[queueNo][i] == "") {
                        arr[queueNo][i] = customerName;
                        return;
                    }
                }
            }
        
        } else {
            System.out.println("Error! No such queue found!");
            System.out.println();
        }
        
    }

    public static boolean hasEmptySpots(String[] arr) {
        for (String i : arr) {
            if (i == "") {
                return true;
            }
        }

        return false;
    }

    public static void removeCustomer(String[][] arr, int queueNo, int row) {
        if (queueNo == 0 || queueNo == 1 || queueNo == 2) {
            if (row < arr[queueNo].length) {
                if (arr[queueNo][row] != "") {
                    arr[queueNo][row] = "";
                
                } else {
                    System.out.println("Error! No customer in position!");
                    return;
                }
            
            } else {
                System.out.println("Error! No such row found!");
                return;
            }
        
        } else {
            System.out.println("Error! No such queue found!");
            return;
        }

        moveForward(arr);
    }

    public static void moveForward(String[][] arr) {
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[i].length; j++) {
                if (j != arr[i].length - 1) {
                    if (arr[i][j + 1] != "" && arr[i][j] == "") {
                        arr[i][j] = arr[i][j + 1];
                        arr[i][j + 1] = "";
                    }
                }
            }
        }
    }

    public static int removeServedCustomer(String[][] arr, int burgerStock, int queueNo) {
        if (queueNo == 0 || queueNo == 1 || queueNo == 2) {
            if (arr[queueNo][0] != 0) {
                arr[queueNo][0] = "";
                moveForward(arr);
                return burgerStock -= 5;
            } else {
                System.out.println("Error! No customers in queue!");
                return burgerStock;
            }

        } else {
            System.out.println("Error! No such queue Found!");
            return burgerStock;
        }
    }

    public static void sortCustomers(String[][] arr) {
        int numberOfCustomers = 0;

        for (String[] i : arr) {
            for (String j : i) {
                if (j != "") {
                    numberOfCustomers++;
                
                }
            }
        }

        String[] customerArr = new String[numberOfCustomers];

        int arrIndex = 0;

        for (String[] i : arr) {
            for (String j : i) {
                if (j != "") {
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

        System.out.println("All customers in alphabetical order: ");
        for (String i : customerArr) {
            System.out.println(i);
        }
    }

    public static void writeToFile(String[][] arr, int burgerStock) {
        try {
            File file = new File("data.txt");

            FileWriter writer = new FileWriter(file);

            System.out.println(burgerStock);

            writer.write(burgerStock + ",\n");

            for (String[] i : arr) {
                for (String j : i) {
                    writer.write(j + ",");
                }

                writer.write("\n");
            }

            writer.close();

            System.out.println("Successfully written to file");
        
        } catch (IOException e) {
            System.out.println("An error Occured: ");
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
            System.out.println("Error! File not found!");
            e.printStackTrace();
        }

        return stock;
    }
}
