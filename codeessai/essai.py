import serial
import time
nucleo = serial.Serial('/dev/ttyACM0')  # open serial port
print(nucleo.name)         # check which port was really used
print(nucleo.write(b'g'))     # write a string
time.sleep(3)
print(nucleo.write(b' '))     # write a string
time.sleep(5)
print(nucleo.write(b'd'))     # write a string
time.sleep(3)
print(nucleo.write(b' '))     # write a string
nucleo.close()
