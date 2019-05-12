package com.example.quiztaker;

/**
 * Interface that implements the onBackPressed method.
 * Used in all fragments that implement it to handle the back pressed button.
 */
public interface OnBackPressed {
    /**
     *
     * @return Returns whether event is handled in ths method
     */
    boolean onBackPressed();
}
