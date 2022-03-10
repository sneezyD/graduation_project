import os
import requests
import uuid
import time
import json
import cv2

path_dir = 'path'
file_list = os.listdir(path_dir)

api_url = 'API_URL'
secret_key = 'Secret Key'
image_file = 'path' + file_list[0]

request_json = {
    'images': [
        {
            'format': 'jpg',
            'name': file_list[0]
        }
    ],
    'requestId': str(uuid.uuid4()),
    'version': 'V2',
    'timestamp': int(round(time.time() * 1000))
}

payload = {'message': json.dumps(request_json).encode('UTF-8')}
files = [
  ('file', open(image_file,'rb'))
]
headers = {
  'X-OCR-SECRET': secret_key
}

response = requests.request("POST", api_url, headers=headers, data=payload, files=files)
res = json.loads(response.text)
res = res.get('images')

fields = res[0]['fields']

src = cv2.imread('1.jpg')
for i in fields:
    field_name = i['name']
    inferText = i['inferText']
    point = i['boundingPoly']['vertices']

    point0 = point[0]
    point2 = point[2]
    dst = src[int(point0['y']):int(point2['y']), int(point0['x']):int(point2['x'])].copy()

    cv2.imshow("dst", dst)
    cv2.waitKey()
    cv2.destroyAllWindows()
