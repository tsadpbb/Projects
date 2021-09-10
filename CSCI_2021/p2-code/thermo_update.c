#include "thermo.h"
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

int set_temp_from_ports(temp_t *temp) {
    if (THERMO_SENSOR_PORT > 64000 || THERMO_SENSOR_PORT < 0) {
        return 1;
    }
    int decVal = THERMO_SENSOR_PORT / 64 - 500;
    int remain = THERMO_SENSOR_PORT % 64;
    int mask = 0b0001;
    if (remain > 31) { decVal++; }          // Checks if the remainder is high enough to round up
    temp->is_fahrenheit = 0;
    if (THERMO_STATUS_PORT & mask) {        // Conversion to farenheit if neccesary, apparently rounding is always down
        decVal = (decVal * 9) / 5 + 320;
        temp->is_fahrenheit = 1;
    }
    temp->tenths_degrees = decVal;
    return 0;
}
// Uses the two global variables (ports) THERMO_SENSOR_PORT and
// THERMO_STATUS_PORT to set the temp structure. If THERMO_SENSOR_PORT
// is above its maximum trusted value, associated with +50.0 deg C,
// does not alter temp and returns 1.  Otherwise, sets fields of temp
// based on converting sensor value to degrees and checking whether
// Celsius or Fahrenheit display is in effect. Returns 0 on successful
// set. This function DOES NOT modify any global variables but may
// access global variables.
//
// CONSTRAINT: Uses only integer operations. No floating point
// operations are used as the target machine does not have a FPU.

int set_display_from_temp(temp_t temp, int *display) {
    // These if's check to make sure everything is within the right boundary
    if (temp.is_fahrenheit) {
        if (temp.tenths_degrees > 9999 || temp.tenths_degrees < -999) { return 1; }    
        if (temp.is_fahrenheit != 1) { return 1; }
    }
    else if (temp.tenths_degrees > 500 || temp.tenths_degrees < -500) { return 1; }
    // The array of masks
    int mask[] = {  0b1111110,
                    0b0001100,
                    0b0110111,
                    0b0011111,
                    0b1001101,
                    0b1011011,
                    0b1111011,
                    0b0001110,
                    0b1111111,
                    0b1011111,
                 } ;
    int retVal = 0;
    // It has to be abs otherwise things get messed up with mod and division
    int one = abs(temp.tenths_degrees) % 10;
    int ten = (abs(temp.tenths_degrees) % 100) / 10;
    int hun = abs(temp.tenths_degrees) / 100;
    // This if else block adds 10 or 01 to the beginning of retVal
    if (temp.is_fahrenheit) { retVal |= 0b10 << 28;; }
    else { retVal |= 0b01 << 28;; }
    // First case handles if hun is not zero, hun stays unchanged if it's not above 9 
    if (hun != 0) {
        int ton = hun / 10;
        hun %= 10;
        if (ton != 0) { retVal |= mask[ton] << 21; }
        else if (temp.tenths_degrees < 0) { retVal |= 0b00000001 << 21; }
        retVal |= mask[hun] << 14;
    }
    else if (temp.tenths_degrees < 0) { retVal |= 0b0000001 << 14; }
    retVal |= mask[ten] << 7;
    retVal |= mask[one];
    *display = retVal;
    return 0;
}
// Alters the bits of integer pointed to by display to reflect the
// temperature in struct arg temp.  If temp has a temperature value
// that is below minimum or above maximum temperature allowable or if
// an improper indication of celsius/fahrenheit is given, does nothing
// and returns 1. Otherwise, calculates each digit of the temperature
// and changes bits at display to show the temperature according to
// the pattern for each digit.  This function DOES NOT modify any
// global variables but may access global variables.

// I do the work on retVal before THERMO_DISPLAY_PORT to check for errors
int thermo_update() {
    temp_t set;
    int retVal = 0;
    if (set_temp_from_ports(&set)) { return 1; }
    if (set_display_from_temp(set, &retVal)) { return 1; }
    THERMO_DISPLAY_PORT = retVal;
    return 0;
}
// Called to update the thermometer display.  Makes use of
// set_temp_from_ports() and set_display_from_temp() to access
// temperature sensor then set the display. Checks these functions and
// if they indicate an error, makes not changes to the display.
// Otherwise modifies THERMO_DISPLAY_PORT to set the display.
// 
// CONSTRAINT: Does not allocate any heap memory as malloc() is NOT
// available on the target microcontroller.  Uses stack and global
// memory only.
