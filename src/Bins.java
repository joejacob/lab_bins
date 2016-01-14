import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Scanner;

/**
 * Runs a number of algorithms that try to fit files onto disks.
 */
public class Bins {
    public static final String DATA_FILE = "example.txt";

    /**
     * Reads list of integer data from the given input.
     *
     * @param input tied to an input source that contains space separated numbers
     * @return list of the numbers in the order they were read
     */
    public List<Integer> readData (Scanner input) {
        List<Integer> results = new ArrayList<Integer>();
        while (input.hasNext()) {
            results.add(input.nextInt());
        }
        return results;
    }

    /**
     * The main program.
     */
    public static void main (String args[]) {
        Bins b = new Bins();
        Scanner input = new Scanner(Bins.class.getClassLoader().getResourceAsStream(DATA_FILE));
        List<Integer> data = b.readData(input);

        // testing for valid inputs
        int total = validInput(data);     
        if(total == -1) {
        	System.out.println("invalid input");
        	return;
        }
        System.out.println("total size = " + total / 1000000.0 + "GB");
        
        // testing each algorithm
        // in-order
        PriorityQueue<Disk> inorderResult = worstFitInOrder(data);       
        System.out.println();
        System.out.println("worst-fit method");
        printStats(inorderResult);
       
        // in-order-decreasing
        Collections.sort(data, Collections.reverseOrder());
        PriorityQueue<Disk> inorderDecreasingResult = worstFitInOrder(data);       
        System.out.println();
        System.out.println("worst-fit decreasing method");
        printStats(inorderDecreasingResult);
    }
    
    private static int validInput(List<Integer> inputData) {
    	// total size for all of the inputs
    	int totalSize = 0;
    	
    	// iterate through the files and make sure the sizes are within [0,1000000] and return total file size
    	// if not, return -1
    	for(int fileSize : inputData) {
    		totalSize += fileSize;
    		if(fileSize > 1000000 || fileSize <0) {
    			return -1;
    		}
    	}
    	
    	return totalSize;
    }
    
    private static PriorityQueue<Disk> worstFitInOrder (List<Integer> files) {
    	PriorityQueue<Disk> allDisks = new PriorityQueue<Disk>();
        allDisks.add(new Disk(0));

     // implements the worst fit in-order heuristic algorithm
        int diskId = 1;
        for (Integer size : files) {
            Disk currDisk = allDisks.peek();
            
            if (currDisk.freeSpace() >= size) {
            	 // if there's still space in the current disk
                allDisks.poll();
                currDisk.add(size);
                allDisks.add(currDisk);
            } else {
            	// if a new disk is needed
                Disk currDisk2 = new Disk(diskId);
                diskId++;
                currDisk2.add(size);
                allDisks.add(currDisk2);
            }
        }
        
        return allDisks;
    }
    
    private static void printStats(PriorityQueue<Disk> results) {
    	// prints the contents of the priority queue
    	 System.out.println("number of pq used: " + results.size());
         while (!results.isEmpty()) {
             System.out.println(results.poll());
         }
         System.out.println();

    }
}
