import pyrebase

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

def img_downloader(self,user):

	firebase = pyrebase.initialize_app(config)
	storage = firebase.storage()

	# firebase : path
	path_on_cloud = "images_classify/"+user+".png"

	# local : path
	#path_local = "test.png"

	#storage.child(path_on_cloud).put(path_local)
	storage.child(path_on_cloud).download("img_classify/test_download.png")