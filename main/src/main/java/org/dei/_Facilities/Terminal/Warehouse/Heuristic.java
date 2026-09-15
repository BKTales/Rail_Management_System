package org.dei._Facilities.Terminal.Warehouse;

/**
 * Represents the different heuristic strategies used for item allocation
 * into trolleys or batches, based on their weight and available capacity.
 *
 * <p>These heuristics define how items are placed to optimize space utilization
 * and minimize the number of trolleys or batches needed.</p>
 *
 * <ul>
 *   <li><b>First Fit (FF):</b> Places each item into the first available trolley
 *   where it fits. The scan order follows the input order of the allocation rows.</li>
 *
 *   <li><b>First Fit Decreasing (FFD):</b> Sorts all unpicked allocations (from US02)
 *   by weight in descending order, then places each item in the first available
 *   batch where it fits.</li>
 *
 *   <li><b>Best Fit Decreasing (BFD):</b> Sorts all unpicked allocations (from US02)
 *   by weight in descending order, then places each item into the batch that results
 *   in the smallest remaining unused capacity — i.e., the tightest fit possible.</li>
 * </ul>
 */
public enum Heuristic {

    /** First Fit strategy — items are placed in the first trolley where they fit. */
    FF("First Fit"),

    /** First Fit Decreasing strategy — items are sorted by weight (descending) before applying First Fit. */ 
    FFD("First Fit Decreasing"),

    /** Best Fit Decreasing strategy — items are sorted by weight (descending) and placed for tightest fit. */
    BFD("Best Fit Decreasing");


    private final String name;


    /**
     * Constructs a heuristic type with a descriptive name.
     *
     * @param name the human-readable name of the heuristic
     */
    Heuristic(String name) {
        this.name = name;
    }

}

