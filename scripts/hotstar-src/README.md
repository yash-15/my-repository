## HOTSTAR on Ubuntu!

Playing Hotstar videos on Ubuntu has become cumbersome in recent times. This script aims to simplify the process a bit.

### Usage instructions

- Open the [Hotstar](hotstar.com) video in web browser
- Copy the last number in URL (eg: 2001807027) and replace in line 2 of the script
- Run the script as  `$ python script.py ` in terminal
- If the output says success, then a link will get copied to clipboard
- Open [HLS Player](www.hlsplayer.net) in browser and paste the link
- Execute the script again and paste whenever the video stops 

### About

[Hotstar](hotstar.com) streams many cricket and football matches in Indian subcontinent, apart from TV shows and movies. However, the videos won't play on Ubuntu apparently due to limited flash support. 
A workaround for this is discussed [here](https://askubuntu.com/a/873681). The process discussed in Solution #1 is quite tedious.
This script attempts to simplify the task a bit. Generally, the URL has to be updated frequently in HLS Player, especially for live streaming videos. This is where this script helps, as you simply execute it to get the updated URL.

### Requirements

This script runs on python 2.7+ and requires python-pyperclip library
The script was tested on Ubuntu 16.04
