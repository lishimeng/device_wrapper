package com.thingple.tagservice.device.vendor;

/**
 *
 * Created by lism on 2017/11/3.
 */
public enum TagArea {

    TID(1),EPC(2),USER(3);

    private int bank;

    private TagArea(int bank) {
        this.bank = bank;
    }

    public int getBank() {
        return bank;
    }
}
