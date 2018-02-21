make clean; make;
sudo mount /dev/sda /media/nucleo;
sudo cp .build/botbinary.bin /media/nucleo/;sync;
sudo umount /media/nucleo
