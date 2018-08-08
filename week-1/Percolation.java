/******************************************************************************
 *  Compilation:  javac Percolation.java
 *  Execution:  java Percolation < input.txt
 *  Dependencies: StdIn.java StdOut.java
 *  
 *  Models a Percolation scenario on a 2 dimensional grid of specified size and uses
 *  weighted quick union find to model filling of the grid and determine percolation.
 *
 ******************************************************************************/

/***
 * @author Vijay Umapathy
 */

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    // Data structure to house the grid
    private boolean[][] grid;
    private WeightedQuickUnionUF uf;
    private int numberOfOpenSites;

    // create n-by-n grid, with all sites blocked
    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException();
        } 
        else {
            // Create new UF with N*N grid plus virtual top and bottom sites at indices n*n and n*n+1, respectively
            this.uf = new WeightedQuickUnionUF(n*n+2);

            this.numberOfOpenSites = 0;
            grid = new boolean[n][n];
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    grid[i][j] = false;
                }
            }
        }
    }

    private boolean isValidCoordinate(int row, int col) {
        return !(row <= 0 || row > this.grid.length || col <= 0 || col > this.grid[0].length);
    }

    // open site (row, col) if it is not open already
    public void open(int row, int col) {
        if (!isValidCoordinate(row, col)) {
            throw new IllegalArgumentException();
        }    
        else if (!isOpen(row, col)) {
            grid[row-1][col-1] = true;
            this.numberOfOpenSites++;
            int site = this.grid.length*(row-1)+(col-1);

            // Connect to top virtual node if in top row
            if (row == 1) {
                uf.union(site, this.grid.length*this.grid.length);
            }

            // Connect to bottom virtual node if in bottom row
            if (row == this.grid.length) {
                uf.union(site, this.grid.length*this.grid.length+1);
            }
            
            // Fill left
            if (col > 1 && isOpen(row, col-1)) {
                uf.union(site, site-1);
            }
            // Fill right
            if (col < this.grid.length && isOpen(row, col+1)) {
                uf.union(site, site+1);
            }
            // Fill up
            if (row > 1 && isOpen(row-1, col)) {
                uf.union(site, site-this.grid.length);
            }
            // Fill down
            if (row < this.grid.length && isOpen(row+1, col)) {
                uf.union(site, site+this.grid.length);
            }
        }
    }   

    // is site (row, col) open?
    public boolean isOpen(int row, int col) {
        if (!isValidCoordinate(row, col)) {
            throw new IllegalArgumentException();
        }    
        else {
            return grid[row-1][col-1];
        }
    } 

    // is site (row, col) full?
    public boolean isFull(int row, int col) {
        if (!isValidCoordinate(row, col)) {
            throw new IllegalArgumentException();
        }    
        else {
            // Check if note is open and connected to virtual top node
            int n = this.grid.length;
            return (isOpen(row, col) && uf.connected(n*(row-1)+(col-1), n*n));
        }
    } 

    // number of open sites
    public int numberOfOpenSites() {
        return this.numberOfOpenSites;
    }   
    
    // does the system percolate?
    public boolean percolates() {
        // Check if the virtual top and bottom node are connected
        int n = this.grid.length;
        return uf.connected(n*n, n*n+1);
    }             
 
    // test client (optional)
    // USAGE:  java-algs4 Percolation < <TEST FILE NAME>
    public static void main(String[] args) {
        int n = StdIn.readInt();
        Percolation percolation = new Percolation(n);
        while (!StdIn.isEmpty()) {
            int p = StdIn.readInt();
            int q = StdIn.readInt();

            percolation.open(p, q);
            StdOut.println(p + " "+ q);
            // String gridState = "[";
            // for (int i = 0; i < n; i++) {
            //     gridState += "[";
            //     for (int j = 0; j < n; j++) {
            //         if (isFull(i+1, j+1)) {
            //             gridState += "Full";
            //         }
            //         else if (isOpen(i+1, j+1)) {
            //             gridState += "Open";
            //         }
            //         else {
            //             gridState += "Closed";
            //         }
            //         if (j < (n-1)) {
            //             gridState += ", ";
            //         }
            //     }
            //     gridState += "]";
            //     if (i < (n-1)) {
            //         gridState += ", ";
            //     }
            // }
            // gridState += "]";
            // StdOut.println("GRID: "+gridState);
            if (percolation.percolates()) {
                StdOut.println("Percolates!");
            }
        }
    }  
 }
 