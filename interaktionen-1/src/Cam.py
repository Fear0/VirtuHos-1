import cv2

video = cv2.VideoCapture(0)
check = video.read()
cam_test = open("camTest.txt", "w")
if check[0] :
    cam_test.write("1")
else :
    cam_test.write("0")
video.release()