package com.nhaarman.listviewanimations.extras.itemmanipulation;

// TODO integrate in ExpandableListItemAdapter
public interface ExpandCollapseListener {

    public void onItemExpanded(int position);

    public void onItemCollapsed(int position);

}
