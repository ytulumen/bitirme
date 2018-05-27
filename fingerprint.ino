/*************************************************** 
  This is an example sketch for our optical Fingerprint sensor

  Designed specifically to work with the Adafruit BMP085 Breakout 
  ----> http://www.adafruit.com/products/751

  These displays use TTL Serial to communicate, 2 pins are required to 
  interface
  Adafruit invests time and resources providing this open source code, 
  please support Adafruit and open-source hardware by purchasing 
  products from Adafruit!

  Written by Limor Fried/Ladyada for Adafruit Industries.  
  BSD license, all text above must be included in any redistribution
 ****************************************************/


#include <Adafruit_Fingerprint.h>

// On Leonardo/Micro or others with hardware serial, use those! #0 is green wire, #1 is white
// uncomment this line:
// #define mySerial Serial1

// For UNO and others without hardware serial, we must use software serial...
// pin #2 is IN from sensor (GREEN wire)
// pin #3 is OUT from arduino  (WHITE wire)
// comment these two lines if using hardware serial
#include <SoftwareSerial.h>
SoftwareSerial mySerial(2, 3);

Adafruit_Fingerprint finger = Adafruit_Fingerprint(&mySerial);

uint8_t id;


void setup()  
{
  Serial.begin(9600);
  while (!Serial);  // For Yun/Leo/Micro/Zero/...
  delay(100);
  //Serial.println("\n\nAdafruit finger detect test");

  // set the data rate for the sensor serial port
  finger.begin(57600);
  
  if (finger.verifyPassword()) {
    Serial.println("Found");
  } else {
    Serial.println("NotFound");
    while (1) { delay(1); }
  }
}

void loop()                     // run over and over again
{
  int readNumber = readnumber();
  if(readNumber == 1){
    enrollFP();
  }
  else if(readNumber == 2){
    searchFP();
  }
  else if(readNumber == 3){
    deleteFP();
  }
  delay(50);            //don't ned to run this at full speed.
}

uint8_t readnumber(void) {
  uint8_t num = 0;
  
  while (num == 0) {
    while (! Serial.available());
    num = Serial.parseInt();
  }
  return num;
}

////////////////////////////////////////////////////////////////////
///////////// ENROLLING FOR FINGERPRINT ////////////////////////////
////////////////////////////////////////////////////////////////////

int enrollFP(){
  Serial.println("Enroll");
  //Serial.println("Please type in the ID # (from 1 to 127) you want to save this finger as...");
  id = readnumber();
  if (id == 0) {// ID #0 not allowed, try again!
     return;
  }
  //Serial.print("Enrolling ID #");
  //Serial.println(id);
  //delay(3000);
  uint8_t p = finger.getImage();
  while(p != FINGERPRINT_OK){
    p = finger.getImage();
  }
  getFingerprintEnroll();
}

