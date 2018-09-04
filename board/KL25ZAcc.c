
/*
  KL25ZAcc

  This program transmits information of the accelerometer's device and the touch panel. It uses the serial port.

  Sketch created 4 Sept 2018
  by Jorge Ortiz Escribano

  http://jorgeortizesc.com/es/space-war.html

*/


#include "mbed.h"
#include "MMA8451Q.h"
#include "TSISensor.h"

PinName const SDA = PTE25;
PinName const SCL = PTE24;

#define MMA8451_I2C_ADDRESS (0x1d<<1)

// Create serial connection
Serial pc(USBTX, USBRX);

float threshold = 0.3;

int main(void)
{
    // Acquire data from the accelerometer device:
    MMA8451Q acc(SDA, SCL, MMA8451_I2C_ADDRESS);
    
    // Initialize the touch pad sensor
    TSISensor tsi;
    
    // Every 0.01 seconds, we send information related to the accelerometer and the touch pad:
    while (true) {
        float x, y, z, but;
        x = acc.getAccX();
        y = acc.getAccY();
        z = acc.getAccZ();
        but = tsi.readPercentage();
        
        if (y > threshold || y < -threshold)
            pc.printf("X%f\n", y);
        if (x > threshold || x < -threshold)
            pc.printf("Y%f\n", x);
        if (z > threshold || z < -threshold)
            pc.printf("Z%f\n", z);
            
        if (but >= 0.1 && but <= 0.9)
            pc.printf("B0.0\n");
        
        wait(0.01);
    }
}

