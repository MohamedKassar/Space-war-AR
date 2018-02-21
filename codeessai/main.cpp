#include "mbed.h"

#define DELTA 200
#define DELTAMOTOR 0.6
#define DELTATEMPS 0.1
#define DELTAMOTORCORRECTION 0.04
#define POWER_LIMIT 1
#define UNDEFINED '0'
#define PERIOD 1000

class Counter {
  public:
    Counter(PinName pin) : _interrupt(pin) {        // create the InterruptIn on the pin specified to Counter
      _interrupt.rise(this, &Counter::increment); // attach increment function of this counter instance
    }
    void increment() {
      _count++;
    }
    int read() {
      return _count;
    }
    void reset() {
      _count=0;
    }
  private:
    InterruptIn _interrupt;
    volatile int _count;
};


Serial raspberry(USBTX, USBRX); // tx, rx
PwmOut myled(LED1);

//Counter counterDipsy1(PB_13);
//Counter counterDipsy2(PB_14);
//Counter counterTinky1(PB_1);
//Counter counterTinky2(PB_2);
//Counter counterPo1(PC_4);
//Counter counterPo2(PB_15);
//Counter counterLaalaa1(PC_2);
//Counter counterLaalaa2(PC_3);

PwmOut pwmTinky(D9);
DigitalOut dirTinky1(D2);
DigitalOut dirTinky2(D4);
PwmOut pwmDipsy(D10);
DigitalOut dirDipsy1(D7);
DigitalOut dirDipsy2(D8);

PwmOut pwmPo(D3);
DigitalOut dirPo1(PA_11);
DigitalOut dirPo2(PA_12);
PwmOut pwmLaalaa(PA_15);
DigitalOut dirLaalaa1(PA_13);
DigitalOut dirLaalaa2(PA_14);

int cT=0;
int cD=0;
int cL=0;
int cP=0;
//int cG2=0;
//int cD2=0;

