import urllib, json, pyperclip
video_id = 2001807027
url = "http://getcdn.hotstar.com/AVS/besc?action=GetCDN&asJson=Y&channel=TABLET&id="+str(video_id)+"&type=VOD"
response = urllib.urlopen(url).read()
data = json.loads(response)
if data["errorDescription"] == "200":
	result = data["resultObj"]["src"]
	pyperclip.copy(result)
	print "Success! Link copied to clipboard! Last four chars : "+result[-4:]
else:
	print "Streaming error! Error code : "+ data["errorDescription"]