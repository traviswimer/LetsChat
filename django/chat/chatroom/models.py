from django.db import models
#from django.utils import timezone
#import datetime


class User(models.Model):
    username = models.CharField(max_length=100)
    password = models.CharField(max_length=128)


class Message(models.Model):
    user = models.ForeignKey(User)
    time = models.DateTimeField('date posted')
    message = models.TextField('posted chat message')
