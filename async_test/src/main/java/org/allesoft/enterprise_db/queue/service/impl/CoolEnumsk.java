package org.allesoft.enterprise_db.queue.service.impl;

public enum CoolEnumsk {
    TUESDAY(2),
    MONDAY(1),
    WEDNESDAY(3),
    THURSDAY(4),
    FRIDAY(5),
    SATURDAY(6),
    SUNDAY(7),
    ;

    int dayNumber;

    CoolEnumsk(int dayNumber) {
        this.dayNumber = dayNumber;
    }

    public static void main(String[] args) {
        for (CoolEnumsk enumsk : CoolEnumsk.values()) {
            System.out.println("Day " + (enumsk.ordinal() + 1) + " - " + enumsk.name());
            System.out.println("Day " + (enumsk.dayNumber) + " - " + enumsk.name());
        }
    }
}
