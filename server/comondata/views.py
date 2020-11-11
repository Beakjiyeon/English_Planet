from django.shortcuts import render
import json
from .models import *
from django.views import View
from django.http import HttpResponse, JsonResponse
from django.views.decorators.csrf import csrf_exempt

import requests

from bs4 import BeautifulSoup

def req(requests):
	crawling()
	return HttpResponse("webcrawling")

def crawling():
	res = requests.get('https://www.freechildrenstories.com/why-the-cricket-chirps')	
	html = res.text
	soup = BeautifulSoup(html, 'html.parser')
	divs = soup.find_all(class_="sqs-block-content")

	text_list=""

	for div in divs[3:len(divs)-7]:
		div=div.get_text().rstrip('\n')
		if div=="" or div == "&nbsp" or div=="\n" or div== " ":
			continue
		else:
			text_list+=div

	b = Book(b_text=text_list)
	b.save()


#	print(text_list)
	
