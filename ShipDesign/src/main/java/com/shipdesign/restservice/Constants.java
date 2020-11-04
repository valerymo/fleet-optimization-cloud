package com.shipdesign.restservice;

public class Constants {
    public static final float GRAVITY_ACCELERATION;

    // coefficient of protruding parts
    // ÐºÐ¾Ñ�Ñ„Ñ„Ð¸Ñ†Ð¸ÐµÐ½Ñ‚ Ð²Ñ‹Ñ�Ñ‚ÑƒÐ¿Ð°ÑŽÑ‰Ð¸Ñ… Ñ‡Ð°Ñ�Ñ‚ÐµÐ¹
    public static final float COEFF_PROTRUDING_PARTS;

    public static final float WATER_DENSITY;

    public static final float P04_PART_OF_DISPLAICEMENT;

    //ÑƒÐ´ÐµÐ»ÑŒÐ½Ð°Ñ� Ð³Ñ€ÑƒÐ·Ð¾Ð²Ð¼ÐµÑ�Ñ‚Ð¸Ð¼Ð¾Ñ�Ñ‚ÑŒ = 1.5
    public static final float RELATIVE_CARGO_CAPACITY;

    static {
        WATER_DENSITY = 1.025f;
        COEFF_PROTRUDING_PARTS = 1.01f;
        GRAVITY_ACCELERATION = 9.81f;
        P04_PART_OF_DISPLAICEMENT = 0.07f; //TMP. TBD - need define per vessel type, take from DB
        RELATIVE_CARGO_CAPACITY = 1.5f; //TBD
    }

}