float power_Laalaa = 0.0;
float power_Po = 0.0;
float power_Tinky = 0.0;
float power_Dipsy = 0.0;
float power_Tinky_Correctif = 0.0;
float power_Dipsy_Correctif = 0.0; 
int main() {
  myled=1;wait(.5);
  myled=0;wait(.5);
  myled=1;wait(.5);
  myled=0;wait(.5);
  myled=1;wait(.5);

//  raspberry.printf("Hello!\n");

  pwmTinky.period_us(PERIOD);
  pwmDipsy.period_us(PERIOD);
  dirTinky1=0;
  dirTinky2=1;
  dirDipsy1=1;
  dirDipsy2=0;
  pwmTinky.pulsewidth_us(0);
  pwmDipsy.pulsewidth_us(0);

  pwmLaalaa.period_us(PERIOD);
  pwmPo.period_us(PERIOD);
  dirLaalaa1=0;
  dirLaalaa2=1;
  dirPo1=1;
  dirPo2=0;
  pwmLaalaa.pulsewidth_us(0);
  pwmPo.pulsewidth_us(0);

//while(1){}
//  pwmTinky.pulsewidth_us(400);
//  pwmDipsy.pulsewidth_us(400);
//  pwmLaalaa.pulsewidth_us(400);
//  pwmPo.pulsewidth_us(400);
//  wait(100);




  while(1) {
    char c = UNDEFINED;
    if (raspberry.readable()) c=raspberry.getc();
//    raspberry.printf("===DEBUT====\n");
//    cT = counterTinky1.read();
//    cD = counterDipsy1.read();
//    cL = counterLaalaa1.read();
//    cP = counterPo1.read();
//    raspberry.printf("counterTinky, counterDipsy, counterLaalaa, counterPo: %d, %d, %d, %d\n",cT,cD,cL,cP);




//    if((c == 'e') && (power_Dipsy < POWER_LIMIT)) {
//      power_Dipsy += DELTAMOTOR;
//      raspberry.printf("Dipsy Up!\n");
//    }
//    if((c == 'd') && (power_Dipsy > -POWER_LIMIT)) {
//      power_Dipsy -= DELTAMOTOR;
//      raspberry.printf("Dipsy Down!\n");
//    }
//    if((c == 'u') && (power_Tinky < POWER_LIMIT)) {
//      power_Tinky += DELTAMOTOR;
//      raspberry.printf("Tinky Up!\n");
//    }
//    if((c == 'j') && (power_Tinky > -POWER_LIMIT)) {
//      power_Tinky -= DELTAMOTOR;
//      raspberry.printf("Tinky Down!\n");
//    }
//    if((c == 'f') && (power_Po < POWER_LIMIT)) {
//      power_Po += DELTAMOTOR;
//      raspberry.printf("Po Up!\n");
//    }
//    if((c == 'r') && (power_Po > -POWER_LIMIT)) {
//      power_Po -= DELTAMOTOR;
//      raspberry.printf("Po Down!\n");
//    }
//    if((c == 'h') && (power_Laalaa < POWER_LIMIT)) {
//      power_Laalaa += DELTAMOTOR;
//      raspberry.printf("Laalaa Up!\n");
//    }
//    if((c == 'y') && (power_Laalaa > -POWER_LIMIT)) {
//      power_Laalaa -= DELTAMOTOR;
//      raspberry.printf("Laalaa Down!\n");
//    }
//    if((c == 't') && (power_Tinky < POWER_LIMIT) && (power_Dipsy < POWER_LIMIT)) {
//      power_Tinky += DELTAMOTOR;
//      power_Dipsy += DELTAMOTOR;
//      raspberry.printf("Dipsy Tinky Up!\n");
//    }
//    if((c == 'g') && (power_Tinky > -POWER_LIMIT)&& (power_Dipsy > -POWER_LIMIT)) {
//      power_Tinky -= DELTAMOTOR;
//      power_Dipsy -= DELTAMOTOR;
//      raspberry.printf("Dipsy Tinky Down!\n");
//    }



    if(c == 'g') {
      power_Tinky = 0.5;
      power_Dipsy = 0.5;
      power_Laalaa = 0.5;
      power_Po = 0.5;
    }
    if(c == 'd') {
      power_Tinky = -0.5;
      power_Dipsy = -0.5;
      power_Laalaa = -0.5;
      power_Po = -0.5;
    }


    if(c == ' ') {
      power_Tinky = 0;
      power_Dipsy = 0;
      power_Laalaa = 0;
      power_Po = 0;
//      counterPo1.reset();
//      counterLaalaa1.reset();
//      counterDipsy1.reset();
//      counterTinky1.reset();
//      raspberry.printf("Stop!\n");
    } 




    if(power_Tinky>0){
      dirTinky1=1;
      dirTinky2=0;
      pwmTinky.pulsewidth_us(PERIOD*(power_Tinky));
    } else {
      dirTinky1=0;
      dirTinky2=1;
      pwmTinky.pulsewidth_us(-PERIOD*(power_Tinky));
    }
    if(power_Dipsy>0){
      dirDipsy1=0;
      dirDipsy2=1;
      pwmDipsy.pulsewidth_us(PERIOD*(power_Dipsy));
    } else {
      dirDipsy1=1;
      dirDipsy2=0;
      pwmDipsy.pulsewidth_us(-PERIOD*(power_Dipsy));
    }

    if(power_Laalaa>0){
      dirLaalaa1=0;
      dirLaalaa2=1;
      pwmLaalaa.pulsewidth_us(PERIOD*(power_Laalaa));
    } else {
      dirLaalaa1=1;
      dirLaalaa2=0;
      pwmLaalaa.pulsewidth_us(-PERIOD*(power_Laalaa));
    }
    if(power_Po>0){
      dirPo1=1;
      dirPo2=0;
      pwmPo.pulsewidth_us(PERIOD*(power_Po));
    } else {
      dirPo1=0;
      dirPo2=1;
      pwmPo.pulsewidth_us(-PERIOD*(power_Po));
    }

//    raspberry.printf("===FIN====\n");

    wait(DELTATEMPS);

  }
}
