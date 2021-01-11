import speech_recognition as sr
mic_test = open("micTest.txt", "w")
try:
    x = sr.Microphone()
    mic_test.write("1")
except OSError:
    mic_test.write("0")
