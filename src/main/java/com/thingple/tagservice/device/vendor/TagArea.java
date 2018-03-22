package com.thingple.tagservice.device.vendor;

/**
 *
 * Created by lism on 2017/11/3.
 */
public enum TagArea {

    EPC(1), TID(2), USER(3);

    private int bank;

    TagArea(int bank) {
        this.bank = bank;
    }

    public int getBank() {
        return bank;
    }

    public static TagArea getBank(int bank) {
        if (bank == 1) {
            return EPC;
        } else if (bank == 2) {
            return TID;
        } else if (bank == 3) {
            return USER;
        } else {
            return null;
        }
    }
}
