#!/usr/bin/env python
# coding: utf-8

import socket
import serial
import time

nucleo = serial.Serial('/dev/ttyACM0')  # open serial port
print int(nucleo.name)         # check which port was really used

TCP_IP = '0.0.0.0'
TCP_PORT = 5005

socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
socket.bind((TCP_IP, 1337))

socket.listen(1)
client, address = socket.accept()
print "{} connected".format( address )

while True:
        request = client.recv(1)
        if request != "":
                print request
                if request == "g":
                        print(nucleo.write(b'g'))     # write a string
                if request == "d":
                        print(nucleo.write(b'd'))     # write a string                        
                if request == " ":
                        print(nucleo.write(b' '))     # write a string
                
print "Close"
client.close()
stock.close()