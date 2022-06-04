from mutagen.mp3 import MP3
import sys

def mutagen_length(path):
    try:
        audio = MP3(path)
        length = audio.info.length
        return length
    except:
        return None

path = sys.argv[1]
#print(path)
length = mutagen_length(path)
print(str(length))
#print("duration min: " + str(int(length/60)) + ':' + str(int(length%60)))