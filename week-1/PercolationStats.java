/******************************************************************************
 *  Compilation:  javac PercolationStats.java
 *  Execution:  java PercolationStats n T
 *  Dependencies: StdOut.java StdRandom.java StdStats.java Percolation.java
 *  
 *  Runs a Monte Carlo simulation of Percolation for a specified size and number
 *  of trials, then outputs relevant statistics.
 *
 ******************************************************************************/

/***
 * @author Vijay Umapathy
 */

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.StdOut;

public class PercolationStats {
    private static final double CONFIDENCE_95 = 1.96;
    private double sampleMean;
    private double sampleStdDev;
    private double[] samples;

    // perform trials independent experiments on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException("Must have positive number of trials and grid size");
        }
        else {
            Percolation percolation;
            this.samples = new double[trials];

            for (int i = 0; i < trials; i++) {
                percolation = null;
                percolation = new Percolation(n);
                while (!percolation.percolates()) {
                    int randomRow = StdRandom.uniform(n)+1;
                    int randomCol = StdRandom.uniform(n)+1;
                    while (percolation.isOpen(randomRow, randomCol)) {
                        randomRow = StdRandom.uniform(n)+1;
                        randomCol = StdRandom.uniform(n)+1;
                    }
                    percolation.open(randomRow, randomCol);
                }
                samples[i] = (double) percolation.numberOfOpenSites() / (double) (n*n);
            }

            this.sampleMean = StdStats.mean(samples);
            this.sampleStdDev = StdStats.stddev(samples);
        }
    }

    // sample mean of percolation threshold
    public double mean() {
        return this.sampleMean;
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return this.sampleStdDev;
    }

    // low  endpoint of 95% confidence interval
    public double confidenceLo() {
        return this.mean() - (CONFIDENCE_95 * this.stddev() / Math.sqrt(this.samples.length));
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return this.mean() + (CONFIDENCE_95 * this.stddev() / Math.sqrt(this.samples.length));
    }
 
    // test client (described below)
    public static void main(String[] args) {
        if (args.length != 2) {
            throw new IllegalArgumentException(
                "Should provide exactly 2 arguments to PercolationStats: n, T");
        }
        else {
            int n = Integer.parseInt(args[0]);
            int t = Integer.parseInt(args[1]);
            PercolationStats stats = new PercolationStats(n, t);
            StdOut.println("Mean                    = "+stats.mean());
            StdOut.println("Mean                    = "+stats.stddev());
            StdOut.println("95% confidence interval = ["
            +stats.confidenceLo()+", "+stats.confidenceHi()+"]");
        }
    }

 }