uint8_t getFingerprintEnroll() {

  int p = -1;
  //Serial.print("Waiting for valid finger to enroll as #"); Serial.println(id);
  if (p != FINGERPRINT_OK) {
    p = finger.getImage();
    if(p == FINGERPRINT_OK){
      Serial.println("Image taken");
    }
    else if(p == FINGERPRINT_NOFINGER){
      Serial.println("No finger");
    }
    else if(p == FINGERPRINT_PACKETRECIEVEERR){
      Serial.println("Communication error");
    }
    else if(p == FINGERPRINT_IMAGEFAIL){
      Serial.println("Imaging error");
    }
    else{
      Serial.println("Unknown error1");
    }
  }
  else{
    return -1;
  }

  // OK success!

  p = finger.image2Tz(1);
  if(p == FINGERPRINT_OK){
    Serial.println("Image converted");
  }
  else if(p == FINGERPRINT_IMAGEMESS){
    Serial.println("Image too messy");
  }
  else if(p == FINGERPRINT_PACKETRECIEVEERR){
    Serial.println("Communication error");
  }
  else if(p == FINGERPRINT_FEATUREFAIL){
    Serial.println("Not found1");
  }
  else if(p == FINGERPRINT_INVALIDIMAGE){
    Serial.println("Not found1");    
  }
  else{
    Serial.println("Unknown error1");
  }
    
  Serial.println("Remove finger");
  //delay(2000);
  p = 0;
  while (p != FINGERPRINT_NOFINGER) {
    p = finger.getImage();
  }

  p = -1;
  Serial.println("Again");
  while(p != FINGERPRINT_OK){
    p = finger.getImage();
  }
  //delay(3000);
  if (p != FINGERPRINT_OK) {
    p = finger.getImage();
    if(p == FINGERPRINT_OK){
      Serial.println("Image taken");
    }
    else if(p == FINGERPRINT_NOFINGER){
      Serial.println("No finger");
    }
    else if(p == FINGERPRINT_PACKETRECIEVEERR){
      Serial.println("Communication error");
    }
    else if(p == FINGERPRINT_IMAGEFAIL){
      Serial.println("Imaging error");
    }
    else{
      Serial.println("Unknown error1");
    }
  }

  // OK success!

  p = finger.image2Tz(2);
  if(p == FINGERPRINT_OK){
    Serial.println("Image converted");
  }
  else if(p == FINGERPRINT_IMAGEMESS){
    Serial.println("Image too messy");
  }
  else if(p == FINGERPRINT_PACKETRECIEVEERR){
    Serial.println("Communication error");
  }
  else if(p == FINGERPRINT_FEATUREFAIL){
    Serial.println("Not found1");
  }
  else if(p == FINGERPRINT_INVALIDIMAGE){
    Serial.println("Not found1");    
  }
  else{
    Serial.println("Unknown error1");
  }

  /*
  switch (p) {
    case FINGERPRINT_OK:
      Serial.println("Image converted");
      break;
    case FINGERPRINT_IMAGEMESS:
      Serial.println("Image too messy");
     // return p;
    case FINGERPRINT_PACKETRECIEVEERR:
      Serial.println("Communication error");
      //return p;
    case FINGERPRINT_FEATUREFAIL:
      Serial.println("Not found1");
      //return p;
    case FINGERPRINT_INVALIDIMAGE:
      Serial.println("Not found1");
      //return p;
    default:
      Serial.println("Unknown error1");
      //return p;
  }*/
  
  // OK converted!
  //Serial.print("Creating model for #");  Serial.println(id);
  
  p = finger.createModel();
  if (p == FINGERPRINT_OK) {
    Serial.println("Matched");
  } else if (p == FINGERPRINT_PACKETRECIEVEERR) {
    Serial.println("Communication error");
    //return p;
  } else if (p == FINGERPRINT_ENROLLMISMATCH) {
    Serial.println("Not match");
    //return p;
  } else {
    Serial.println("Unknown error");
    //return p;
  }   
  
  //Serial.print("ID "); Serial.println(id);
  p = finger.storeModel(id);
  if (p == FINGERPRINT_OK) {
    Serial.println("Stored!");
  } else if (p == FINGERPRINT_PACKETRECIEVEERR) {
    Serial.println("Communication error");
    return p;
  } else if (p == FINGERPRINT_BADLOCATION) {
    Serial.println("Location error");
    return p;
  } else if (p == FINGERPRINT_FLASHERR) {
    Serial.println("Write error");
    return p;
  } else {
    Serial.println("Unknown error");
    return p;
  }   
}

////////////////////////////////////////////////////////////////////
///////////// DELETING FOR FINGERPRINT /////////////////////////////
////////////////////////////////////////////////////////////////////
int deleteFP(){
  Serial.println("Delete");
  uint8_t id = readnumber();
  if (id == 0) {// ID #0 not allowed, try again!
     return;
  }

  //Serial.print("Deleting ID #");
  //Serial.println(id);
  
  deleteFingerprint(id);
}

uint8_t deleteFingerprint(uint8_t id) {
  uint8_t p = -1;
  
  p = finger.deleteModel(id);

  if (p == FINGERPRINT_OK) {
    Serial.println("Deleted");
  } else if (p == FINGERPRINT_PACKETRECIEVEERR) {
    Serial.println("Communication error");
    return p;
  } else if (p == FINGERPRINT_BADLOCATION) {
    Serial.println("Could not delete in that location");
    return p;
  } else if (p == FINGERPRINT_FLASHERR) {
    Serial.println("Error writing to flash");
    return p;
  } else {
    Serial.print("Unknown error");
    return p;
  }   
}


////////////////////////////////////////////////////////////////////
///////////// SEARCHING FOR FINGERPRINT ////////////////////////////
////////////////////////////////////////////////////////////////////

int searchFP(){
  finger.getTemplateCount();
  //Serial.print("Sensor contains "); Serial.print(finger.templateCount); Serial.println(" templates");
  uint8_t p = finger.getImage();
  Serial.println("Search");
  while(p != FINGERPRINT_OK){
    p = finger.getImage();
  }
  //delay(1000);
  Serial.print(getFingerprintIDez());
  return ;    
}

// returns -1 if failed, otherwise returns ID #
int getFingerprintIDez() {
  uint8_t p = finger.getImage();
  if (p != FINGERPRINT_OK)  return -1;

  p = finger.image2Tz();
  if (p != FINGERPRINT_OK)  return -1;

  p = finger.fingerFastSearch();
  if (p != FINGERPRINT_OK)  return -1;
  
  // found a match!
  //Serial.print("Found ID #"); Serial.print(finger.fingerID); 
  //Serial.print(" with confidence of "); Serial.println(finger.confidence);
  return finger.fingerID; 
}
