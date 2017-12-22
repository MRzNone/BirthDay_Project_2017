
#include "FastLED.h"
#include "test.h"
#define NUM_LEDS 8
CRGB leds[NUM_LEDS];
void setup() { FastLED.addLeds<NEOPIXEL, A1>(leds, NUM_LEDS); }

void loop() {
  int b = a[0][0][0];
  leds[b].red = 0;
  leds[b].green = 0;
  leds[b].blue = 0;
  FastLED.show();
  delay(500);
  leds[b].red = 0;
  leds[b].green = 255;
  leds[b].blue = 255;
  FastLED.show();
  delay(340);
}
