
from django.urls import path
from cocktailapp import endpoints

urlpatterns = [
    path('users', endpoints.register),
    path('sessions', endpoints.login),
]
