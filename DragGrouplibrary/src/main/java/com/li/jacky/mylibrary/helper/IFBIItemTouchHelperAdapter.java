package com.li.jacky.mylibrary.helper;


public interface IFBIItemTouchHelperAdapter {

    boolean onItemMove(int fromPosition, int toPosition);

    boolean canMove(int source, int target);
}
