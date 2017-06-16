package com.li.jacky.draggrouprecyclerview.helper;


public interface IFBIItemTouchHelperAdapter {

    boolean onItemMove(int fromPosition, int toPosition);

    boolean canMove(int source, int target);
}
