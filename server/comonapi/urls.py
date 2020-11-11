from django.urls import *
from django.conf.urls import *
from rest_framework.routers import *
from rest_framework.urls import *
from . import views

router = DefaultRouter()
router.register('user',views.UserViewSet)
router.register('quiz',views.QuizViewSet)
router.register('camera', views.CameraViewSet)
router.register('bookword', views.BookwordViewSet)
router.register('book',views.BookViewSet)
router.register('myword',views.MywordViewSet)

urlpatterns=[
	path('',include(router.urls)),
	path('api-auth/',include('rest_framework.urls',namespace='rest_framework')),
	path('classify/', views.img_classifier),
]