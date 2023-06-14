package com.codeclause.smsapp.adapters


interface ItemCLickListener {
    fun itemClicked(color: Int, contact: String?, savedContactName: String?, id: Long, read: String?) //void itemLongClicked(int position,String contact,long id);
}