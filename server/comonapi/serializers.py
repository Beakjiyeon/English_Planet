from comondata.models import *
from rest_framework import serializers

class UserSerializer(serializers.ModelSerializer):
	class Meta:
		model=User
		fields="__all__"



class QuizSerializer(serializers.ModelSerializer):
	class Meta:
		model=Quiz
		fields="__all__"

class CameraSerializer(serializers.ModelSerializer):
	class Meta:
		model=Camera
		fields="__all__"	

class BookwordSerializer(serializers.ModelSerializer):
	class Meta:
		model=Bookword
		fields="__all__"	

class BookSerializer(serializers.ModelSerializer):
	class Meta:
		model=Book
		fields="__all__"	

class MywordSerializer(serializers.ModelSerializer):
	class Meta:
		model=Myword
		fields="__all__"
