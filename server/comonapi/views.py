from django.shortcuts import render
from django.shortcuts import get_object_or_404
from rest_framework import viewsets, routers
from comondata.models import *
from django.http import Http404
from rest_framework import status
from rest_framework.response import Response
from rest_framework.views import APIView
from rest_framework.decorators import *
import comondata
from .serializers import *
from rest_framework import filters
from django_filters.rest_framework import DjangoFilterBackend
import pyrebase
import comonapi.label_image
from django.http import HttpResponse
import json


config = {
"apiKey" : "AIzaSyDbUGhPtiAiWVjPyo9li9e4ptJQ3qIYrvs",
"authDomain": "common-8b09b.firebaseapp.com",
"databaseURL": "https://common-8b09b.firebaseio.com",
"projectId": "common-8b09b",
"storageBucket": "common-8b09b.appspot.com",
"messagingSenderId": "633775581634",
"appId": "1:633775581634:web:129f459a835943277e8e5b",
"measurementId": "G-B5SEK98GR4"
}


@api_view(['GET'])
def img_classifier(request):
	user = request.query_params.get('id',None)
	firebase = pyrebase.initialize_app(config)
	storage = firebase.storage()
	path_on_cloud = "images_classify/"+str(user)+".png"
	storage.child(path_on_cloud).download("comonapi/imgs/"+str(user)+".png")

	#### image detection

	res = comonapi.label_image.main_func(user)
	return HttpResponse(res, content_type="text/json-comment-filtered")




# Create your views here.
class UserViewSet(viewsets.ModelViewSet):
	queryset = User.objects.all()
	serializer_class = UserSerializer
	


class QuizViewSet(viewsets.ModelViewSet):
	queryset = Quiz.objects.all()
	serializer_class = QuizSerializer

	def get_queryset(self):
		qs = super().get_queryset()
		search = self.request.query_params.get('id',None)
		if search:
			qs = qs.filter(b_id=search)

		return qs



class CameraViewSet(viewsets.ModelViewSet):
	queryset = Camera.objects.all()
	serializer_class = CameraSerializer
	# filter_backends = [DjangoFilterBackend]
	# filter_fields =['uid']

class BookwordViewSet(viewsets.ModelViewSet):
	queryset = Bookword.objects.all()
	serializer_class = BookwordSerializer

	def get_queryset(self):
		qs = super().get_queryset()
		search = self.request.query_params.get('id', None)
		if search:
			qs = qs.filter(b_id=search)

		return qs


class BookViewSet(viewsets.ModelViewSet):
	queryset = Book.objects.all()
	serializer_class = BookSerializer

	
class MywordViewSet(viewsets.ModelViewSet):
	queryset = Myword.objects.all()
	serializer_class = MywordSerializer

	def get_queryset(self):
		qs = super().get_queryset()
		search = self.request.query_params.get('id',None)
		if search:
			qs = qs.filter(uid=search)

		return qs






# class LoginView(views.APIView):
# 	def post(self, request, format=None):
# 		data = request.data

# 		username = data.get('uid', None)
# 		password = data.get('pwd', None)

# 		user = authenticate(username=username, password=password)

# 		if user is not None:
# 			if user.is_active:
# 				login(request, user)

# 				return Response(status=stauts.HTTP_200_OK)

# 			else:
# 				return Response(stauts=stauts.HTTP_404_NOT_FOUND)
# 		else:
# 			return Response(status=status.HTTP_404_NOT_FOUND)