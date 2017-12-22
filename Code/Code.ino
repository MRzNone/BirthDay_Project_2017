#include <SD.h>
#include "FastLED.h"

#define CS 4
#define CYCLES 340
#define NUM_OF_LEDS 15
#define BYTES_PER_LINE NUM_OF_LEDS * 3

CRGB leds[NUM_OF_LEDS];

void setup() {
  FastLED.addLeds<NEOPIXEL, A0> (leds, NUM_OF_LEDS);
  Serial.begin(9600);
  Serial.println("Initializing SD Card");
  pinMode(CS, OUTPUT);

  // start SD
  if (!SD.begin(CS))
  {
    Serial.println("Card Fail");
    return;
  }
  Serial.println("Card Ready");
}

void loop() {
  loopPattern("export");
}



void loopPattern(String fileName)
{
  File file = SD.open(fileName);
  byte bytes[BYTES_PER_LINE];
  int index = 0;
  
  for(int i = 0; i < CYCLES; i++)
  {
    // read bytes
    file.read(bytes, BYTES_PER_LINE);
    // assign leds
    index = 0;
    for (int led = 0; led < NUM_OF_LEDS; led++)
    {
      
      leds[led].red = bytes[index++];
      leds[led].green = bytes[index++];
      leds[led].blue = bytes[index++];
    }

    FastLED.show();
    delay(1);
  }
}
