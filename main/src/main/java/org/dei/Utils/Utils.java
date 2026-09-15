package org.dei.Utils;

import org.dei._Facilities.Terminal.Warehouse.Allocation;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Utils {


    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_YELLOW = "\u001B[33m";

    public static int readValue(String msg, int max, int min) {
        Scanner s = new Scanner(System.in);
        boolean frst = true;
        int i = -1;

        do {
            System.out.println(msg);
            String input = s.nextLine();

            if (!input.isEmpty()) {
                try {
                    i = Integer.parseInt(input);
                } catch (NumberFormatException e) { }
            }
            if (frst)
                msg = ANSI_YELLOW + "Bad answer, try again!" + ANSI_RESET + "\n" + msg; frst = false;
        } while (!(i < max && i >= min));
        return i;
    }

    public static double readDoubleValue(String msg, double max, double min) {
        Scanner s = new Scanner(System.in);
        boolean frst = true;
        double i = -1;

        do {
            System.out.println(msg);
            String input = s.nextLine();

            if (!input.isEmpty()) {
                try {
                    i = Double.parseDouble(input);
                } catch (NumberFormatException e) { }
            }
            if (frst)
                msg = ANSI_YELLOW + "Bad answer, try again!"  + ANSI_RESET + "\n" + msg; frst = false;
        } while (!(i < max && i >= min));
        return i;
    }

    public static boolean readValueYorN( String msg ) {
        Scanner s = new Scanner(System.in);
        boolean frst = true;
        String  answ;

        do {
            System.out.println(msg);
            answ = s.nextLine();

            if (frst)
                msg = ANSI_YELLOW + "Bad answer, try again!" + ANSI_RESET + "\n" + msg; frst = false;
        } while (!(answ.equals("y") || answ.equals("yes") || answ.equals("n") || answ.equals("no")));

        if (answ.equals("y") || answ.equals("yes"))
            return true;
        return false;
    }

    /**
     * Performs a merge sort on a list of {@link Allocation} objects in descending order
     * based on their weighted value.
     *
     * <p>This method creates a copy of the input list to preserve immutability.
     * If the list is {@code null} or contains fewer than two elements, it is returned as is.</p>
     *
     * @param allocations the list of {@link Allocation} objects to be sorted
     * @return a new list containing the sorted {@link Allocation} objects in descending order
     */
    public static List<Allocation> mergeSortAllocationDESC(List<Allocation> allocations) {
        if(allocations == null || allocations.size() < 2){
            return allocations;
        }

        List<Allocation> sortedList = new ArrayList<>(allocations);
        mergeSort(sortedList, 0, sortedList.size() - 1);
        return sortedList;
    }

    /**
     * Recursively divides the list into halves and sorts them using the merge sort algorithm.
     *
     * <p>This method operates directly on the provided list by sorting elements
     * between the specified left and right indices (inclusive).</p>
     *
     * @param list  the list of {@link Allocation} objects to be sorted
     * @param left  the starting index of the range to sort
     * @param right the ending index of the range to sort
     */
    public static void mergeSort(List<Allocation> list, int left, int right){
        if(left<right){
            int middle = (left + right) / 2;

            mergeSort(list, left, middle);
            mergeSort(list, middle + 1, right);
            merge(list, left, middle, right);
        }

    }


    /**
     * Merges two sorted sublists of {@link Allocation} objects into a single sorted segment
     * in descending order based on the {@code weighted} value.
     *
     * <p>The left sublist is defined by indices {@code [left, middle]} and the right sublist
     * by {@code [middle + 1, right]}.</p>
     *
     * @param list   the list of {@link Allocation} objects being sorted
     * @param left   the starting index of the left sublist
     * @param middle the ending index of the left sublist
     * @param right  the ending index of the right sublist
     */
    public static void merge(List<Allocation> list, int left, int middle, int right){
        List<Allocation> leftList = new ArrayList<>(list.subList(left, middle + 1));
        List<Allocation> rightList = new ArrayList<>(list.subList(middle + 1, right + 1));

        int i = 0, j = 0, k = left;

        while (i < leftList.size() && j < rightList.size()){
            if (leftList.get(i).getWeighted() >= rightList.get(j).getWeighted()){
                list.set(k++, leftList.get(i++));
            }else{
                list.set(k++, rightList.get(j++));
            }
        }
        while (i < leftList.size()){
            list.set(k++, leftList.get(i++));
        }

        while (j < rightList.size()){
            list.set(k++, rightList.get(j++));
        }

    }
}
